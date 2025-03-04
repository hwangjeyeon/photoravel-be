package trendravel.photoravel_be.common.matching.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trendravel.photoravel_be.common.exception.error.MatchingErrorCode;
import trendravel.photoravel_be.common.exception.error.MemberErrorCode;
import trendravel.photoravel_be.common.exception.error.PhotographerErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.db.match.enums.MatchingStatus;
import trendravel.photoravel_be.db.photographer.Photographer;
import trendravel.photoravel_be.db.respository.matching.MatchingRepository;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.db.respository.photographer.PhotographerRepository;
import trendravel.photoravel_be.common.matching.dto.request.MatchingRequestDto;
import trendravel.photoravel_be.common.matching.dto.response.MatchingResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;
    private final MatchingRepository matchingRepository;
    
    @Transactional
    public List<MatchingResponseDto> getMatchingListByMemberId(String memberId) {
        return matchingRepository.getMatchingListByMemberId(memberId);
    }
    
    @Transactional
    public List<MatchingResponseDto> getMatchingListByPhotographerId(String accountId) {
        return matchingRepository.getMatchingListByPhotographerId(accountId);
    }
    
    @Transactional
    public void pendingMatching(MatchingRequestDto matchingRequestDto) {
        // 멤버가 존재하는지 검증
        if (!memberRepository.existsByMemberId(matchingRequestDto.getMemberId())) {
            throw new ApiException(MemberErrorCode.USER_NOT_FOUND);
        }
        
        // 사진작가가 존재하는지 검증
        if (!photographerRepository.existsByAccountId(matchingRequestDto.getPhotographerId())) {
            throw new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND);
        }
        
        // 멤버가 pending, accept의 매칭을 이미 가지고 있는지 검증
        boolean memberHasActiveMatching = matchingRepository.existsByMemberIdAndStatusIn(
                matchingRequestDto.getMemberId(), List.of(MatchingStatus.PENDING, MatchingStatus.ACCEPTED));
        
        if (memberHasActiveMatching) {
            throw new ApiException(MatchingErrorCode.MEMBER_ALREADY_HAS_MATCHING);
        }
        
        matchingRepository.save(Matching.builder()
                .memberId(matchingRequestDto.getMemberId())
                .photographerId(matchingRequestDto.getPhotographerId())
                .status(MatchingStatus.PENDING)
                .build());
    }
    
    //expectedStatus인 상태의 매칭을 찾아서 newStatus로 수정
    public void updateMatchingStatus(MatchingRequestDto matchingRequestDto, MatchingStatus newStatus, MatchingStatus expectedStatus) {
        Matching matching = matchingRepository.findByMemberIdAndStatus(
                matchingRequestDto.getMemberId(), expectedStatus).orElseThrow(
                () -> new ApiException(MatchingErrorCode.MATCHING_STATUS_INVALID));
        
        matching.updateStatus(newStatus);
    }
    
    // 매칭 취소
    @Transactional
    public void cancel(MatchingRequestDto matchingRequestDto) {
        updateMatchingStatus(matchingRequestDto, MatchingStatus.CANCELED, MatchingStatus.PENDING);
    }
    
    // 사진작가가 수락
    @Transactional
    public void accept(MatchingRequestDto matchingRequestDto) {
        updateMatchingStatus(matchingRequestDto, MatchingStatus.ACCEPTED, MatchingStatus.PENDING);
    }
    
    // 사진작가가 거절
    @Transactional
    public void reject(MatchingRequestDto matchingRequestDto) {
        updateMatchingStatus(matchingRequestDto, MatchingStatus.REJECTED, MatchingStatus.PENDING);
    }
    
    // 사진작가가 완료
    @Transactional
    public void complete(MatchingRequestDto matchingRequestDto) {
        updateMatchingStatus(matchingRequestDto, MatchingStatus.COMPLETED, MatchingStatus.ACCEPTED);
        
        Photographer photographer =
                photographerRepository.findByAccountId(matchingRequestDto.getPhotographerId()).orElseThrow(
                        () -> new ApiException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND));
        photographer.increaseMatchingCount();
    }
}

package trendravel.photoravel_be.domain.guidebook.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trendravel.photoravel_be.common.exception.error.GuidebookErrorCode;
import trendravel.photoravel_be.common.exception.error.MemberErrorCode;
import trendravel.photoravel_be.common.exception.ApiException;
import trendravel.photoravel_be.common.image.service.ImageServiceFacade;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.respository.member.MemberRepository;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateImageDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookListResponseDto;
import trendravel.photoravel_be.domain.guidebook.dto.response.GuidebookResponseDto;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.respository.guidebook.GuidebookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuidebookService {
    
    private final GuidebookRepository guidebookRepository;
    private final ImageServiceFacade imageServiceFacade;
    private final MemberRepository memberRepository; 
    
    @Transactional
    public GuidebookResponseDto createGuidebook(
            GuidebookRequestDto guidebookRequestDto,
            List<MultipartFile> images) {
        
        Guidebook guidebook = Guidebook.builder()
                .title(guidebookRequestDto.getTitle())
                .content(guidebookRequestDto.getContent())
                .region(guidebookRequestDto.getRegion())
                .images(imageServiceFacade.uploadImageFacade(images))
                .views(0)
                .build();
        
        MemberEntity member = memberRepository.findByMemberId(guidebookRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        
        guidebook.setMemberGuidebook(member);
        guidebookRepository.save(guidebook);
        
        return GuidebookResponseDto.builder()
                .id(guidebook.getId())
                .userId(member.getMemberId())
                .title(guidebook.getTitle())
                .content(guidebook.getContent())
                .region(guidebook.getRegion())
                .images(guidebook.getImages())
                .createdAt(guidebook.getCreatedAt())
                .updatedAt(guidebook.getUpdatedAt())
                .views(guidebook.getViews())
                .build();
    }
    
    @Transactional
    public GuidebookResponseDto createGuidebook(
            GuidebookRequestDto guidebookRequestDto) {
        
        Guidebook guidebook = Guidebook.builder()
                .title(guidebookRequestDto.getTitle())
                .content(guidebookRequestDto.getContent())
                .region(guidebookRequestDto.getRegion())
                .views(0)
                .build();
        
        MemberEntity member = memberRepository.findByMemberId(guidebookRequestDto.getUserId())
                .orElseThrow(() -> new ApiException(MemberErrorCode.USER_NOT_FOUND));
        
        guidebook.setMemberGuidebook(member);
        guidebookRepository.save(guidebook);
        
        return GuidebookResponseDto.builder()
                .id(guidebook.getId())
                .userId(member.getMemberId())
                .title(guidebook.getTitle())
                .content(guidebook.getContent())
                .region(guidebook.getRegion())
                .createdAt(guidebook.getCreatedAt())
                .updatedAt(guidebook.getUpdatedAt())
                .views(guidebook.getViews())
                .build();
    }
    
    
    @Transactional
    public List<GuidebookListResponseDto> getGuidebookList(String region) {
        
        List<Guidebook> guidebooks;
        
        /*
        region이 all로 들어오면 모든 가이드북 반환
        region이 지역으로 들어오면 해당 지역으로 검색
        키워드 기반 가이드북은 프론트 측과 상의하여 잠시 보류
         */
        
        if (region.equals("newest")) {
            guidebooks = guidebookRepository.getGuidebookByNewest();
        } else if (region.equals("view")) {
            guidebooks = guidebookRepository.getGuidebookByViews();
        } else { //지역으로 검색
            guidebooks = guidebookRepository.getGuidebookByRegion(Region.valueOf(region));
        }
        
        if (guidebooks.isEmpty()) {
            throw new ApiException(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND);
        }
        
        
        return guidebooks.stream()
                .map(guidebook -> GuidebookListResponseDto.builder()
                        .id(guidebook.getId())
                        .userId(guidebook.getMember().getMemberId())
                        .title(guidebook.getTitle())
                        .region(guidebook.getRegion())
                        .views(guidebook.getViews())
                        .image((guidebook.getImages() == null || guidebook.getImages().isEmpty())
                                ? null
                                : guidebook.getImages().get(0))
                        .createdAt(guidebook.getCreatedAt())
                        .updatedAt(guidebook.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    
    @Transactional
    public GuidebookResponseDto getGuidebook(Long guidebookId) {
        
        Guidebook guidebook = guidebookRepository.findById(guidebookId).orElseThrow(
                () -> new ApiException(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND));
        
        guidebookRepository.increaseViewCount(guidebookId);
        
        return GuidebookResponseDto.builder()
                .id(guidebook.getId())
                .userId(guidebook.getMember().getMemberId())
                .title(guidebook.getTitle())
                .content(guidebook.getContent())
                .region(guidebook.getRegion())
                .views(guidebook.getViews())
                .images(guidebook.getImages())
                .createdAt(guidebook.getCreatedAt())
                .updatedAt(guidebook.getUpdatedAt())
                .build();
    }
    
    @Transactional
    public GuidebookResponseDto updateGuidebook(GuidebookUpdateImageDto guidebookUpdateImageDto,
                                                List<MultipartFile> images) {
        
        Guidebook guidebook = guidebookRepository.findById(guidebookUpdateImageDto.getId()).orElseThrow(
                () -> new ApiException(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND));
        
        
        guidebook.updateGuidebook(guidebookUpdateImageDto,
                imageServiceFacade.updateImageFacade(images, guidebookUpdateImageDto.getDeleteImages()));
        
        return GuidebookResponseDto.builder()
                .id(guidebook.getId())
                .userId(guidebook.getMember().getMemberId())
                .title(guidebook.getTitle())
                .content(guidebook.getContent())
                .region(guidebook.getRegion())
                .views(guidebook.getViews())
                .images(guidebook.getImages())
                .createdAt(guidebook.getCreatedAt())
                .updatedAt(guidebook.getUpdatedAt())
                .build();
    }
    
    @Transactional
    public GuidebookResponseDto updateGuidebook(GuidebookUpdateDto guidebookUpdateDto) {
        
        Guidebook guidebook = guidebookRepository.findById(guidebookUpdateDto.getId()).orElseThrow(
                () -> new ApiException(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND));
        
        
        guidebook.updateGuidebook(guidebookUpdateDto);
        
        return GuidebookResponseDto.builder()
                .id(guidebook.getId())
                .userId(guidebook.getMember().getMemberId())
                .title(guidebook.getTitle())
                .content(guidebook.getContent())
                .region(guidebook.getRegion())
                .views(guidebook.getViews())
                .createdAt(guidebook.getCreatedAt())
                .updatedAt(guidebook.getUpdatedAt())
                .build();
    }
    
    @Transactional
    public void deleteGuidebook(Long guidebookId) {
        Guidebook guidebook = guidebookRepository.findById(guidebookId).orElseThrow(
                () -> new ApiException(GuidebookErrorCode.GUIDEBOOK_NOT_FOUND));
        guidebookRepository.deleteById(guidebookId);
        
        if (guidebook.getImages() != null) {
            imageServiceFacade.deleteAllImagesFacade(guidebook.getImages());
        }
        
    }
    
    
}

package trendravel.photoravel_be.common.exception.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MatchingErrorCode implements ErrorCodeIfs{
    
    MATCHING_STATUS_INVALID(400, "매칭 상태가 올바르지 않습니다"),
    MEMBER_ALREADY_HAS_MATCHING(400 , "멤버는 이미 매칭이 존재합니다");
    

    private final Integer httpStatusCode;
    private final String errorDescription;
}

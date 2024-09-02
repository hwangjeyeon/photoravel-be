package trendravel.photoravel_be.db.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trendravel.photoravel_be.db.match.enums.MatchingStatus;
import trendravel.photoravel_be.db.member.MemberEntity;
import trendravel.photoravel_be.db.photographer.Photographer;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "MATCHING")
public class Matching {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private MatchingStatus status;
    
    
    private String memberId;

    private String photographerId;
    
    
}

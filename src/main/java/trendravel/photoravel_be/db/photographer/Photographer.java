package trendravel.photoravel_be.db.photographer;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.match.Matching;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.photographer.dto.request.PhotographerUpdateDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PHOTOGRAPHER")
public class Photographer extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //이름 수정 필요
    @Column(length = 50, nullable = false, unique = true)
    private String accountId;
    
    @Column(length = 50, nullable = false)
    private String password;
    
    @Column(length = 50, nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;
    
    private String description;
    
    @Column(nullable = false)
    private String profileImg;
    
    @Column(nullable = false)
    private Integer careerYear;
    
    @Column(nullable = false)
    private Integer matchingCount;
    
    //images 타입에 대해 수정 필요 imageService 단에서 하나의 이미지를 처리하는 메서드 필요
    public void updatePhotographer(PhotographerUpdateDto photographerUpdateDto, List<String> images) {
        this.password = photographerUpdateDto.getPassword();
        this.name = photographerUpdateDto.getName();
        this.region = photographerUpdateDto.getRegion();
        this.description = photographerUpdateDto.getDescription();
        this.profileImg = images.get(0);
    }
    
    public void updatePhotographer(PhotographerUpdateDto photographerUpdateDto) {
        this.password = photographerUpdateDto.getPassword();
        this.name = photographerUpdateDto.getName();
        this.region = photographerUpdateDto.getRegion();
        this.description = photographerUpdateDto.getDescription();
    }
    
    @OneToMany(mappedBy = "photographerReview", orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    
    public void increaseMatchingCount() {
        this.matchingCount++;
    }
    
}

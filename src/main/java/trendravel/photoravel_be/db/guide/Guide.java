package trendravel.photoravel_be.db.guide;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.db.review.Review;
import trendravel.photoravel_be.domain.guide.dto.request.GuideRequestDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "GUIDE")
public class Guide extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //이름 수정 필요
    @Column(length = 50, nullable = false)
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
    
    //images 타입에 대해 수정 필요 imageService 단에서 하나의 이미지를 처리하는 메서드 필요
    public void updateGuide(GuideRequestDto guideRequestDto, List<String> images) {
        this.password = guideRequestDto.getPassword();
        this.name = guideRequestDto.getName();
        this.region = guideRequestDto.getRegion();
        this.description = guideRequestDto.getDescription();
        this.profileImg = images.get(0);
    }
    
    @OneToMany(mappedBy = "guideReview")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    
}

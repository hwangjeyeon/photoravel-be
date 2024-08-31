package trendravel.photoravel_be.db.guidebook;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;
import trendravel.photoravel_be.db.enums.Region;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "GUIDEBOOK")
public class Guidebook extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String title;
    
    @Lob
    @Column(nullable = false)
    private String content;
    
    
    @ElementCollection
    @CollectionTable(
            name = "guidebook_images",
            joinColumns = @JoinColumn(name = "id")
    )
    private List<String> images = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;
    
    private int views;
    
    public void updateGuidebook(GuidebookRequestDto guidebookRequestDto, List<String> images) {
        this.title = guidebookRequestDto.getTitle();
        this.content = guidebookRequestDto.getContent();
        this.region = guidebookRequestDto.getRegion();
        this.images = images;
    }
    
    public void updateGuidebook(GuidebookRequestDto guidebookRequestDto) {
        this.title = guidebookRequestDto.getTitle();
        this.content = guidebookRequestDto.getContent();
        this.region = guidebookRequestDto.getRegion();
    }
    
    public void increaseView() {
        this.views++;
    }
}

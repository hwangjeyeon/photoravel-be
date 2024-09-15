package trendravel.photoravel_be.db.guidebook;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import trendravel.photoravel_be.db.BaseEntity;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookRequestDto;
import trendravel.photoravel_be.db.enums.Region;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateDto;
import trendravel.photoravel_be.domain.guidebook.dto.request.GuidebookUpdateImageDto;

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
    
    @Column(nullable = false, columnDefinition = "TEXT")
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
    
    public void updateGuidebook(GuidebookUpdateImageDto guidebookUpdateImageDto, List<String> newImages) {
        this.title = guidebookUpdateImageDto.getTitle();
        this.content = guidebookUpdateImageDto.getContent();
        this.region = guidebookUpdateImageDto.getRegion();
        
        for (String deleteImage : guidebookUpdateImageDto.getDeleteImages()) {
            this.images.remove(deleteImage);
        }
        this.images.addAll(newImages);
    }
    
    public void updateGuidebook(GuidebookUpdateDto guidebookUpdateDto) {
        this.title = guidebookUpdateDto.getTitle();
        this.content = guidebookUpdateDto.getContent();
        this.region = guidebookUpdateDto.getRegion();
    }
    
    public void increaseView() {
        this.views++;
    }
}

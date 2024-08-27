package trendravel.photoravel_be.db.respository.guidebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.guidebook.Guidebook;
import trendravel.photoravel_be.db.enums.Region;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuidebookRepository extends JpaRepository<Guidebook, Long> {
    
    List<Guidebook> findByRegion(Region region);

    
}

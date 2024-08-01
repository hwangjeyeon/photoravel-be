package trendravel.photoravel_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}

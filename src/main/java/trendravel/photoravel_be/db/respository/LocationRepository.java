package trendravel.photoravel_be.db.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.db.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}

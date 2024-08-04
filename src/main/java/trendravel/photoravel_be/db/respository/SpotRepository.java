package trendravel.photoravel_be.db.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.db.spot.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long> {

}

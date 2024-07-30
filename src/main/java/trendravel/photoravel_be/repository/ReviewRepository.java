package trendravel.photoravel_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}

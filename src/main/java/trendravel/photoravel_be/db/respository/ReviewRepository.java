package trendravel.photoravel_be.db.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.db.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}

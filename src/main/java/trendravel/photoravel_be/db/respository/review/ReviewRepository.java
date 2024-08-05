package trendravel.photoravel_be.db.respository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import trendravel.photoravel_be.db.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}

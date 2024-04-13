package codingTechniques.repositories;

import codingTechniques.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // You can add custom query methods here if needed
}

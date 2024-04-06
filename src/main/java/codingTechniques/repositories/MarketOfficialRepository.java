package codingTechniques.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import codingTechniques.model.MarketOfficial;

@Repository
public interface MarketOfficialRepository extends JpaRepository<MarketOfficial, Long> {

	MarketOfficial findByUserEmail(String email);
    // You can add custom query methods if needed
}

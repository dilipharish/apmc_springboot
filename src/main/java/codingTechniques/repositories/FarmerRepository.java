package codingTechniques.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import codingTechniques.model.Farmer;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
	 Farmer findByUserEmail(String email);
}


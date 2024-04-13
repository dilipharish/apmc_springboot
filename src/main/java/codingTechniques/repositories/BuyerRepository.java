package codingTechniques.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import codingTechniques.model.Buyer;
import codingTechniques.model.FinalCrop;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

	Buyer findByUserEmail(String email);
    // You can add custom query methods if needed

	Optional<FinalCrop> findById(Buyer buyerId);
}

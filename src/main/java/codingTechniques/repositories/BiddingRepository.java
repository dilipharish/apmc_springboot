package codingTechniques.repositories;



import codingTechniques.model.Bidding;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, Long> {

	  @Query("SELECT MAX(b.bidAmount) FROM Bidding b WHERE b.finalCrop.id = :finalCropId")
	    Double findMaxBidAmountByFinalCropId(@Param("finalCropId") Long finalCropId);

	List<Bidding> findAllByFinalCropId(Long finalCropId);

	int countBidsByFinalCropId(Long finalCropId);
}

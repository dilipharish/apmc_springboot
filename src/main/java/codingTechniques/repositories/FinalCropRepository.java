package codingTechniques.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codingTechniques.model.Farmer;
import codingTechniques.model.FinalCrop;

@Repository
public interface FinalCropRepository extends JpaRepository<FinalCrop, Long> {

    // Check if a final crop exists with a specific draft crop ID
    boolean existsByDraftCropId(Long id);

    // Retrieve approved crops for a specific buyer
    List<FinalCrop> findByBuyerId(Long buyerId);
    
    // Retrieve approved crops by draft crop ID
    List<FinalCrop> findByDraftCropId(Long draftCropId);

	List<FinalCrop> findByBuyerIsNull();

	List<FinalCrop> findByBuyerIdIsNull();

	List<FinalCrop> findByDraftCropFarmerId(Long farmerId);

	List<FinalCrop> findByDraftCrop_FarmerAndBuyerIsNotNull(Farmer farmer);

	List<FinalCrop> findByBuyerIdNotNull();
}

package codingTechniques.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codingTechniques.model.CropStatus;
import codingTechniques.model.DraftCrop;
import codingTechniques.model.Farmer;

@Repository
public interface DraftCropRepository extends JpaRepository<DraftCrop, Long> {
    // You can add custom query methods if needed
	List<DraftCrop> findByFarmer(Farmer farmer);

	List<DraftCrop> findByStatus(CropStatus approved);
}

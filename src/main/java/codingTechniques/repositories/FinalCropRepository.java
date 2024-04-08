package codingTechniques.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codingTechniques.model.FinalCrop;

@Repository
public interface FinalCropRepository extends JpaRepository<FinalCrop, Long> {

	boolean existsByDraftCropId(Long id);
    // You can add custom query methods if needed
}

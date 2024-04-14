package codingTechniques.repositories;

import java.io.ByteArrayInputStream;

import codingTechniques.model.FinalCrop;

public interface FinalCropPDFRepository {

	ByteArrayInputStream generateFinalCropPDF(FinalCrop finalCrop);

}

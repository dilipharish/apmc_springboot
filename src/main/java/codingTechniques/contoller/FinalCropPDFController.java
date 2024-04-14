//package codingTechniques.contoller;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.http.HttpHeaders;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import codingTechniques.model.FinalCrop;
//import codingTechniques.repositories.FinalCropPDFRepository;
//import codingTechniques.repositories.FinalCropRepository;
//
//@Controller
//public class FinalCropPDFController {
//
//    @Autowired
//    private FinalCropRepository finalCropRepository;
//
//    @Autowired
//    private FinalCropPDFRepository finalCropPDFRepository;
//
//    @GetMapping("/farmer/{farmerId}/generatePDFs")
//    public ResponseEntity<byte[]> generatePDFsForFarmer(@PathVariable("farmerId") Long farmerId) {
//        // Retrieve all FinalCrops for the given farmerId
//        List<FinalCrop> finalCrops = finalCropRepository.findByDraftCropFarmerId(farmerId);
//
//        // Create a ByteArrayOutputStream to store the combined PDF content
//        ByteArrayOutputStream combinedPDFs = new ByteArrayOutputStream();
//
//        try {
//            // Iterate over each FinalCrop and generate a PDF
//            for (FinalCrop finalCrop : finalCrops) {
//                // Generate PDF for the current FinalCrop
//                ByteArrayInputStream pdfBytes = finalCropPDFRepository.generateFinalCropPDF(finalCrop);
//
//                // Append the PDF content to the combined PDF
//                while (pdfBytes.available() > 0) {
//                    combinedPDFs.write(pdfBytes.read());
//                }
//
//                // Add a separator between PDFs (optional)
//                combinedPDFs.write("\n".getBytes());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//
//        // Set headers for the response
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("filename", "final_crops.pdf");
//
//        // Return the combined PDF content as a byte array
//        return ResponseEntity.ok().headers(headers).body(combinedPDFs.toByteArray());
//    }
//}

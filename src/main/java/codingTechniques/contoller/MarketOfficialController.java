package codingTechniques.contoller;



import java.util.List;
import codingTechniques.model.CropStatus;
import codingTechniques.model.DraftCrop;
import codingTechniques.repositories.DraftCropRepository;
import codingTechniques.repositories.FinalCropRepository;
import codingTechniques.repositories.MarketOfficialRepository;
import codingTechniques.repositories.ReviewRepository;
import codingTechniques.model.FinalCrop;
import codingTechniques.model.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MarketOfficialController {

    @Autowired
    private MarketOfficialRepository marketOfficialRepository;

    @Autowired
    private DraftCropRepository draftCropRepository;
    @Autowired
    private FinalCropRepository finalCropRepository ;
    
    @Autowired
    private ReviewRepository reviewRepository;
    // Existing method to display market official dashboard
    @GetMapping("/marketOfficial/{marketOfficialId}/dashboard")
    public String marketOfficialDashboard(@PathVariable("marketOfficialId") Long marketOfficialId, Model model) {
        // Implementation to retrieve market official details and necessary data
        List<DraftCrop> draftCrops = draftCropRepository.findAll();
        model.addAttribute("draftCrops", draftCrops);
        return "marketOfficial/dashboard";
    }

    // New method to handle changing the status of a draft crop
    @PostMapping("/marketOfficial/{marketOfficialId}/updateCropStatus/{draftCropId}")
    public String updateCropStatus(@PathVariable("marketOfficialId") Long marketOfficialId,
                                   @PathVariable("draftCropId") Long draftCropId,
                                   @RequestParam("status") String status) {
        // Retrieve the draft crop
        DraftCrop draftCrop = draftCropRepository.findById(draftCropId).orElse(null);
        if (draftCrop == null) {
            // Handle case where draft crop is not found
            return "error";
        }
        
        // Check if the status is valid
        if (status.equalsIgnoreCase("APPROVED")) {
            draftCrop.setStatus(CropStatus.APPROVED);
        } else if (status.equalsIgnoreCase("REJECTED")) {
            draftCrop.setStatus(CropStatus.REJECTED);
        } else {
            // Handle invalid status
            return "error";
        }
        
        // Save the updated draft crop
        draftCropRepository.save(draftCrop);

        // Redirect back to the market official dashboard
        return "redirect:/marketOfficial/" + marketOfficialId + "/dashboard";
    }
    
    @PostMapping("/marketOfficial/{marketOfficialId}/addApprovedCrops")
    public String addApprovedCropsToFinal(@PathVariable("marketOfficialId") Long marketOfficialId) {
        // Retrieve all approved draft crops
        List<DraftCrop> approvedDraftCrops = draftCropRepository.findByStatus(CropStatus.APPROVED);

        // Iterate over approved draft crops and add them to final crops if they don't already exist
        for (DraftCrop draftCrop : approvedDraftCrops) {
            // Check if a FinalCrop with the same draft_crop_id already exists
            if (finalCropRepository.existsByDraftCropId(draftCrop.getId())) {
                // Skip adding this draftCrop to final crops if it already exists
                continue;
            }

            // Create a new FinalCrop entity and save it
            FinalCrop finalCrop = new FinalCrop();
            finalCrop.setDraftCrop(draftCrop);
            // Set other properties of final crop if needed
            finalCropRepository.save(finalCrop);
        }

        // Redirect back to the market official dashboard
        return "redirect:/marketOfficial/" + marketOfficialId + "/dashboard";
    }
    @GetMapping("/marketOfficial/{marketOfficialId}/reviews")
    public String viewReviews(Model model) {
        List<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        return "marketOfficial/reviews";
    }

    
}

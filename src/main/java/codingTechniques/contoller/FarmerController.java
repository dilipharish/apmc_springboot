package codingTechniques.contoller;

import codingTechniques.model.DraftCrop;
import codingTechniques.model.Farmer;
import codingTechniques.repositories.DraftCropRepository;
import codingTechniques.repositories.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.context.SecurityContextHolder;
//import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FarmerController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private DraftCropRepository draftCropRepository;

    @GetMapping("/farmer/{farmerId}/dashboard")
    public String showFarmerDashboard(Model model, @PathVariable("farmerId") Long farmerId) {
        // Retrieve the farmer using the farmerId
        Farmer currentFarmer = farmerRepository.findById(farmerId).orElse(null);

        if (currentFarmer == null) {
            // Handle the case where the farmer is not found
            return "error";
        }

        // Retrieve all crops belonging to the current farmer
        List<DraftCrop> crops = draftCropRepository.findByFarmer(currentFarmer);

        // Add the current farmer and crops to the view
        model.addAttribute("farmer", currentFarmer);
        model.addAttribute("crops", crops);
        model.addAttribute("draftCrop", new DraftCrop());

        return "farmer/dashboard";
    }

    @GetMapping("/farmer/{farmerId}/addCrop")
    public String showAddCropForm(Model model, @PathVariable("farmerId") Long farmerId) {
        // Retrieve the farmer using the farmerId
        Farmer currentFarmer = farmerRepository.findById(farmerId).orElse(null);

        if (currentFarmer == null) {
            // Handle the case where the farmer is not found
            return "error";
        }

        // Add the current farmer to the model
        model.addAttribute("farmer", currentFarmer);
        model.addAttribute("draftCrop", new DraftCrop());

        return "farmer/addCrop";
    }

    @PostMapping("/farmer/{farmerId}/addCrop")
    public String addCrop(@ModelAttribute("draftCrop") DraftCrop draftCrop, @PathVariable("farmerId") Long farmerId) {
        // Retrieve the farmer using the farmerId
        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);

        if (farmer == null) {
            // Handle the case where the farmer is not found
            return "redirect:/error";
        }

        // Set the farmer for the draftCrop
        draftCrop.setFarmer(farmer);

        // Save the crop to the database
        draftCropRepository.save(draftCrop);

        // Redirect back to the farmer dashboard
        return "redirect:/farmer/" + farmerId + "/dashboard";
    }
  
    @GetMapping("/logout")
    public String logout() {
        // Logout the current user
//        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}

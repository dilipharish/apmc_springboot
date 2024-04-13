package codingTechniques.contoller;


import codingTechniques.model.DraftCrop;
import codingTechniques.repositories.DraftCropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AudienceController {

    @Autowired
    private DraftCropRepository draftCropRepository;

    @GetMapping("/audience")
    public String showDraftCrops(Model model) {
        // Retrieve all draft crops
        List<DraftCrop> draftCrops = draftCropRepository.findAll();
        // Add draft crops to the model
        model.addAttribute("draftCrops", draftCrops);
        // Return the audience view
        return "audience";
    }
}

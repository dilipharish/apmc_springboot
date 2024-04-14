package codingTechniques.contoller;

import codingTechniques.model.Farmer;
import codingTechniques.model.Buyer;
import codingTechniques.model.FinalCrop;
import codingTechniques.model.Issue;
import codingTechniques.repositories.FarmerRepository;
import codingTechniques.repositories.BuyerRepository;
import codingTechniques.repositories.FinalCropRepository;
import codingTechniques.repositories.IssueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private FinalCropRepository finalCropRepository;

    @GetMapping("/issue/{marketOfficerId}/{farmerId}/{buyerId}/{finalCropId}")
    public String showIssuePage(@PathVariable Long marketOfficerId, @PathVariable Long farmerId, @PathVariable Long buyerId, @PathVariable Long finalCropId, Model model) {
        List<Issue> issues = issueRepository.findByFinalCropsIdOrderBySender(finalCropId);
        model.addAttribute("issues", issues);
        model.addAttribute("marketOfficerId", marketOfficerId);
        model.addAttribute("farmerId", farmerId);
        model.addAttribute("buyerId", buyerId);
        model.addAttribute("finalCropId", finalCropId);
        return "issue/issue";
    }


    // Other controller methods...
}

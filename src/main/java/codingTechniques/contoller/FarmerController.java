package codingTechniques.contoller;

import codingTechniques.model.DraftCrop;
import codingTechniques.model.Farmer;
import codingTechniques.model.FinalCrop;
import codingTechniques.model.Issue;
import codingTechniques.model.Review;
import codingTechniques.repositories.DraftCropRepository;
import codingTechniques.repositories.FarmerRepository;
import codingTechniques.repositories.FinalCropRepository;
import codingTechniques.repositories.IssueRepository;
import codingTechniques.repositories.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
//import org.springframework.security.core.context.SecurityContextHolder;
//import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FarmerController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private DraftCropRepository draftCropRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FinalCropRepository finalCropRepository;
    @Autowired
    private IssueRepository issueRepository;


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
    @GetMapping("/farmer/{farmerId}/addReview")
    public String showAddReviewForm(Model model, @PathVariable("farmerId") Long farmerId) {
        // Retrieve the farmer using the farmerId
        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);
        if (farmer == null) {
            // Handle the case where the farmer is not found
            return "redirect:/error";
        }

        // Add the farmer to the model
        model.addAttribute("farmer", farmer);
        model.addAttribute("review", new Review());

        return "farmer/addReview";
    }

    @PostMapping("/farmer/{farmerId}/addReview")
    public String addReview(@ModelAttribute("review") Review review, @PathVariable("farmerId") Long farmerId) {
        // Retrieve the farmer using the farmerId
        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);
        if (farmer == null) {
            // Handle the case where the farmer is not found
            return "redirect:/error";
        }

        // Set the user for the review
        review.setUser(farmer.getUser()); // Assuming user_id for farmer is the same as the user_id for review
        
        // Save the review to the database
        reviewRepository.save(review);

        // Redirect back to the farmer dashboard
        return "redirect:/farmer/" + farmerId + "/dashboard";
    }
    @GetMapping("/farmer/{farmerId}/transactions")
    public String showFarmerTransactions(@PathVariable("farmerId") Long farmerId, Model model) {
        // Retrieve the farmer using the farmerId
        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);

        if (farmer == null) {
            // Handle the case where the farmer is not found
            return "error";
        }

        // Retrieve final crops where the farmer's draft crops are referenced and buyer ID is not null
        List<FinalCrop> transactions = finalCropRepository.findByDraftCrop_FarmerAndBuyerIsNotNull(farmer);

        // Add transactions to the view
        model.addAttribute("transactions", transactions);

        return "farmer/transactions";
    }
    @GetMapping("/farmer/{farmerId}/raiseIssue/{transactionId}")
    public String showRaiseIssueForm(@PathVariable("farmerId") Long farmerId, @PathVariable("transactionId") Long transactionId, Model model) {
        // Fetch the transaction
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Add transaction details to the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("farmerId", farmerId);

        // Add an empty Issue object to the model
        model.addAttribute("issue", new Issue());

        return "farmer/raiseIssue";
    }

    @PostMapping("/farmer/{farmerId}/raiseIssue/{transactionId}")
    public String raiseIssue(@PathVariable("farmerId") Long farmerId, @PathVariable("transactionId") Long transactionId, @ModelAttribute("issue") Issue issue) {
        // Retrieve the farmer using the farmerId
        Farmer farmer = farmerRepository.findById(farmerId).orElse(null);
        if (farmer == null) {
            // Handle the case where the farmer is not found
            return "redirect:/error";
        }

        // Retrieve the transaction using the transactionId
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Set the farmer, buyer, and final crops for the issue
        issue.setFarmer(farmer);
        issue.setBuyer(transaction.getBuyer());
        issue.setFinalCropsId(transaction.getId());
        issue.setSender("farmer");
        issue.setTimestamp(LocalDateTime.now());// Assuming the farmer is the sender

        // Save the issue to the database
        issueRepository.save(issue);

        // Redirect to the issue page
        return "redirect:/issue/" + "102" + "/" + farmerId + "/" + issue.getBuyer().getId() + "/" + transactionId;
    }

    @GetMapping("/logout")
    public String logout() {
        // Logout the current user
//        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}

package codingTechniques.contoller;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import codingTechniques.model.Bidding;
import codingTechniques.model.CropStatus;
import codingTechniques.model.DraftCrop;
import codingTechniques.repositories.BiddingRepository;
import codingTechniques.repositories.DraftCropRepository;
import codingTechniques.repositories.FinalCropRepository;
import codingTechniques.repositories.IssueRepository;
import codingTechniques.repositories.MarketOfficialRepository;
import codingTechniques.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import codingTechniques.model.FinalCrop;
import codingTechniques.model.Issue;
import codingTechniques.model.MarketOfficial;
import codingTechniques.model.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private BiddingRepository biddingRepository;
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
    @GetMapping("/marketOfficial/{marketOfficialId}/finalCropTransactions")
    public String viewFinalCropTransactions(@PathVariable("marketOfficialId") String marketOfficialId, Model model) {
        // Retrieve final crop transactions where buyerId is not null
        List<FinalCrop> finalCropTransactions = finalCropRepository.findByBuyerIdNotNull();
        model.addAttribute("finalCropTransactions", finalCropTransactions);
        return "marketOfficial/finalCropTransactions";
    }
    @GetMapping("/marketOfficial/{marketOfficialId}/raiseIssue/{transactionId}")
    public String showRaiseIssueForm(@PathVariable("marketOfficialId") String marketOfficialId, @PathVariable("transactionId") Long transactionId, Model model) {
        // Retrieve the transaction using the transactionId
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Create a new Issue object and set the necessary properties
        Issue issue = new Issue(); // Assuming Issue is your model class
        issue.setFarmer(transaction.getDraftCrop().getFarmer()); // Set the farmer from the draft crop

        // Add the Issue object to the model
        model.addAttribute("issue", issue);

        // Your code to fetch transaction details and prepare the model

        return "marketOfficial/raiseIssue"; // Assuming "raiseIssue" is the Thymeleaf template name
    }


//    @PostMapping("/marketOfficial/{marketOfficialId}/raiseIssue/{transactionId}")
//    public String raiseIssue1(@PathVariable("marketOfficialId") Long marketOfficialId, @PathVariable("transactionId") Long transactionId, @ModelAttribute("issue") Issue issue) {
//        // Your code to handle the POST request and save the issue
//        return "redirect:/marketOfficial/" + marketOfficialId + "/transactions"; // Assuming redirection after raising the issue
//    }

    @PostMapping("/marketOfficial/{marketOfficialId}/raiseIssue/{transactionId}")
    public String raiseIssue(@PathVariable("marketOfficialId") String marketOfficialId, @PathVariable("transactionId") Long transactionId, @ModelAttribute("issue") Issue issue) {
        // Retrieve the market official using the marketOfficialId
        MarketOfficial marketOfficial = marketOfficialRepository.findById(102L).orElse(null);
        if (marketOfficial == null) {
            // Handle the case where the market official is not found
            return "redirect:/error";
        }

        // Retrieve the transaction using the transactionId
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Set the market official, buyer, and final crops for the issue
//        issue.setMarketOfficerId(102);
        issue.setBuyer(transaction.getBuyer());
        issue.setFinalCropsId(transaction.getId());
        issue.setSender("market_officer"); // Assuming the market officer is the sender
issue.setFarmer(transaction.getDraftCrop().getFarmer());
issue.setTimestamp(LocalDateTime.now());
        // Save the issue to the database
        issueRepository.save(issue);

        // Redirect to the issue page
        return "redirect:/issue/" + "102" + "/" + issue.getFarmer().getId() + "/" + issue.getBuyer().getId() + "/" + transactionId;
    }

    @GetMapping("/marketOfficial/{marketOfficialId}/finalCropTransactionsWithNullBuyer")
    public String viewFinalCropTransactionsWithNullBuyer(@PathVariable("marketOfficialId") String marketOfficialId, Model model) {
        // Retrieve final crop transactions where buyerId is null
        List<FinalCrop> finalCropTransactions = finalCropRepository.findByBuyerIdIsNull();
        model.addAttribute("finalCropTransactions", finalCropTransactions);
        return "marketOfficial/finalCropTransactionsWithNullBuyer";
    }
    
    
    @PostMapping("/marketOfficial/endBid")
    @Transactional
    public String endBid(@RequestParam("finalCropId") Long finalCropId) {
        // Retrieve the maximum bid amount for the given finalCropId
        Optional<Bidding> maxBidOptional = biddingRepository.findFirstByFinalCropIdOrderByBidAmountDesc(finalCropId);
        if (maxBidOptional.isEmpty()) {
            // Handle case where there are no bids for the given finalCropId
            return "error";
        }
        Bidding maxBid = maxBidOptional.get();

        // Retrieve the buyerId from the maximum bid
        Long buyerId = maxBid.getBuyer().getId();

        // Update the FinalCrop entity with the buyerId
        FinalCrop finalCrop = finalCropRepository.findById(finalCropId).orElse(null);
        if (finalCrop == null) {
            // Handle case where FinalCrop with given finalCropId is not found
            return "error";
        }
        finalCrop.setBuyer(buyerId);
        finalCrop.setMaxPrice((int)(maxBid.getBidAmount()));

        // Save the updated FinalCrop entity
        finalCropRepository.save(finalCrop);

        // Redirect back to the market official dashboard
        return "redirect:/marketOfficial/" + "102" + "/dashboard";
    }
    
}

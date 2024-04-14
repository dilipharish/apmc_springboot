package codingTechniques.contoller;

import codingTechniques.model.Bidding;
import codingTechniques.model.Buyer;
import codingTechniques.model.FinalCrop;
import codingTechniques.model.Issue;
import codingTechniques.model.Review;
import codingTechniques.repositories.BiddingRepository;
import codingTechniques.repositories.BuyerRepository;
import codingTechniques.repositories.FinalCropRepository;
import codingTechniques.repositories.ReviewRepository;
import codingTechniques.repositories.IssueRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BuyerController {

    @Autowired
    private FinalCropRepository finalCropRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BiddingRepository bidRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private IssueRepository issueRepository;


    @GetMapping("/buyer/{buyerId}/dashboard")
    public String buyerDashboard(@PathVariable("buyerId") Long buyerId, Model model) {
        List<FinalCrop> finalCropsWithoutBuyer = finalCropRepository.findByBuyerIdIsNull();
        model.addAttribute("finalCrops", finalCropsWithoutBuyer);
        model.addAttribute("buyerId", buyerId);
        return "buyer/dashboard";
    }

    @GetMapping("/bidding/{finalCropId}/{buyerId}")
    public String joinBidding(@PathVariable("finalCropId") Long finalCropId, @PathVariable("buyerId") Long buyerId, Model model) {
        FinalCrop finalCrop = finalCropRepository.findById(finalCropId).orElse(null);
        if (finalCrop != null && finalCrop.getDraftCrop() != null) {
            // Fetch the current highest bid amount
            double currentMaxBid = getCurrentMaxBid(finalCropId);
            // Fetch all bidding details for the final crop
            List<Bidding> biddingDetails = bidRepository.findAllByFinalCropId(finalCropId);
            model.addAttribute("finalCrop", finalCrop);
            model.addAttribute("finalCropId", finalCropId);
            model.addAttribute("buyerId", buyerId);
            model.addAttribute("currentMaxBid", currentMaxBid);
            model.addAttribute("biddingDetails", biddingDetails);
            return "buyer/bidding";
        } else {
            // Final crop not found or draft crop not set
            return "error";}
        }
    
    @GetMapping("/buyer/{buyerId}/addReview")
    public String showAddReviewForm(Model model, @PathVariable("buyerId") Long buyerId) {
        // Retrieve the buyer using the buyerId
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (buyer == null) {
            // Handle the case where the buyer is not found
            return "redirect:/error";
        }

        // Add the buyer to the model
        model.addAttribute("buyer", buyer);
        model.addAttribute("review", new Review());

        return "buyer/addReview";
    }

    @PostMapping("/buyer/{buyerId}/addReview")
    public String addReview(@ModelAttribute("review") Review review, @PathVariable("buyerId") Long buyerId) {
        // Retrieve the buyer using the buyerId
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (buyer == null) {
            // Handle the case where the buyer is not found
            return "redirect:/error";
        }

        // Set the user for the review
        review.setUser(buyer.getUser()); // Assuming user_id for buyer is the same as the user_id for review
        
        // Save the review to the database
        reviewRepository.save(review);

        // Redirect back to the buyer dashboard
        return "redirect:/buyer/" + buyerId + "/dashboard";
    }


    
    @PostMapping("/bidding/{finalCropId}/{buyerId}")
    public String placeBid(@PathVariable("finalCropId") Long finalCropId, @PathVariable("buyerId") Long buyerId,
                           @RequestParam("bidAmount") double bidAmount, Model model) {
        FinalCrop finalCrop = finalCropRepository.findById(finalCropId).orElse(null);
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (finalCrop != null && buyer != null) {
            // Fetch the current highest bid amount
            double currentMaxBid = getCurrentMaxBid(finalCropId);
            // Fetch the number of existing bids for the final crop
            int numberOfBids = bidRepository.countBidsByFinalCropId(finalCropId);
            // Check if the new bid is higher than the current highest bid and if the number of bids is less than ten
            if (bidAmount > currentMaxBid && numberOfBids < 10) {
                // Save the new bid
                Bidding bid = new Bidding();
                bid.setFinalCrop(finalCrop);
                bid.setBuyer(buyer);
                bid.setBidAmount(bidAmount);
                bidRepository.save(bid);
                // Check if this is the tenth bid
                if (numberOfBids == 9) {
                    // Update the final crop with the buyer ID of the highest bidder
                    finalCrop.setBuyer(buyer);
                    // Save the maximum price
                    finalCrop.setMaxPrice((int) bidAmount);
                    // Save the bid start time
//                    finalCrop.setBidStartTime(LocalDateTime.now());
                    finalCropRepository.save(finalCrop);
                }
            }
        }
        // Redirect back to the same bidding page
        return "redirect:/bidding/{finalCropId}/{buyerId}";
    }


    private double getCurrentMaxBid(Long finalCropId) {
        // Fetch the current highest bid amount for the final crop
        Double maxBid = bidRepository.findMaxBidAmountByFinalCropId(finalCropId);
        return maxBid != null ? maxBid : 0;
    }
    
    @GetMapping("/buyer/{buyerId}/transactions")
    public String showTransactions(@PathVariable("buyerId") Long buyerId, Model model) {
        // Query final crops where buyerId matches
        List<FinalCrop> buyerTransactions = finalCropRepository.findByBuyerId(buyerId);
        model.addAttribute("transactions", buyerTransactions);
        return "buyer/transactions";
    }
    
    @GetMapping("/buyer/{buyerId}/raiseIssue/{transactionId}")
    public String showRaiseIssueForm(@PathVariable("buyerId") Long buyerId, @PathVariable("transactionId") Long transactionId, Model model) {
        // Fetch the transaction
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Add transaction details to the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("buyerId", buyerId);

        // Add an empty Issue object to the model
        model.addAttribute("issue", new Issue());

        return "buyer/raiseIssue";
    }

    @PostMapping("/buyer/{buyerId}/raiseIssue/{transactionId}")
    public String raiseIssue(@PathVariable("buyerId") Long buyerId, @PathVariable("transactionId") Long transactionId, @ModelAttribute("issue") Issue issue) {
        // Retrieve the buyer using the buyerId
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (buyer == null) {
            // Handle the case where the buyer is not found
            return "redirect:/error";
        }

        // Retrieve the transaction using the transactionId
        FinalCrop transaction = finalCropRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            // Handle the case where the transaction is not found
            return "redirect:/error";
        }

        // Set the buyer, farmer, and final crops for the issue
        issue.setBuyer(buyer);
        issue.setFarmer(transaction.getDraftCrop().getFarmer());
        issue.setFinalCropsId(transaction.getId());
        issue.setSender("buyer"); // Assuming the buyer is the sender

        // Save the issue to the database
        issueRepository.save(issue);

        // Redirect to the issue page
        return "redirect:/issue/" + "102" + "/" + issue.getFarmer().getId() + "/" + buyerId + "/" + transactionId;
    }



}

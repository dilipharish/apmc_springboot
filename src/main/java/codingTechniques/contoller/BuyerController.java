package codingTechniques.contoller;

import codingTechniques.model.Bidding;
import codingTechniques.model.Buyer;
import codingTechniques.model.FinalCrop;
import codingTechniques.repositories.BiddingRepository;
import codingTechniques.repositories.BuyerRepository;
import codingTechniques.repositories.FinalCropRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}

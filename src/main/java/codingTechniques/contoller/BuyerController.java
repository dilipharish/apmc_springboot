package codingTechniques.contoller;

import codingTechniques.model.Buyer;
import codingTechniques.repositories.BuyerRepository;
import codingTechniques.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/buyer/{buyerId}/dashboard")
    public String buyerDashboard(@PathVariable("buyerId") Long buyerId, Model model) {
        // Retrieve buyer details and necessary data
        Buyer buyer = buyerRepository.findById(buyerId).orElse(null);
        if (buyer == null) {
            // Handle the case where buyer is not found
            return "error";
        }
        // Add necessary attributes to the model
        model.addAttribute("buyer", buyer);
        // Return the buyer dashboard view
        return "buyer/dashboard";
    }
}

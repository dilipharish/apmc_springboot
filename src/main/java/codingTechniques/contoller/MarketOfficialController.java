package codingTechniques.contoller;

import codingTechniques.model.MarketOfficial;
import codingTechniques.repositories.MarketOfficialRepository;
import codingTechniques.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MarketOfficialController {

    @Autowired
    private MarketOfficialRepository marketOfficialRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/marketOfficial/{marketOfficialId}/dashboard")
    public String marketOfficialDashboard(@PathVariable("marketOfficialId") Long marketOfficialId, Model model) {
        // Retrieve market official details and necessary data
        MarketOfficial marketOfficial = marketOfficialRepository.findById(marketOfficialId).orElse(null);
        if (marketOfficial == null) {
            // Handle the case where market official is not found
            return "error";
        }
        // Add necessary attributes to the model
        model.addAttribute("marketOfficial", marketOfficial);
        // Return the market official dashboard view
        return "marketOfficial/dashboard";
    }
}

package codingTechniques.contoller;

import codingTechniques.model.Buyer;
import codingTechniques.model.Farmer;
import codingTechniques.model.MarketOfficial;
import codingTechniques.model.Role;
import codingTechniques.model.User;
import codingTechniques.repositories.BuyerRepository;
import codingTechniques.repositories.FarmerRepository;
import codingTechniques.repositories.MarketOfficialRepository;
import codingTechniques.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private MarketOfficialRepository marketOfficialRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    
    

    @GetMapping("/registration")
    public String getRegPage(@ModelAttribute("user") User user) {
        return "register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        User savedUser = userRepository.save(user);

        // Check the role of the registered user
        if (user.getRole().equals(Role.FARMER)) {
            Farmer farmer = new Farmer();
            farmer.setUser(savedUser);
            farmerRepository.save(farmer);
            return "redirect:/farmer/" + farmer.getId() + "/dashboard";
        } else if (user.getRole().equals(Role.MARKET_OFFICIAL)) {
            MarketOfficial marketOfficial = new MarketOfficial();
            marketOfficial.setUser(savedUser);
            marketOfficialRepository.save(marketOfficial);
            return "redirect:/marketOfficial/" + marketOfficial.getId() + "/dashboard";
        } else if (user.getRole().equals(Role.BUYER)) {
            Buyer buyer = new Buyer();
            buyer.setUser(savedUser);
            buyerRepository.save(buyer);
            return "redirect:/buyer/" + buyer.getId() + "/dashboard";
        } else {
            // Handle other roles accordingly
            return "redirect:/"; // Redirect to default page or handle error
        }
    }


    @GetMapping("/users")
    public String userPage(Model model) {
        List<User> listOfUsers = userRepository.findAll();
        model.addAttribute("user", listOfUsers);
        return "user";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

 // Inside UserController.java

    
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        // Perform authentication logic
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null) {
            // Check the role of the logged-in user
            if (user.getRole() == Role.FARMER) {
                // Retrieve the farmer associated with the user's email
                Farmer farmer = farmerRepository.findByUserEmail(email);
                if (farmer != null) {
                    // Redirect to farmer dashboard with the farmer's ID
                    return "redirect:/farmer/" + farmer.getId() + "/dashboard";
                } else {
                    // Handle the case where the farmer is not found
                    model.addAttribute("error", "Farmer details not found");
                    return "login";
                }
            } else if (user.getRole() == Role.MARKET_OFFICIAL) {
                // Redirect to market official dashboard
                MarketOfficial marketOfficial = marketOfficialRepository.findByUserEmail(email);
                if (marketOfficial != null) {
                    return "redirect:/marketOfficial/" + marketOfficial.getId() + "/dashboard";
                } else {
                    // Handle the case where the market official is not found
                    model.addAttribute("error", "Market official details not found");
                    return "login";
                }
            } else if (user.getRole() == Role.BUYER) {
                // Redirect to buyer dashboard
                Buyer buyer = buyerRepository.findByUserEmail(email);
                if (buyer != null) {
                    return "redirect:/buyer/" + buyer.getId() + "/dashboard";
                } else {
                    // Handle the case where the buyer is not found
                    model.addAttribute("error", "Buyer details not found");
                    return "login";
                }
            } else {
                // Redirect to user dashboard or another page
                return "redirect:/users";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }




}

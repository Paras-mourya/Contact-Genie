package smart.smart_contract.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import smart.smart_contract.Repository.UserRepository;
import smart.smart_contract.entity.User;



@Controller
public class FeedbackController {
    
    @Autowired
private UserRepository userRepository;


    @GetMapping("/user/feedback")
public String feedbackForm(Model model, Principal principal) {
    String userEmail = principal.getName(); // email is your username
    User user = userRepository.findByEmail(userEmail);
    model.addAttribute("user", user);
    return "Normal/feedback_form";
}

  @PostMapping("/user/submit-feedback")
public String handleFeedback(
        @RequestParam("subject") String subject,
        @RequestParam("message") String message,
        Principal principal,
        RedirectAttributes redirectAttributes) {

    String username = principal.getName();

    System.out.println("Feedback from: " + username);
    System.out.println("Subject: " + subject);
    System.out.println("Message: " + message);

    redirectAttributes.addFlashAttribute("successMessage", "Thank you for your feedback!");
    return "redirect:/user/feedback";  // ✅ Correct path for GET mapping
}

}

package smart.smart_contract.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Add kiya for error message
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import smart.smart_contract.Repository.UserRepository;
import smart.smart_contract.entity.User;
import smart.smart_contract.service.Emailservice;


@Controller
public class ForgotController {
    Random random = new Random(); // 👈 Seed hata diya (yahi fix tha)

    @Autowired
    private Emailservice emailService;

    @Autowired
    private UserRepository userRepository; // Add kiya

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @RequestMapping("/forgot")
    public String openEmailform() {
        return "forgot-email";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, Model m, HttpSession session) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            m.addAttribute("message", "Unable to send OTP. Please check your Email ID !");
            return "forgot-email";
        }

        int otp = 100000 + random.nextInt(900000); // 👈 OTP fix kiya (6 digit, unique)
        System.out.println("otp" + otp);
        String subject = "OTP From ContactGenie";
        String message = "<h1> OTP =" + otp + " </h1>";
        String to = email;

        boolean flag = this.emailService.sendEmail(subject, message, to);
        if (flag) {
            // Store in session instead of model
            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "verify-otp";
        } else {
            m.addAttribute("message", "Unable to send OTP. Please try again later.");
            return "forgot-email";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp, Model model, HttpSession session) {

        Integer myotp = (Integer) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");

        if (myotp != null && myotp.equals(otp)) {
            session.removeAttribute("message"); // agar pehle se koi galat msg ho toh hata de
            return "password-change";
        } else {
            session.setAttribute("message", "You have entered wrong OTP");
            return "verify-otp";
        }
    }



    
    @PostMapping("/change-password") 
    public String passwordchange(@RequestParam("newpassword") String newpassword,HttpSession session,Model m){

      String email= (String) session.getAttribute("email");
      User user=userRepository.findByEmail(email);

      if(user !=null){
        user.setPassword(this.bcrypt.encode(newpassword));
        userRepository.save(user);
        session.setAttribute("message","Password Successfully Changed");
        return "login";
      }
      else{
        return "passwod-change";
      }
    }
}

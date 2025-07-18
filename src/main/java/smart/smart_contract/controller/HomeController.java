package smart.smart_contract.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import smart.smart_contract.Repository.UserRepository;
import smart.smart_contract.entity.User;





@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository UserRepository;
    
    @GetMapping("/")
    public String home(Model m){
        m.addAttribute("title","Home- smartcontact");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m){
        m.addAttribute("title","About-smartcontact");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model m){
        m.addAttribute("title","smart-contact");
        m.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String register(@Valid @ModelAttribute("user") User user ,BindingResult resultt,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model){

         if (!agreement) {
        model.addAttribute("user", user);
        model.addAttribute("message", "You must agree to the terms and conditions.");
        return "signup";
    }

        if(resultt.hasErrors()){
            model.addAttribute("user",user);
            return "signup";
        }
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(agreement);
        System.out.println(user);

       User result =this.UserRepository.save(user);
       model.addAttribute("user",result);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String customLogin(Model model){
        model.addAttribute("title","login page");
        return "login";
    }


    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id){

        this.UserRepository.deleteById(id);
        return "redirect:/login";

    }
}

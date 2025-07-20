package smart.smart_contract.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import smart.smart_contract.Repository.ContactRepository;
import smart.smart_contract.Repository.UserRepository;
import smart.smart_contract.entity.Contact;
import smart.smart_contract.entity.User;
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

   @ModelAttribute
public void AddCommondata(Model model, Principal principal){
    if (principal != null) {
        String username = principal.getName();
        System.out.println("✅ Principal is not null: " + username);
        User user = userRepository.findByEmail(username);
        System.out.println("✅ User fetched: " + user.getName());
        model.addAttribute("user", user);
    } else {
        System.out.println("❌ Principal is null!");
    }
}

    
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        return "Normal/user_dashboard";
    }



   @GetMapping("/add-contact")
public String openAddContactform(Model model){
    model.addAttribute("title", "Add Contact");
    model.addAttribute("contact", new Contact());
    return "Normal/add_contactform";
}

@PostMapping("/process-contact")
public String processContact(@ModelAttribute Contact contact,
                             @RequestParam("profileImage") MultipartFile file,
                             Principal principal, HttpSession session){
   try {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        contact.setUser(user);

        // Handle file
        if (file.isEmpty()) {
            System.out.println("file is empty");
            contact.setImage("default.png");
        } else {
            System.out.println("Uploaded file name: " + file.getOriginalFilename());

            // ✅ uploads folder outside jar
            String uploadDir = new File("uploads").getAbsolutePath();
            new File(uploadDir).mkdirs(); // 👈 auto-create folder

            Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            contact.setImage(file.getOriginalFilename());
            System.out.println("image uploaded to uploads/");
        }

        user.getContacts().add(contact);
        this.userRepository.save(user);
        System.out.println(contact);

   } catch (Exception e) {
       System.out.println(e.getMessage());
   }
    return "redirect:/user/showContacts/0";
}


// ✅ Add this GET mapping to handle accidental GET requests
@GetMapping("/process-contact")
public String fallbackProcessContact() {
    return "redirect:/user/add-contact";
}

@GetMapping("/showContacts/{page}")
public String showContacts(@PathVariable("page") Integer page, Model m,Principal principal){
    m.addAttribute("title","Show user contacts");

   String username= principal.getName();
  User user=this.userRepository.findByEmail(username);
  PageRequest pageable =PageRequest.of(page,5);

  Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);

 m.addAttribute("contacts",contacts);
 m.addAttribute("currentPage",page);
 m.addAttribute("totalPages",contacts.getTotalPages());
    return "Normal/show_contacts";
}

@GetMapping("/{cId}/contact-detail")
public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal){
   
   Optional<Contact> contactOptional= this.contactRepository.findById(cId);
   Contact contact= contactOptional.get();

   String userName = principal.getName();
  User user=this.userRepository.findByEmail(userName);

  if(user.getId() == contact.getUser().getId()){
     model.addAttribute("contact",contact);
     model.addAttribute("title",contact.getFullName());
  }
  
    return "Normal/contact_detail";
}

@GetMapping("/delete/{cId}")
public String deleteContact(@PathVariable("cId") Integer cId,Principal principal){
    Contact contact= this.contactRepository.findById(cId).get();

   // contact.setUser(null);

   User user = this.userRepository.findByEmail(principal.getName());
   user.getContacts().remove(contact);

   this.userRepository.save(user);
    
   

    return "redirect:/user/showContacts/0";
}

@PostMapping("/update-contact/{cId}")
public String updateForm(@PathVariable("cId") Integer cId ,Model m){

   Contact contact= this.contactRepository.findById(cId).get();
   m.addAttribute("contact",contact);

    m.addAttribute("title","update Contact");
    return "Normal/update_form";
}

@PostMapping("/process-update")
public String updateHandler(@ModelAttribute Contact contact,
                            @RequestParam("profileImage") MultipartFile file,
                            Principal principal) {
    try {
        Contact oldcontactDetail = this.contactRepository.findById(contact.getcId()).get();

        if (!file.isEmpty()) {
            // ✅ Delete old image
            String uploadDir = new File("uploads").getAbsolutePath();
            File oldFile = new File(uploadDir, oldcontactDetail.getImage());
            if (oldFile.exists()) oldFile.delete();

            // ✅ Save new image
            new File(uploadDir).mkdirs();
            Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            contact.setImage(file.getOriginalFilename());
        } else {
            contact.setImage(oldcontactDetail.getImage());
        }

        String userName = principal.getName();
        User user = this.userRepository.findByEmail(userName);
        contact.setUser(user);

        this.contactRepository.save(contact);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return "redirect:/user/" + contact.getcId() + "/contact-detail";
}

@GetMapping("/yourProfile")
public String yourProfile(Model m, Principal principal){
    String userName = principal.getName();
    User user = userRepository.findByEmail(userName);

    System.out.println("==== Debug User ====");
    System.out.println(user);
    System.out.println("====================");

    if (user.getImageUrl() == null || user.getImageUrl().trim().isEmpty()) {
        user.setImageUrl("default.png");
    }

    m.addAttribute("title", "YourProfile");
    m.addAttribute("user", user);

    return "Normal/yourProfile";
}


}

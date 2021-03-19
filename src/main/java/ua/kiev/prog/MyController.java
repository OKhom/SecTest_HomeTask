package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import java.util.*;

@Controller
public class MyController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/")
    public String index(Model model){
        User user = getCurrentUser();

        String login = user.getUsername();
        CustomUser dbUser = userService.findByLogin(login);

        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin", isAdmin(user));
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());
        model.addAttribute("age", dbUser.getAge());

        return "index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) Integer age) {
        User user = getCurrentUser();

        String login = user.getUsername();
        userService.updateUser(login, email, phone, age);

        return "redirect:/";
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String update(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) Integer age,
                         Model model) throws MessagingException {
        if (email.equals("") || email == null) {
            model.addAttribute("error", "noemail");
            model.addAttribute("login", login);
            return "register";
        }

        String passHash = passwordEncoder.encode(password);
        String code = String.valueOf(new Random().nextInt(89999998) + 10000001);

        if ( ! userService.addUser(login, passHash, UserRole.PREACT, email, phone, age, code)) {
            model.addAttribute("error", "exist");
            model.addAttribute("login", login);
            return "register";
        }
        emailService.sendHtmlEmail(email, "activate account",
                "http://localhost:8080/activate?login=" + login + "&code=" + code);

        return "redirect:/";
    }

    @PostMapping(value = "/activated")
    public String activateUser(@RequestParam String login,
                               @RequestParam String code,
                               Model model) {
        CustomUser dbUser = userService.findByLogin(login);

        try {
            if (!dbUser.getCode().equals(code)) {
                model.addAttribute("activation", false);
                model.addAttribute("login", login);
                model.addAttribute("code", code);
                return "activate";
            }
        } catch (NullPointerException npe) {
            return "login";
        }
        userService.activateUser(login);
        return "login";
    }

    @GetMapping("/activate")
    public String activatePage(@RequestParam(required = false) String login,
                               @RequestParam(required = false) String code,
                               Model model) {
        model.addAttribute("login", login);
        model.addAttribute("code", code);
        return "activate";
    }

    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateRole(@RequestParam(name = "ids[]", required = false) List<Long> ids,
                             @RequestParam(name = "newRole") UserRole newRole,
                             Model model) {
        userService.updateUserById(ids, newRole);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')") // !!!
    public String admin(Model model) {
        User user = getCurrentUser();
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("role", user.getAuthorities());
        List<UserRole> roles = new ArrayList<>();
        for (UserRole ur : UserRole.values()) {
            if (!(ur.toString().equals("ROLE_ADMIN") || ur.toString().equals("ROLE_PREACT"))) roles.add(ur);
        }
        model.addAttribute("roles", roles);
        return "admin";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(Model model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    // ----

    private User getCurrentUser() {
        return (User)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()) || "ROLE_MODERATOR".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }
}

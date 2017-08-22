package eu.t6nn.demo.codecomp.controller;

import eu.t6nn.demo.codecomp.model.GameSession;
import eu.t6nn.demo.codecomp.model.Player;
import eu.t6nn.demo.codecomp.service.GameSessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private GameSessions sessions;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if(!model.containsAttribute("player")) {
            model.addAttribute("player", new Player());
        }
        return "registrationForm";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute Player player, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            GameSession session = sessions.register(player);
            return new ModelAndView("redirect:/play/" + session.getId());
        } else {
            return new ModelAndView("registrationForm", "player", player);
        }
    }

    private boolean isValid(Player player) {
        return player != null && !StringUtils.isEmpty(player.getName()) && player.getLang() != null;
    }
}

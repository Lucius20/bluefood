package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class LogInController {

	@GetMapping(path = {"/login", "/"})
	public String logIn() {
		return "login";
	}
	
	@GetMapping(path = "/login-error")
	public String logInError(Model model) {
		model.addAttribute("msg", "Credenciais inválidas.");
		return "login";
	}
}

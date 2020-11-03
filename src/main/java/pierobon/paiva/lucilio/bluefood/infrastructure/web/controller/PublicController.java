package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pierobon.paiva.lucilio.bluefood.application.service.ClientService;
import pierobon.paiva.lucilio.bluefood.application.service.RestaurantService;
import pierobon.paiva.lucilio.bluefood.application.service.ValidationException;
import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategoryRepository;

@Controller
@RequestMapping(path = "/public")
public class PublicController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private RestaurantCategoryRepository restaurantCategoryRepository;
	
	@GetMapping("/client/new")
	public String newClient(Model model) {
		
		model.addAttribute("client", new Client());
		ControllerHelper.setEditMode(model, false);
		
		return "client-signup";
	}
	
	@GetMapping("/restaurant/new")
	public String newRestaurant(Model model) {
		
		model.addAttribute("restaurant", new Restaurant());
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addCategoriesToRequest(restaurantCategoryRepository, model);
		
		return "restaurant-signup";
	}
	
	@PostMapping(path = "/client/save")
	public String saveClient(@ModelAttribute("client") @Valid Client client, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			try {
				clientService.saveClient(client);
				model.addAttribute("msg", "Cliente gravado com sucesso!");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditMode(model, false);
		
		return "client-signup";
	}
	
	@PostMapping(path = "/restaurant/save")
	public String saveRestaurant(@ModelAttribute("restaurant") @Valid Restaurant restaurant, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			try {
				restaurantService.saveRestaurant(restaurant);
				model.addAttribute("msg", "Restaurante gravado com sucesso!");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditMode(model, false);
		ControllerHelper.addCategoriesToRequest(restaurantCategoryRepository, model);
		
		return "restaurant-signup";
	}

}

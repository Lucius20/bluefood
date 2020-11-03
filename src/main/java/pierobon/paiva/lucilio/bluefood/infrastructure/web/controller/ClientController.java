package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pierobon.paiva.lucilio.bluefood.application.service.ClientService;
import pierobon.paiva.lucilio.bluefood.application.service.RestaurantService;
import pierobon.paiva.lucilio.bluefood.application.service.ValidationException;
import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategory;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategoryRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.SearchFilter;
import pierobon.paiva.lucilio.bluefood.util.CurrencyUtils;
import pierobon.paiva.lucilio.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/client")
public class ClientController {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private RestaurantCategoryRepository restaurantCategoryRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	private CurrencyUtils currencyUtils = new CurrencyUtils();
	
	@GetMapping(path = "/home")
	public String home(Model model) {
		
		List<RestaurantCategory> categories = restaurantCategoryRepository.findAll(Sort.by("name"));
		model.addAttribute("categories", categories);
		
		model.addAttribute("searchFilter", new SearchFilter());
		
		List<Order> orders = orderRepository.listOrdersFromClient(SecurityUtils.loggedClient().getId());
		model.addAttribute("orders", orders);
		
		return "client-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {
		
		Integer clientId = SecurityUtils.loggedClient().getId();
		
		Client client = clientRepository.findById(clientId).orElseThrow();
		model.addAttribute("client", client);
		
		ControllerHelper.setEditMode(model, true);
		
		return "client-signup";
	}
	
	@GetMapping(path = "/edit/delete")
	public String delete() {
		
		return "client-delete-acc";
	}
	
	@GetMapping(path = "/edit/delete/confirm")
	public String confirmDeletion() {
		
		Client client = SecurityUtils.loggedClient();
		clientRepository.delete(client);
		
		return "redirect:/logout";
	}
	
	@PostMapping(path = "/save")
	public String save(@ModelAttribute("client") @Valid Client client, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			try {
				clientService.saveClient(client);
				model.addAttribute("msg", "Cliente gravado com sucesso!");
			
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditMode(model, true);
		
		return "client-signup";
	}
	
	@GetMapping(path = "/search")
	public String search(
			@ModelAttribute("searchFilter") SearchFilter filter,
			@RequestParam(value = "cmd", required = false) String command,
			Model model) {
		
		filter.processFilter(command);
		
		List<Restaurant> restaurants = restaurantService.search(filter);
		model.addAttribute("restaurants", restaurants);
		
		ControllerHelper.addCategoriesToRequest(restaurantCategoryRepository, model);
		
		model.addAttribute("searchFilter", filter);
		
		model.addAttribute("cep", SecurityUtils.loggedClient().getCep());
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-search";
	}
	
	@GetMapping(path = "/restaurant")
	public String viewRestaurant(
			@RequestParam("restaurantId") Integer restaurantId,
			@RequestParam(value = "category", required = false) String category,
			Model model) {
		
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
		model.addAttribute("restaurant", restaurant);
		
		model.addAttribute("cep", SecurityUtils.loggedClient().getCep());
		
		List<String> categories = menuItemRepository.findCategories(restaurantId);
		model.addAttribute("categories", categories);
		
		List<MenuItem> menuItemsHighlighted;
		List<MenuItem> menuItemsNotHighlighted;
		
		if (category == null || category.trim().isBlank()) {
			menuItemsHighlighted = menuItemRepository.findByRestaurant_IdAndHighlightOrderByName(restaurantId, true);
			menuItemsNotHighlighted = menuItemRepository.findByRestaurant_IdAndHighlightOrderByName(restaurantId, false);
		
		} else {
			menuItemsHighlighted = menuItemRepository.findByRestaurant_IdAndHighlightAndCategoryOrderByName(restaurantId, true, category);
			menuItemsNotHighlighted = menuItemRepository.findByRestaurant_IdAndHighlightAndCategoryOrderByName(restaurantId, false, category);
			
		}
		
		model.addAttribute("menuItemsHighlighted", menuItemsHighlighted);
		model.addAttribute("menuItemsNotHighlighted", menuItemsNotHighlighted);
		model.addAttribute("selectedCategory", category);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-restaurant";
	}
}

package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pierobon.paiva.lucilio.bluefood.domain.order.Cart;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItem;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.RestaurantDoesNotMatchException;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItemRepository;
import pierobon.paiva.lucilio.bluefood.util.CurrencyUtils;

@Controller
@RequestMapping("/client/cart")
@SessionAttributes("cart")
public class CartController {
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	private CurrencyUtils currencyUtils = new CurrencyUtils();
	
	@ModelAttribute("cart")
	public Cart cart() {
		return new Cart();
	}
	
	@GetMapping(path = "/view")
	public String viewCart(Model model) {
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-cart";
	}
	
	@GetMapping(path = "/add")
	public String addItem(
			@RequestParam("itemId") Integer itemId,
			@RequestParam("amount") Integer amount,
			@RequestParam("note") String note,
			@ModelAttribute("cart") Cart cart,
			Model model) {
		
		MenuItem menuItem =  menuItemRepository.findById(itemId).orElseThrow();
		
		try {
			cart.addItem(menuItem, amount, note);
		} catch (RestaurantDoesNotMatchException e) {
			model.addAttribute("msg", "Não é possível misturar comidas de restaurantes diferentes.");
		}
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-cart";
	}
	
	@GetMapping(path = "/remove")
	public String removeItem(
			@RequestParam("itemId") Integer itemId,
			@ModelAttribute("cart") Cart cart,
			SessionStatus sessionStatus,
			Model model) {
		
		MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow();
		
		cart.removeItem(menuItem);
		
		if (cart.empty()) {
			sessionStatus.setComplete();
		}
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-cart";
	}
	
	@GetMapping(path = "/clear")
	public String clear(
			@ModelAttribute("cart") Cart cart,
			SessionStatus sessionStatus,
			Model model) {
		
		cart.clear();
		sessionStatus.setComplete();
		
		return "client-cart";
	}
	
	@GetMapping(path = "/redoCart")
	public String redoCart(
			@RequestParam("orderId") Integer orderId,
			@ModelAttribute("cart") Cart cart,
			Model model) {
		
		Order order = orderRepository.findById(orderId).orElseThrow();
		cart.clear();
		
		for (OrderItem orderItem : order.getItems()) {
			cart.addItem(orderItem);
		}
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-cart";
	}

}

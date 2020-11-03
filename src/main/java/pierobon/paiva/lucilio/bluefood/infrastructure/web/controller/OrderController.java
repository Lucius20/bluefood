package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.util.CurrencyUtils;

@Controller
@RequestMapping("/client/order")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	private CurrencyUtils currencyUtils = new CurrencyUtils();
	
	@GetMapping(path = "/view")
	public String viewOrder(
			@RequestParam("orderId") Integer orderId,
			Model model) {
		
		Order order = orderRepository.findById(orderId).orElseThrow();
		model.addAttribute("order", order);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "client-order";
	}

}

package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pierobon.paiva.lucilio.bluefood.application.service.OrderService;
import pierobon.paiva.lucilio.bluefood.application.service.PaymentException;
import pierobon.paiva.lucilio.bluefood.domain.order.Cart;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;

@Controller
@RequestMapping("/client/payment")
@SessionAttributes("cart")
public class PaymentController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping(path = "/pay")
	public String pay(
			@RequestParam("cardNumber") String cardNumber,
			@ModelAttribute("cart") Cart cart,
			SessionStatus sessionStatus,
			Model model) {
		
		Order order;
		
		try {
			order = orderService.createAndPay(cart, cardNumber);
			sessionStatus.setComplete();
			return "redirect:/client/order/view?orderId=" + order.getId();	
		
		} catch (PaymentException e) {
			model.addAttribute("msg", e.getMessage());
			return "client-cart";
		}
	}

}

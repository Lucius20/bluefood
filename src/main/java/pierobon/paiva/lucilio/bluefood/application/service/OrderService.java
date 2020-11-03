package pierobon.paiva.lucilio.bluefood.application.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import pierobon.paiva.lucilio.bluefood.domain.order.Cart;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.Order.Status;
import pierobon.paiva.lucilio.bluefood.domain.payment.CardData;
import pierobon.paiva.lucilio.bluefood.domain.payment.Payment;
import pierobon.paiva.lucilio.bluefood.domain.payment.PaymentRepository;
import pierobon.paiva.lucilio.bluefood.domain.payment.PaymentStatus;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItem;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItemPK;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.util.SecurityUtils;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Value("${bluefood.sbpay.url}")
	private String sbPayUrl;
	
	@Value("${bluefood.sbpay.token}")
	private String sbPayToken;
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = PaymentException.class)
	public Order createAndPay(Cart cart, String cardNumber) throws PaymentException {

		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setClient(SecurityUtils.loggedClient());
		order.setRestaurant(cart.getRestaurant());
		order.setStatus(Status.PRODUCTION);
		order.setDeliveryFee(cart.getRestaurant().getDeliveryFee());
		order.setSubtotal(cart.getTotalPrice(false));
		order.setTotal(cart.getTotalPrice(true));
		
		order = orderRepository.save(order);
		
		int sortOrder = 1;
		
		for (OrderItem orderItem : cart.getItems()) {
			orderItem.setId(new OrderItemPK(order, sortOrder++));
			orderItemRepository.save(orderItem);
		}
		
		CardData cardData = new CardData();
		cardData.setCardNumber(cardNumber);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Token", sbPayToken);
		
		HttpEntity<CardData> requestEntity = new HttpEntity<>(cardData, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		Map<String, String> response;
		
		try {
			response = restTemplate.postForObject(sbPayUrl, requestEntity, Map.class);
			
		} catch (Exception e) {
			throw new PaymentException("Erro no servidor de pagamento.");
		}
		
		PaymentStatus paymentStatus = PaymentStatus.valueOf(response.get("status"));
		
		if (paymentStatus != PaymentStatus.AUTHORIZED) {
			throw new PaymentException(paymentStatus.getDescription());
		}
		
		Payment payment = new Payment();
		payment.setDate(LocalDateTime.now());
		payment.setOrder(order);
		payment.setNumberAndLabel(cardNumber);
		paymentRepository.save(payment);
		
		
		return order;
	}

}

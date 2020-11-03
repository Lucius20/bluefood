package pierobon.paiva.lucilio.bluefood.application.test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItem;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItemPK;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.domain.payment.CardLabel;
import pierobon.paiva.lucilio.bluefood.domain.payment.Payment;
import pierobon.paiva.lucilio.bluefood.domain.payment.PaymentRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategory;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategoryRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;
import pierobon.paiva.lucilio.bluefood.util.StringUtils;

@SessionAttributes("testData")
@Component
public class InsertDataForTesting {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private RestaurantCategoryRepository restaurantCategoryRepository;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Environment environment = event.getApplicationContext().getEnvironment();
		
		if (environment.acceptsProfiles(Profiles.of("development"))) {
			clients();
			Restaurant[] restaurants = restaurants();
			menuItems(restaurants);
			orders();
		}
		
	}
	
	private Restaurant[] restaurants() {
		List<Restaurant> restaurants = new ArrayList<>(); 
		
		RestaurantCategory categoryPizza = restaurantCategoryRepository.findById(1).orElseThrow(NoSuchElementException::new);
		RestaurantCategory categorySandwich = restaurantCategoryRepository.findById(2).orElseThrow(NoSuchElementException::new);
		RestaurantCategory categoryDessert = restaurantCategoryRepository.findById(5).orElseThrow(NoSuchElementException::new);
		RestaurantCategory categoryJapanese = restaurantCategoryRepository.findById(6).orElseThrow(NoSuchElementException::new);
		
		Restaurant r = new Restaurant();
		r.setName("Bubger Kirg");
		r.setEmail("r1@bluefood.com.br");
		r.setPassword(StringUtils.encrypt("r"));
		r.setCnpj("00000000000101");
		r.setDeliveryFee(BigDecimal.valueOf(3.2));
		r.setPhoneNumber("99876671010");
		r.getCategories().add(categorySandwich);
		r.getCategories().add(categoryDessert);
		r.setLogo("0001-logo.png");
		r.setDeliveryBaseTime(30);
		restaurantRepository.save(r);
		restaurants.add(r);
		
		r = new Restaurant();
		r.setName("Mc Naldo's");
		r.setEmail("r2@bluefood.com.br");
		r.setPassword(StringUtils.encrypt("r"));
		r.setCnpj("00000000000102");
		r.setDeliveryFee(BigDecimal.valueOf(4.5));
		r.setPhoneNumber("99876671011");
		r.getCategories().add(categorySandwich);
		r.getCategories().add(categoryDessert);
		r.setLogo("0002-logo.png");
		r.setDeliveryBaseTime(25);
		restaurantRepository.save(r);
		restaurants.add(r);
		
		r = new Restaurant();
		r.setName("Sbubby");
		r.setEmail("r3@bluefood.com.br");
		r.setPassword(StringUtils.encrypt("r"));
		r.setCnpj("00000000000103");
		r.setDeliveryFee(BigDecimal.valueOf(12.2));
		r.setPhoneNumber("99876671012");
		r.getCategories().add(categorySandwich);
		r.getCategories().add(categoryDessert);
		r.setLogo("0003-logo.png");
		r.setDeliveryBaseTime(38);
		restaurantRepository.save(r);
		restaurants.add(r);
		
		r = new Restaurant();
		r.setName("Pizza Brut");
		r.setEmail("r4@bluefood.com.br");
		r.setPassword(StringUtils.encrypt("r"));
		r.setCnpj("00000000000104");
		r.setDeliveryFee(BigDecimal.valueOf(9.8));
		r.setPhoneNumber("99876671013");
		r.getCategories().add(categoryPizza);
		r.getCategories().add(categoryDessert);
		r.setLogo("0004-logo.png");
		r.setDeliveryBaseTime(22);
		restaurantRepository.save(r);
		restaurants.add(r);
		
		r = new Restaurant();
		r.setName("Wiki Japa");
		r.setEmail("r5@bluefood.com.br");
		r.setPassword(StringUtils.encrypt("r"));
		r.setCnpj("00000000000105");
		r.setDeliveryFee(BigDecimal.valueOf(14.9));
		r.setPhoneNumber("99876671014");
		r.getCategories().add(categoryJapanese);
		r.getCategories().add(categoryDessert);
		r.setLogo("0005-logo.png");
		r.setDeliveryBaseTime(19);
		restaurantRepository.save(r);
		restaurants.add(r);
		
		Restaurant[] array = new Restaurant[restaurants.size()]; 
		return restaurants.toArray(array);
	}
	
	private Client[] clients() {
		List<Client> clients = new ArrayList<>(); 
		
		Client c = new Client();
		c.setName("João Silva");
		c.setEmail("joao@bluefood.com.br");
		c.setPassword(StringUtils.encrypt("c"));
		c.setCep("89300100");
		c.setCpf("03099887666");
		c.setPhoneNumber("99355430001");
		clients.add(c);
		clientRepository.save(c);
		
		c = new Client();
		c.setName("Maria Torres");
		c.setEmail("maria@bluefood.com.br");
		c.setPassword(StringUtils.encrypt("c"));
		c.setCep("89300101");
		c.setCpf("03099887677");
		c.setPhoneNumber("99355430002");
		clients.add(c);
		clientRepository.save(c);
		
		Client[] array = new Client[clients.size()]; 
		return clients.toArray(array);
	}
	
	private void menuItems(Restaurant[] restaurants) {
		MenuItem mi = new MenuItem();
		mi.setCategory("Sanduíche");
		mi.setDescription("Delicioso sanduíche com molho.");
		mi.setName("Double Cheese Burger Especial");
		mi.setPrice(BigDecimal.valueOf(23.8));
		mi.setRestaurant(restaurants[0]);
		mi.setHighlight(true);
		mi.setImage("0001-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Sanduíche");
		mi.setDescription("Sanduíche padrão que mata a fome.");
		mi.setName("Cheese Burger Simples");
		mi.setPrice(BigDecimal.valueOf(17.8));
		mi.setRestaurant(restaurants[0]);
		mi.setHighlight(false);
		mi.setImage("0006-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Sanduíche");
		mi.setDescription("Sanduíche natural com peito de peru.");
		mi.setName("Sanduíche Natural da Casa");
		mi.setPrice(BigDecimal.valueOf(11.8));
		mi.setRestaurant(restaurants[0]);
		mi.setHighlight(false);
		mi.setImage("0007-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Bebida");
		mi.setDescription("Refrigerante com gás.");
		mi.setName("Refrigerante Tradicional");
		mi.setPrice(BigDecimal.valueOf(9));
		mi.setRestaurant(restaurants[0]);
		mi.setHighlight(false);
		mi.setImage("0004-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Bebida");
		mi.setDescription("Suco natural e docinho.");
		mi.setName("Suco de Laranja");
		mi.setPrice(BigDecimal.valueOf(9));
		mi.setRestaurant(restaurants[0]);
		mi.setHighlight(false);
		mi.setImage("0005-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Pizza");
		mi.setDescription("Pizza saborosa com cebola.");
		mi.setName("Pizza de Calabresa");
		mi.setPrice(BigDecimal.valueOf(38.9));
		mi.setRestaurant(restaurants[3]);
		mi.setHighlight(false);
		mi.setImage("0002-food.png");
		menuItemRepository.save(mi);
		
		mi = new MenuItem();
		mi.setCategory("Japonesa");
		mi.setDescription("Delicioso Uramaki tradicional.");
		mi.setName("Uramaki");
		mi.setPrice(BigDecimal.valueOf(16.8));
		mi.setRestaurant(restaurants[4]);
		mi.setHighlight(false);
		mi.setImage("0003-food.png");
		menuItemRepository.save(mi);
	}
	
	private void orders() {
		Order o = new Order();
		MenuItem mi1 = menuItemRepository.findById(1).orElseThrow();
		MenuItem mi2 = menuItemRepository.findById(2).orElseThrow();
		MenuItem mi3 = menuItemRepository.findById(3).orElseThrow();
		OrderItem oi = new OrderItem();
		Payment p = new Payment();
		
		o.setId(1);
		o.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), 3, 23, 8, 25, 30));
		o.setStatus(Order.Status.PRODUCTION);
		o.setClient(clientRepository.findById(1).orElseThrow());
		o.setRestaurant(restaurantRepository.findById(1).orElseThrow());
		o.setSubtotal(BigDecimal.valueOf(71.40));
		o.setDeliveryFee(BigDecimal.valueOf(3.20));
		o.setTotal(BigDecimal.valueOf(74.60));
		
		oi.setId(new OrderItemPK(o, 1));
		oi.setMenuItem(mi1);
		oi.setAmount(3);
		oi.setPrice(BigDecimal.valueOf(23.80));
		
		p.setId(1);
		p.setOrder(o);
		p.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), 3, 23, 8, 25, 30));
		p.setCardLastNumbers("1234");
		p.setCardLabel(CardLabel.ELO);
		
		orderRepository.save(o);
		orderItemRepository.save(oi);
		paymentRepository.save(p);
		
		//o.setItems(Set.of(oi));
		orderRepository.save(o);
		
		
		
		
		o.setId(2);
		o.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1, 10, 25, 30));
		o.setStatus(Order.Status.PRODUCTION);
		o.setClient(clientRepository.findById(1).orElseThrow());
		o.setRestaurant(restaurantRepository.findById(1).orElseThrow());
		o.setSubtotal(BigDecimal.valueOf(35.60));
		o.setDeliveryFee(BigDecimal.valueOf(3.20));
		o.setTotal(BigDecimal.valueOf(38.80));
		
		oi.setId(new OrderItemPK(o, 1));
		oi.setMenuItem(mi2);
		oi.setAmount(2);
		oi.setPrice(BigDecimal.valueOf(17.80));
		
		p.setId(2);
		p.setOrder(o);
		p.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1, 10, 25, 30));
		p.setCardLastNumbers("1234");
		p.setCardLabel(CardLabel.ELO);
		
		orderRepository.save(o);
		orderItemRepository.save(oi);
		paymentRepository.save(p);
		
		//o.setItems(Set.of(oi));
		orderRepository.save(o);
		
		
		
		
		o.setId(3);
		o.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), 12, 25, 30));
		o.setStatus(Order.Status.PRODUCTION);
		o.setClient(clientRepository.findById(1).orElseThrow());
		o.setRestaurant(restaurantRepository.findById(1).orElseThrow());
		o.setSubtotal(BigDecimal.valueOf(11.80));
		o.setDeliveryFee(BigDecimal.valueOf(3.20));
		o.setTotal(BigDecimal.valueOf(15));
		
		oi.setId(new OrderItemPK(o, 1));
		oi.setMenuItem(mi3);
		oi.setAmount(1);
		oi.setPrice(BigDecimal.valueOf(11.80));
		
		p.setId(3);
		p.setOrder(o);
		p.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), 12, 25, 30));
		p.setCardLastNumbers("1234");
		p.setCardLabel(CardLabel.ELO);
		
		orderRepository.save(o);
		orderItemRepository.save(oi);
		paymentRepository.save(p);
		
		//o.setItems(Set.of(oi));
		orderRepository.save(o);

	}

}

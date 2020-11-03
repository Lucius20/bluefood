package pierobon.paiva.lucilio.bluefood.infrastructure.web.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pierobon.paiva.lucilio.bluefood.application.service.ReportService;
import pierobon.paiva.lucilio.bluefood.application.service.RestaurantService;
import pierobon.paiva.lucilio.bluefood.application.service.RevenuesService;
import pierobon.paiva.lucilio.bluefood.application.service.ValidationException;
import pierobon.paiva.lucilio.bluefood.domain.order.ItemBillingReport;
import pierobon.paiva.lucilio.bluefood.domain.order.ItemReportFilter;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderReportFilter;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.Revenues;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategoryRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;
import pierobon.paiva.lucilio.bluefood.util.CurrencyUtils;
import pierobon.paiva.lucilio.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private RestaurantCategoryRepository restaurantCategoryRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RevenuesService revenuesService;
	
	private CurrencyUtils currencyUtils = new CurrencyUtils();
	
	@GetMapping(path = "/home")
	public String home(Model model) {
		
		Restaurant restaurant = SecurityUtils.loggedRestaurant();
		model.addAttribute("restaurant", restaurant);
		
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		List<Order> orders = orderRepository.findByRestaurant_IdAndNotCompleteOrderByDateDesc(restaurantId);
		
		model.addAttribute("orders", orders);
		
		LocalDateTime timeNow = LocalDateTime.now();
		LocalDateTime startDay = LocalDateTime.of(timeNow.getYear(), timeNow.getMonthValue(), timeNow.getDayOfMonth(), 0, 0, 1);
		LocalDateTime endDay = LocalDateTime.of(timeNow.getYear(), timeNow.getMonthValue(), timeNow.getDayOfMonth(), 23, 59, 59);
		LocalDateTime startMonth = LocalDateTime.of(timeNow.getYear(), timeNow.getMonthValue(), 1, 00, 00, 01);
		LocalDateTime endMonth = LocalDateTime.of(timeNow.getYear(), timeNow.getMonthValue(), 31, 23, 59, 59);
		LocalDateTime startYear = LocalDateTime.of(timeNow.getYear(), 1, 1, 00, 00, 01);
		LocalDateTime endYear = LocalDateTime.of(timeNow.getYear(), 12, 31, 23, 59, 59);
		
		Revenues revenues = revenuesService.calculateRevenues(startDay, endDay, startMonth, endMonth, startYear, endYear);
		model.addAttribute("revenues", revenues);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-home";
	}
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {
		
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
		model.addAttribute("restaurant", restaurant);
		
		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addCategoriesToRequest(restaurantCategoryRepository, model);
		
		return "restaurant-signup";
	}
	
	@GetMapping(path = "/edit/delete")
	public String delete() {
		
		return "restaurant-delete-acc";
	}
	
	@GetMapping(path = "/edit/delete/confirm")
	public String confirmDeletion() {
		
		Restaurant restaurant = SecurityUtils.loggedRestaurant();
		menuItemRepository.deleteAll(menuItemRepository.findByRestaurant_IdOrderByName(SecurityUtils.loggedRestaurant().getId()));
		restaurantRepository.delete(restaurant);
		
		return "redirect:/logout";
	}
	
	@PostMapping(path = "/save")
	public String save(@ModelAttribute("restaurant") @Valid Restaurant restaurant, Errors errors, Model model) {
		
		if (!errors.hasErrors()) {
			try {
				restaurantService.saveRestaurant(restaurant);
				model.addAttribute("msg", "Restaurante gravado com sucesso!");
			
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditMode(model, true);
		ControllerHelper.addCategoriesToRequest(restaurantCategoryRepository, model);
		
		return "restaurant-signup";
	}
	
	@GetMapping(path = "/foods")
	public String viewFoods(Model model) {
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
		model.addAttribute("restaurant", restaurant);
		
		List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdOrderByName(restaurantId);
		model.addAttribute("menuItems", menuItems);
		
		model.addAttribute("menuItem", new MenuItem());
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-foods";
	}
	
	@GetMapping(path = "/foods/remove")
	public String remove(@RequestParam("itemId") Integer itemId, Model model) {
		menuItemRepository.deleteById(itemId);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "redirect:/restaurant/foods";
	}
	
	@PostMapping(path = "/foods/create")
	public String createFood(
			@Valid @ModelAttribute("menuItem") MenuItem menuItem,
			Errors errors,
			Model model) {
		if (errors.hasErrors()) {
			Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
			
			Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
			model.addAttribute("restaurant", restaurant);
			
			List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdOrderByName(restaurantId);
			model.addAttribute("menuItems", menuItems);
			
			model.addAttribute("currencyUtils", currencyUtils);
			
			return "restaurant-foods";
		}
		
		restaurantService.saveMenuItem(menuItem);
		
		return "redirect:/restaurant/foods";
	}
	
	@GetMapping(path = "/order")
	public String viewOrder(
			@RequestParam("orderId") Integer orderId,
			Model model) {
		
		Order order = orderRepository.findById(orderId).orElseThrow();
		model.addAttribute("order", order);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-order";
	}
	
	@PostMapping(path = "/order/nextStatus")
	public String nextStatus(
			@RequestParam("orderId") Integer orderId,
			Model model) {
		
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setNextStatus();
		orderRepository.save(order);
		
		model.addAttribute("order", order);
		
		if (!order.getStatus().isLast()) {
			model.addAttribute("msg", "Status alterado com sucesso!");
		}
		
		if (order.getStatus().isLast()) {
			model.addAttribute("msg", "Pedido concluído.");
		}
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-order";
	}
	
	@GetMapping(path = "/report/orders")
	public String ordersReport(
			@ModelAttribute("orderReportFilter") OrderReportFilter filter,
			Model model) {
		
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		List<Order> orders = reportService.listOrders(restaurantId, filter);
		
		model.addAttribute("orders", orders);
		model.addAttribute("filter", filter);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-report-orders";
	}
	
	@GetMapping(path = "/report/items")
	public String itemsReport(
			@ModelAttribute("itemReportFilter") ItemReportFilter filter,
			Model model) {
		
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		List<MenuItem> menuItems = menuItemRepository.findByRestaurant_IdOrderByName(restaurantId);
		model.addAttribute("menuItems", menuItems);
		
		List<ItemBillingReport> calculatedItems = reportService.calculateItemsBilling(restaurantId, filter);
		model.addAttribute("calculatedItems", calculatedItems);
		
		model.addAttribute("itemReportFilter", filter);
		
		model.addAttribute("currencyUtils", currencyUtils);
		
		return "restaurant-report-items";
	}

}

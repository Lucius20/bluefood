package pierobon.paiva.lucilio.bluefood.application.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pierobon.paiva.lucilio.bluefood.domain.order.ItemBillingReport;
import pierobon.paiva.lucilio.bluefood.domain.order.ItemReportFilter;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderReportFilter;
import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;

@Service
public class ReportService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public List<Order> listOrders(Integer restaurantId, OrderReportFilter filter) {
		
		Integer orderId = filter.getOrderId();
		
		if (orderId != null) {
			Order order = orderRepository.findByIdAndRestaurant_Id(orderId, restaurantId);
			return List.of(order);
		}
		
		LocalDate startDate = filter.getStartDate();
		LocalDate endDate = filter.getEndDate();
		
		if (startDate == null) {
			return List.of();
		}
		
		if (endDate == null) {
			endDate =  LocalDate.now();
		}
		
		return orderRepository.findByDateInterval(restaurantId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		
	}
	
	public List<ItemBillingReport> calculateItemsBilling(Integer restaurantId, ItemReportFilter filter) {
		
		List<Object[]> itemsObj;
		
		Integer itemId = filter.getItemId();
		LocalDate startDate = filter.getStartDate();
		LocalDate endDate = filter.getEndDate();
		
		if (startDate == null) {
			return List.of();
		}
		
		if (endDate == null) {
			endDate =  LocalDate.now();
		}
		
		if (itemId != 0) {
			itemsObj = orderRepository.findItemsForBilling(restaurantId, itemId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		
		} else {
			itemsObj = orderRepository.findItemsForBilling(restaurantId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		}
		
		List<ItemBillingReport> items = new ArrayList<>();
		
		for (Object[] item : itemsObj) {
			String name = (String) item[0];
			long orders = (Long) item[1];
			long amount = (Long) item[2];
			BigDecimal earning = (BigDecimal) item[3];
			items.add(new ItemBillingReport(name, orders, amount, earning));
		}
		
		return items;
		
	}

}

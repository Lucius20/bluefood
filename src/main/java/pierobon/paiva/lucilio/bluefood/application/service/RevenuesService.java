package pierobon.paiva.lucilio.bluefood.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pierobon.paiva.lucilio.bluefood.domain.order.OrderRepository;
import pierobon.paiva.lucilio.bluefood.domain.order.Revenues;
import pierobon.paiva.lucilio.bluefood.util.SecurityUtils;

@Service
public class RevenuesService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public Revenues calculateRevenues(
			LocalDateTime startDay,
			LocalDateTime endDay,
			LocalDateTime startMonth,
			LocalDateTime endMonth,
			LocalDateTime startYear,
			LocalDateTime endYear) {
		
		List<Object[]> valuesObj;
		Revenues revenues = new Revenues();
		
		Integer restaurantId = SecurityUtils.loggedRestaurant().getId();
		
		valuesObj = orderRepository.findValuesForRevenues(restaurantId, startDay, endDay);
		
		for (Object[] value : valuesObj) {
			if (value != null) {
				revenues.setDay((BigDecimal) value[0]);
			} else {
				revenues.setDay(BigDecimal.valueOf(0));
			}
		}
		
		valuesObj = orderRepository.findValuesForRevenues(restaurantId, startMonth, endMonth);
		
		for (Object[] value : valuesObj) {
			if (value != null) {
				revenues.setMonth((BigDecimal) value[0]);
			} else {
				revenues.setMonth(BigDecimal.valueOf(0));
			}
		}
		
		valuesObj = orderRepository.findValuesForRevenues(restaurantId, startYear, endYear);
		
		for (Object[] value : valuesObj) {
			if (value != null) {
				revenues.setYear((BigDecimal) value[0]);
			} else {
				revenues.setYear(BigDecimal.valueOf(0));
			}
		}

	
		return revenues;
	}

}

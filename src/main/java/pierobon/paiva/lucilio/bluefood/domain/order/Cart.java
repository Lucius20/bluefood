package pierobon.paiva.lucilio.bluefood.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;

@SuppressWarnings("serial")
@Getter
public class Cart implements Serializable {
	
	private List<OrderItem> items = new ArrayList<>();
	private Restaurant restaurant;
	
	public void addItem(MenuItem menuItem, Integer amount, String note) throws RestaurantDoesNotMatchException {
		
		if (items.size() == 0) {
			restaurant = menuItem.getRestaurant();
		
		} else if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
			throw new RestaurantDoesNotMatchException();
		}
		
		if (!exists(menuItem)) {
			OrderItem orderItem = new OrderItem();
			orderItem.setMenuItem(menuItem);
			orderItem.setAmount(amount);
			orderItem.setNote(note);
			orderItem.setPrice(menuItem.getPrice());
			items.add(orderItem);
		}
		
	}
	
	public void addItem(OrderItem orderItem) {
		try {
			addItem(orderItem.getMenuItem(), orderItem.getAmount(), orderItem.getNote());
		} catch (RestaurantDoesNotMatchException e) {
		}
	}
	
	public void removeItem(MenuItem menuItem) {
		for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext();) {
			OrderItem orderItem = iterator.next();
			
			if (orderItem.getMenuItem().getId().equals(menuItem.getId())) {
				iterator.remove();
				break;
			}
			
		}
		
		if (items.size() == 0) {
			restaurant = null;
		}
	}
	
	private boolean exists(MenuItem menuItem) {
		for (OrderItem orderItem : items) {
			if (orderItem.getMenuItem().getId().equals(menuItem.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public BigDecimal getTotalPrice(boolean addDeliveryFee) {
		BigDecimal sum = BigDecimal.ZERO;
		
		for (OrderItem item : items) {
			sum = sum.add(item.getCalculatedPrice());
		}
		
		if (addDeliveryFee) {
			sum = sum.add(restaurant.getDeliveryFee());
		}
		
		return sum;
	}
	
	public void clear() {
		items.clear();
		restaurant = null;
	}
	
	public boolean empty() {
		return items.size() == 0;
	}

}

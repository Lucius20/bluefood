package pierobon.paiva.lucilio.bluefood.domain.restaurant;

import java.util.Comparator;

import pierobon.paiva.lucilio.bluefood.domain.restaurant.SearchFilter.SortOrder;

public class RestaurantComparator implements Comparator<Restaurant> {

	private SearchFilter filter;
	private String cep;
	
	
	
	public RestaurantComparator(SearchFilter filter, String cep) {
		this.filter = filter;
		this.cep = cep;
	}



	@Override
	public int compare(Restaurant r1, Restaurant r2) {
		int result;
		
		if (filter.getSortOrder() == SortOrder.FEE) {
			result = r1.getDeliveryFee().compareTo(r2.getDeliveryFee());
		
		} else if (filter.getSortOrder() == SortOrder.TIME) {
			result = r1.calculateDeliveryBaseTime(cep).compareTo(r2.calculateDeliveryBaseTime(cep));
		
		} else {
			throw new IllegalStateException("O valor de ordenação " + filter.getSortOrder() + " não é válido.");
		}
		
		return filter.isAsc() ? result : -result;
	}
	
	

}

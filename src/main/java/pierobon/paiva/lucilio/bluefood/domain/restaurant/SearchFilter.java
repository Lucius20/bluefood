package pierobon.paiva.lucilio.bluefood.domain.restaurant;

import lombok.Data;
import pierobon.paiva.lucilio.bluefood.util.StringUtils;

@Data
public class SearchFilter {
	
	public enum SearchType {
		TEXT, CATEGORY
	}
	
	public enum SortOrder {
		FEE, TIME
	}
	
	public enum Command {
		FREE_DELIVERY, HIGHEST_FEE, LOWEST_FEE, HIGHEST_TIME, LOWEST_TIME
	}
	
	private SearchType searchType;
	
	private String text;
	
	private Integer categoryId;
	
	private SortOrder sortOrder = SortOrder.FEE;
	
	private boolean asc;
	
	private boolean freeDelivery;
	
	public void processFilter(String cmdString) {
		
		if (!StringUtils.isEmpty(cmdString)) {
			Command cmd = Command.valueOf(cmdString);
			
			if (cmd == Command.FREE_DELIVERY) {
				freeDelivery = !freeDelivery;
			
			} else if (cmd == Command.HIGHEST_FEE) {
				sortOrder = SortOrder.FEE;
				asc = false;
			
			} else if (cmd == Command.LOWEST_FEE) {
				sortOrder = SortOrder.FEE;
				asc = true;
			
			} else if (cmd == Command.HIGHEST_TIME) {
				sortOrder = SortOrder.TIME;
				asc = false;
			
			} else if (cmd == Command.LOWEST_TIME) {
				sortOrder = SortOrder.TIME;
				asc = true;
			}
		}
		
		if (searchType == SearchType.TEXT) {
			categoryId = null;
		
		} else if (searchType == SearchType.CATEGORY) {
			text = null;
		}
	}

}

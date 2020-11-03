package pierobon.paiva.lucilio.bluefood.domain.order;

@SuppressWarnings("serial")
public class RestaurantDoesNotMatchException extends Exception {

	public RestaurantDoesNotMatchException() {
		super();
	}

	public RestaurantDoesNotMatchException(String message) {
		super(message);
	}
	
	

}

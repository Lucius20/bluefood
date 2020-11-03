package pierobon.paiva.lucilio.bluefood.domain.payment;

import javax.validation.constraints.Pattern;

public class CardData {
	
	@Pattern(regexp = "\\d{16}", message = "O número do cartão é inválido.")
	private String cardNumber;
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}

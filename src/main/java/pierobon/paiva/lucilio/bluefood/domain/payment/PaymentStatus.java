package pierobon.paiva.lucilio.bluefood.domain.payment;

public enum PaymentStatus {
	
	AUTHORIZED("Autorizado."),
	DENIED("Pagamento negado pela instituição financeira."),
	INVALID_CARD("Cartão inválido ou bloqueado.");
	
	String description;
	
	PaymentStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

}

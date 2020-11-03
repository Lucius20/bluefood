package pierobon.paiva.lucilio.bluefood.domain.payment;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pierobon.paiva.lucilio.bluefood.domain.order.Order;

@SuppressWarnings("serial")
@Entity
@Table(name = "payment")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment implements Serializable {
	
	@Id
	private Integer id;
	
	@NotNull
	@OneToOne
	@MapsId
	private Order order;
	
	@NotNull
	private LocalDateTime date;
	
	@NotNull
	@Size(min = 4, max = 4)
	@Column(name = "card_last_numbers")
	private String cardLastNumbers;
	
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CardLabel cardLabel;
	
	public void setNumberAndLabel(String cardNumber) {
		cardLastNumbers = cardNumber.substring(12);
		cardLabel = getLabel(cardNumber);
	}
	
	private CardLabel getLabel(String cardNumber) {
		if (cardNumber.startsWith("0000")) {
			return CardLabel.AMEX;
		}
		
		if (cardNumber.startsWith("1111")) {
			return CardLabel.ELO;
		}
		
		if (cardNumber.startsWith("2222")) {
			return CardLabel.MASTERCARD;
		}
		
		return CardLabel.VISA;
	}

}

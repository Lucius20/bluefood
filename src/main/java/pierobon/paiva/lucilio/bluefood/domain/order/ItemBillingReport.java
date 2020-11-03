package pierobon.paiva.lucilio.bluefood.domain.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemBillingReport {
	
	private String name;
	
	private long orders;
	
	private long amount;
	
	private BigDecimal earning;

}

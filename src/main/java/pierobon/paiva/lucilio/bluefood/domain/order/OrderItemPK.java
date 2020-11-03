package pierobon.paiva.lucilio.bluefood.domain.order;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("serial")
@Embeddable
public class OrderItemPK implements Serializable {
	
	@NotNull
	@ManyToOne
	private Order order;
	
	@NotNull
	private Integer sortOrder;

}

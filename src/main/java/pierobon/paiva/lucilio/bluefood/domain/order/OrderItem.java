package pierobon.paiva.lucilio.bluefood.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {
	
	@EmbeddedId
	@EqualsAndHashCode.Include
	private OrderItemPK id;
	
	@NotNull
	@ManyToOne
	private MenuItem menuItem;
	
	@NotNull
	private Integer amount;
	
	@Size(max = 50)
	private String note;
	
	@NotNull
	private BigDecimal price;
	
	public BigDecimal getCalculatedPrice() {
		return price.multiply(BigDecimal.valueOf(amount));
	}

}

package pierobon.paiva.lucilio.bluefood.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.payment.Payment;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_table")
public class Order implements Serializable {
	
	public enum Status {
		PRODUCTION(1, "EM PRODUÇÃO", false),
		DELIVERY(2, "SAIU PARA ENTREGA", false),
		COMPLETED(3, "CONCLUÍDO", true);
		
		int order;
		String description;
		boolean last;
		
		Status(int order, String description, boolean last) {
			this.order = order;
			this.description = description;
			this.last = last;
		}
		
		public String getDescription() {
			return description;
		}
		
		public int getOrder() {
			return order;
		}
		
		public boolean isLast() {
			return last;
		}
		
		public static Status fromOrder(int order) {
			for (Status status : Status.values()) {
				if (status.getOrder() == order) {
					return status;
				}
			}
			return null;
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private LocalDateTime date;
	
	@NotNull
	private Status status;
	
	@NotNull
	@ManyToOne
	private Client client;
	
	@NotNull
	@ManyToOne
	private Restaurant restaurant;
	
	@NotNull
	private BigDecimal subtotal;
	
	@NotNull
	@Column(name = "delivery_fee")
	private BigDecimal deliveryFee;
	
	@NotNull
	private BigDecimal total;
	
	@OneToMany(mappedBy = "id.order", fetch = FetchType.EAGER)
	private Set<OrderItem> items = new HashSet<>();
	
	@OneToOne(mappedBy = "order")
	private Payment payment;
	
	public String getFormattedId() {
		return String.format("#%04d", id);
	}
	
	public void setNextStatus() {
		int order = status.getOrder();
		Status newStatus = Status.fromOrder(order + 1);
		
		if (newStatus != null) {
			this.status = newStatus;	
		}
	}

}

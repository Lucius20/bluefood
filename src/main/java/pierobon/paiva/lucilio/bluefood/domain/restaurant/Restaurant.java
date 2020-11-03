package pierobon.paiva.lucilio.bluefood.domain.restaurant;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pierobon.paiva.lucilio.bluefood.domain.user.User;
import pierobon.paiva.lucilio.bluefood.infrastructure.web.validator.UploadConstraint;
import pierobon.paiva.lucilio.bluefood.util.FileType;
import pierobon.paiva.lucilio.bluefood.util.StringUtils;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@SuppressWarnings("serial")
@Table(name="restaurant")
public class Restaurant extends User {
	
	@NotBlank(message = "O CPF não pode ser vazio.")
	@Pattern(regexp = "[0-9]{14}", message = "O CNPJ possui formato inválido.")
	@Column(length = 14, nullable = false)
	private String cnpj;
	
	@Size(max = 80)
	private String logo;
	
	@UploadConstraint(acceptedTypes = {FileType.PNG, FileType.JPG}, message = "O arquivo não é um arquivo de imagem válido.")
	private transient MultipartFile logoFile;
	
	@NotNull(message = "A taxa de entrega não pode ser vazia.")
	@Min(0)
	@Max(99)
	private BigDecimal deliveryFee;
	
	@NotNull(message = "O tempo de entrega n�o pode ser vazio.")
	@Min(0)
	@Max(120)
	private Integer deliveryBaseTime;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
				name = "restaurant_has_category",
				joinColumns = @JoinColumn(name = "restaurant_id"),
				inverseJoinColumns = @JoinColumn(name = "restaurant_category_id")
			)
	@Size(min = 1, message = "O restaurante precisa ter pelo menos uma categoria.")
	@ToString.Exclude
	private Set<RestaurantCategory> categories = new HashSet<>(0);
	
	@OneToMany(mappedBy = "restaurant")
	private Set<MenuItem> menuItems = new HashSet<>(0);
	
	public void setLogoFileName() {
		if (getId() == null) {
			throw new IllegalStateException("É preciso primeiro gravar o registro.");
		}
		
		this.logo = String.format("%04d-logo.%s", getId(), FileType.of(logoFile.getContentType()).getExtension());
	}
	
	public String getCategoriesAsText() {
		Set<String> strings = new LinkedHashSet<>();
		
		for (RestaurantCategory category : categories) {
			strings.add(category.getName());
		}
		
		return StringUtils.concatenate(strings);
	}
	
	public Integer calculateDeliveryBaseTime(String cep) {
		int sum = 0;
		
		for (char c : cep.toCharArray()) {
			int v = Character.getNumericValue(c);
			
			if (v > 0) {
				sum += v;
			}
		}
		
		sum /= 2;
		
		return deliveryBaseTime + sum;
	}

}

package pierobon.paiva.lucilio.bluefood.domain.restaurant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pierobon.paiva.lucilio.bluefood.infrastructure.web.validator.UploadConstraint;
import pierobon.paiva.lucilio.bluefood.util.FileType;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "menu_item")
public class MenuItem implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	@Size(max = 50)
	private String name;
	
	@NotBlank(message = "O categoria não pode ser vazia.")
	@Size(max = 25)
	private String category;
	
	@NotBlank(message = "A descrição não pode ser vazia.")
	@Size(max = 80)
	private String description;
	
	@Size(max = 50)
	private String image;
	
	@NotNull(message = "O preço não pode ser vazio.")
	@Min(0)
	private BigDecimal price;
	
	@NotNull
	private boolean highlight;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	@UploadConstraint(acceptedTypes = FileType.PNG, message = "O arquivo não é um arquivo de imagem válido.")
	private transient MultipartFile imageFile;
	
	public void setImageFileName() {
		
		if (getId() == null) {
			throw new IllegalStateException("O objeto precisa primeiro ser criado.");
		}
		
		this.image = String.format("%04d-food.%s", getId(), FileType.of(imageFile.getContentType()).getExtension());
		
	}

}

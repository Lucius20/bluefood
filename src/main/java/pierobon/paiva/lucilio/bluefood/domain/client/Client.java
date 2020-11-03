package pierobon.paiva.lucilio.bluefood.domain.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pierobon.paiva.lucilio.bluefood.domain.user.User;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@SuppressWarnings("serial")
@Table(name="client")
public class Client extends User {

	@NotBlank(message = "O CPF não pode ser vazio.")
	@Pattern(regexp = "[0-9]{11}", message = "O CPF possui formato inválido.")
	@Column(length = 11, nullable = false)
	private String cpf;
	
	@NotBlank(message = "O CEP não pode ser vazio.")
	@Pattern(regexp = "[0-9]{8}", message = "O CEP possui formato inválido.")
	@Column(length = 8)
	private String cep;
	
	public String getFormattedCep() {
		return cep.substring(0, 5) + "-" + cep.substring(5);
	}
	
}

package pierobon.paiva.lucilio.bluefood.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import pierobon.paiva.lucilio.bluefood.application.service.ClientService;
import pierobon.paiva.lucilio.bluefood.application.service.ValidationException;
import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ClientServiceTest {
	
	@Autowired
	private ClientService clientService;
	
	@MockBean
	private ClientRepository clientRepository;
	
	@Test
	public void testWhenHasDuplicateEmail() throws Exception {
		
		Client c1 = new Client();
		c1.setId(1);
		c1.setName("José");
		c1.setEmail("a@a.com");
		
		Mockito.when(clientRepository.findByEmail(c1.getEmail())).thenReturn(c1);
		
		Client c2 = new Client();
		c2.setEmail("a@a.com");
		
		assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> clientService.saveClient(c2));
		
	}

}

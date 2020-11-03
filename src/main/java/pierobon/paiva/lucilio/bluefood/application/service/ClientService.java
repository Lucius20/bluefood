package pierobon.paiva.lucilio.bluefood.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Transactional
	public void saveClient(Client client) throws ValidationException {
		
		if (!validateEmail(client.getEmail(), client.getId())) {
			throw new ValidationException("O e-mail está duplicado.");
		}
		
		if (client.getId() != null) {
			
			Client clientDB = clientRepository.findById(client.getId()).orElseThrow();
			client.setPassword(clientDB.getPassword());
			
		} else {
			client.encryptPassword();
		}
		
		clientRepository.save(client);
	}
	
	private boolean validateEmail(String email, Integer id) {
		
		Restaurant restaurant = restaurantRepository.findByEmail(email);
		
		if (restaurant != null) {
			return false;
		}
		
		Client client = clientRepository.findByEmail(email);
		
		if (client != null) {
			if (id == null) {
				return false;
			}
			
			if (!client.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}

}

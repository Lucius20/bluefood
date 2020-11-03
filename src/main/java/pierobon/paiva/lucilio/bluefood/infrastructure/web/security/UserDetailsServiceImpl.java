package pierobon.paiva.lucilio.bluefood.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;
import pierobon.paiva.lucilio.bluefood.domain.user.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = clientRepository.findByEmail(username);
		
		if (user == null) {
			user = restaurantRepository.findByEmail(username);
			
			if (user == null) {
				throw new UsernameNotFoundException(username);
			}
		}
		
		return new LoggedUser(user);
		
	}
	
}

package pierobon.paiva.lucilio.bluefood.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.infrastructure.web.security.LoggedUser;

public class SecurityUtils {
	
	public static LoggedUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Client loggedClient() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("Não existe um usuário logado.");
		}
		
		if (!(loggedUser.getUser() instanceof Client)) {
			throw new IllegalStateException("O usuário logado não é um cliente.");
		}
		
		return (Client) loggedUser.getUser();
	}
	
	public static Restaurant loggedRestaurant() {
		LoggedUser loggedUser = loggedUser();
		
		if (loggedUser == null) {
			throw new IllegalStateException("N�o existe um usuário logado.");
		}
		
		if (!(loggedUser.getUser() instanceof Restaurant)) {
			throw new IllegalStateException("O usuário logado não é um restaurante.");
		}
		
		return (Restaurant) loggedUser.getUser();
	}

}

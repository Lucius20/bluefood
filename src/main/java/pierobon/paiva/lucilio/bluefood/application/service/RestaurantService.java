package pierobon.paiva.lucilio.bluefood.application.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pierobon.paiva.lucilio.bluefood.domain.client.Client;
import pierobon.paiva.lucilio.bluefood.domain.client.ClientRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItem;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.MenuItemRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.Restaurant;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantComparator;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantRepository;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.SearchFilter;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.SearchFilter.SearchType;
import pierobon.paiva.lucilio.bluefood.util.SecurityUtils;

@Service
public class RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional
	public void saveRestaurant(Restaurant restaurant) throws ValidationException {
		
		if (!validateEmail(restaurant.getEmail(), restaurant.getId())) {
			throw new ValidationException("O e-mail está duplicado.");
		}
		
		if (restaurant.getId() != null) {
			
			Restaurant restaurantDB = restaurantRepository.findById(restaurant.getId()).orElseThrow();
			restaurant.setPassword(restaurantDB.getPassword());
			restaurant.setLogo(restaurantDB.getLogo());
			restaurantRepository.save(restaurant);
			
		} else {
			restaurant.encryptPassword();
			restaurant = restaurantRepository.save(restaurant);
			restaurant.setLogoFileName();
			imageService.uploadLogo(restaurant.getLogoFile(), restaurant.getLogo());
		}
		
		
	}
	
	private boolean validateEmail(String email, Integer id) {
		
		Client client = clientRepository.findByEmail(email);
				
				if (client != null) {
					return false;
				}
		
		Restaurant restaurant = restaurantRepository.findByEmail(email);
		
		if (restaurant != null) {
			if (id == null) {
				return false;
			}
			
			if (!restaurant.getId().equals(id)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<Restaurant> search(SearchFilter filter) {
		
		List<Restaurant> restaurants;
		
		if (filter.getSearchType() == SearchType.TEXT) {
			restaurants = restaurantRepository.findByNameIgnoreCaseContaining(filter.getText());
		
		} else if (filter.getSearchType() == SearchType.CATEGORY) {
			restaurants = restaurantRepository.findByCategories_Id(filter.getCategoryId());
			
		} else {
			throw new IllegalStateException("O tipo de busca " + filter.getSearchType() + " não é suportado.");
		}
		
		Iterator<Restaurant> it = restaurants.iterator();
		
		while (it.hasNext()) {
			Restaurant restaurant = it.next();
			
			double deliveryFee = restaurant.getDeliveryFee().doubleValue();
			
			if (filter.isFreeDelivery() && deliveryFee > 0 || !filter.isFreeDelivery() && deliveryFee == 0) {
				it.remove();
			}
		}
		
		RestaurantComparator comparator = new RestaurantComparator(filter, SecurityUtils.loggedClient().getCep());
		restaurants.sort(comparator);
		
		return restaurants;
	}
	
	@Transactional
	public void saveMenuItem(MenuItem menuItem) {
		menuItem = menuItemRepository.save(menuItem);
		menuItem.setImageFileName();
		imageService.uploadFood(menuItem.getImageFile(), menuItem.getImage());
	}

}

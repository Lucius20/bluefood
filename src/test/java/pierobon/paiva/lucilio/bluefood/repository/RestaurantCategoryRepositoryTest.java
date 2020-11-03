package pierobon.paiva.lucilio.bluefood.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategory;
import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategoryRepository;

@DataJpaTest
@ActiveProfiles("test")
public class RestaurantCategoryRepositoryTest {
	
	@Autowired
	private RestaurantCategoryRepository restaurantCategoryRepository;
	
	@Test
	public void testInsertAndDelete() throws Exception {
		assertThat(restaurantCategoryRepository).isNotNull();
		
		RestaurantCategory cr = new RestaurantCategory();
		cr.setName("Chinesa");
		cr.setImage("chinesa.png");
		restaurantCategoryRepository.saveAndFlush(cr);
		
		assertThat(cr.getId()).isNotNull();
		
		RestaurantCategory cr2 = restaurantCategoryRepository.findById(cr.getId()).orElseThrow();
		
		assertThat(cr.getName()).isEqualTo(cr2.getName());
		
		assertThat(restaurantCategoryRepository.findAll()).hasSize(7);
		
		restaurantCategoryRepository.delete(cr);
		
		assertThat(restaurantCategoryRepository.findAll()).hasSize(6);
	}

}

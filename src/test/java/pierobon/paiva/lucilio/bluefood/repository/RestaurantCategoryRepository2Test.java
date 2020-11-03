package pierobon.paiva.lucilio.bluefood.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import pierobon.paiva.lucilio.bluefood.domain.restaurant.RestaurantCategory;

@DataJpaTest
@ActiveProfiles("test")
public class RestaurantCategoryRepository2Test {
	
	@Autowired
	private TestEntityManager em;
	
	@Test
	public void testInsertAndDelete() throws Exception {
		assertThat(em).isNotNull();
		
		RestaurantCategory cr = new RestaurantCategory();
		cr.setName("Chinesa");
		cr.setImage("chinesa.png");
		em.persistAndFlush(cr);
		
		assertThat(cr.getId()).isNotNull();
		
		RestaurantCategory cr2 = em.find(RestaurantCategory.class, cr.getId());
		
		assertThat(cr.getName()).isEqualTo(cr2.getName());
		
		em.remove(cr);
		
		assertThat(em.find(RestaurantCategory.class, cr.getId())).isNull();
	}

}

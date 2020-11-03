package pierobon.paiva.lucilio.bluefood.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE o.client.id = ?1 ORDER BY o.date DESC")
	public List<Order> listOrdersFromClient(Integer clientId);
	
	public List<Order> findByRestaurant_IdOrderByDateDesc(Integer restaurantId);
	
	@Query("SELECT o FROM Order o WHERE o.restaurant.id = ?1 AND NOT (o.status = 2) ORDER BY o.date DESC")
	public List<Order> findByRestaurant_IdAndNotCompleteOrderByDateDesc(Integer restaurantId);
	
	public Order findByIdAndRestaurant_Id(Integer orderId, Integer restaurantId);
	
	@Query("SELECT o FROM Order o WHERE o.restaurant.id = ?1 AND o.date BETWEEN ?2 AND ?3")
	public List<Order> findByDateInterval(Integer restaurantId, LocalDateTime startDate, LocalDateTime endDate);
	
	@Query("SELECT i.menuItem.name, COUNT(i.menuItem.id), SUM(i.amount), SUM(i.price * i.amount) FROM Order o INNER JOIN o.items i "
			+ "WHERE o.restaurant.id = ?1 AND i.menuItem.id = ?2 AND o.date BETWEEN ?3 AND ?4 GROUP BY i.menuItem.name")
	public List<Object[]> findItemsForBilling(Integer restaurantId, Integer menuItemId, LocalDateTime startDate, LocalDateTime endDate);
	
	@Query("SELECT i.menuItem.name, COUNT(i.menuItem.id), SUM(i.amount), SUM(i.price * i.amount) FROM Order o INNER JOIN o.items i "
			+ "WHERE o.restaurant.id = ?1 AND o.date BETWEEN ?2 AND ?3 GROUP BY i.menuItem.name")
	public List<Object[]> findItemsForBilling(Integer restaurantId, LocalDateTime startDate, LocalDateTime endDate);
	
	@Query("SELECT SUM(i.price * i.amount) FROM Order o INNER JOIN o.items i "
			+ "WHERE o.restaurant.id = ?1 AND o.date BETWEEN ?2 AND ?3")
	public List<Object[]> findValuesForRevenues(Integer restaurantId, LocalDateTime startDate, LocalDateTime endDate);
}

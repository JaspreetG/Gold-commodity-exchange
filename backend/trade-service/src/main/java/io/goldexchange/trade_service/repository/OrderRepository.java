
package io.goldexchange.trade_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import io.goldexchange.trade_service.model.Order;

/**
 * Repository interface for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds orders by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of orders placed by the user.
     */
    List<Order> findByUserId(Long userId);
}


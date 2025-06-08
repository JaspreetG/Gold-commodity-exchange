
package io.goldexchange.trade_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import io.goldexchange.trade_service.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // List<Trade> findByUserId(Long userId);
}


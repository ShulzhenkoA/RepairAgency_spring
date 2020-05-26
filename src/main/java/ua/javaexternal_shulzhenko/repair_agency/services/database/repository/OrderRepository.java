package ua.javaexternal_shulzhenko.repair_agency.services.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findAllByStatusIn(Collection<OrderStatus> statuses, Pageable pageable);

    Page<Order> findAllByCustomer_IdAndStatusIn(int customerId, Collection<OrderStatus> statuses, Pageable pageable);

    Page<Order> findAllByCustomer_IdAndStatusNotIn(int customerId, Collection<OrderStatus> statuses, Pageable pageable);

    Page<Order> findAllByMaster_IdAndStatusIn(int masterId, Collection<OrderStatus> statuses, Pageable pageable);

    Page<Order> findAllByMaster_IdAndStatusNotIn(int masterId, Collection<OrderStatus> statuses, Pageable pageable);
}

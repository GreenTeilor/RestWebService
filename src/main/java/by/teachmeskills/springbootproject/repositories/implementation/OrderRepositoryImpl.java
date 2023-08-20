package by.teachmeskills.springbootproject.repositories.implementation;

import by.teachmeskills.springbootproject.entities.Order;
import by.teachmeskills.springbootproject.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final static String GET_ALL_ORDERS_QUERY = "select o from Order o";

    @PersistenceContext
    private final EntityManager manager;

    @Override
    public Order create(Order order) {
        manager.persist(order);
        return order;
    }

    @Override
    public List<Order> read() {
        return manager.createQuery(GET_ALL_ORDERS_QUERY, Order.class).getResultList();
    }

    @Override
    public Order update(Order order) {
        return manager.merge(order);
    }

    @Override
    public void delete(int id) {
        Order order = manager.find(Order.class, id);
        manager.remove(order);
    }
}

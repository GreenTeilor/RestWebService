package by.teachmeskills.springbootproject.repositories.implementation;

import by.teachmeskills.springbootproject.entities.User;
import by.teachmeskills.springbootproject.repositories.UserRepository;
import by.teachmeskills.springbootproject.utils.HashUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final static String GET_USER_BY_EMAIL = "select u from User u where u.email=:email";
    private final static String SEARCH_USER_QUERY = "select u from User u where u.email=:email and u.password=:password";
    private final static String UPDATE_ADDRESS_AND_PHONE_NUMBER_QUERY = "update User set address=:address, phoneNumber=:phoneNumber where email=:email";
    private final static String GET_ALL_USERS_QUERY = "select u from User u";
    private final static String GET_USER_FAVORITE_GENRE = "SELECT category FROM (SELECT category, count(*) as count " +
            "FROM orders_products JOIN products ON productId = products.id " +
            "JOIN orders ON orderId = orders.id WHERE userId = ? GROUP BY category) as res1 ORDER BY count DESC LIMIT 1";

    private final static String GET_USER_DAYS_REGISTERED = "SELECT datediff(CURRENT_TIMESTAMP, registrationDate) as result FROM users WHERE id = ?";
    private final static String GET_USER_PURCHASED_BOOKS_COUNT = "SELECT count(*) FROM orders_products JOIN orders ON orderId = orders.id WHERE userId = ?";
    private final static String GET_USER_ORDERS_COUNT = "SELECT count(*) FROM orders WHERE userId = ?";

    @PersistenceContext
    private final EntityManager manager;

    @Override
    public Optional<User> getUserByEmail(String email) {
        TypedQuery<User> query = manager.createQuery(GET_USER_BY_EMAIL, User.class);
        query.setParameter("email", email);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(manager.find(User.class, id));
    }

    @Override
    public Optional<User> getUser(String email, String password) {
        TypedQuery<User> query = manager.createQuery(SEARCH_USER_QUERY, User.class);
        query.setParameter("email", email);
        query.setParameter("password", HashUtils.getHash(password));
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateAddressAndPhoneNumber(String address, String phoneNumber, String email) {
        Query query = manager.createQuery(UPDATE_ADDRESS_AND_PHONE_NUMBER_QUERY);
        query.setParameter("address", address);
        query.setParameter("phoneNumber", phoneNumber);
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Override
    public String getUserFavoriteGenre(int id) {
        Query query = manager.createNativeQuery(GET_USER_FAVORITE_GENRE, String.class);
        query.setParameter(1, id);
        String result;
        try {
            result = (String) query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result != null ? result : "Не определен";
    }

    @Override
    public int getUserDaysRegistered(int id) {
        Query query = manager.createNativeQuery(GET_USER_DAYS_REGISTERED, Integer.class);
        query.setParameter(1, id);
        return (int)query.getSingleResult();
    }

    @Override
    public int getUserPurchasedBooksCount(int id) {
        Query query = manager.createNativeQuery(GET_USER_PURCHASED_BOOKS_COUNT, Integer.class);
        query.setParameter(1, id);
        return (int)query.getSingleResult();
    }

    @Override
    public int getUserOrdersCount(int id) {
        Query query = manager.createNativeQuery(GET_USER_ORDERS_COUNT, Integer.class);
        query.setParameter(1, id);
        return (int)query.getSingleResult();
    }

    @Override
    public User create(User user) {
        user.setPassword(HashUtils.getHash(user.getPassword()));
        manager.persist(user);
        return user;
    }

    @Override
    public List<User> read() {
        return manager.createQuery(GET_ALL_USERS_QUERY, User.class).getResultList();
    }

    @Override
    public User update(User user) {
        return manager.merge(user);
    }

    @Override
    public void delete(int id) {
        User user = manager.find(User.class, id);
        manager.remove(user);
    }
}

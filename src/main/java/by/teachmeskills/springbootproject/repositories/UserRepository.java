package by.teachmeskills.springbootproject.repositories;

import by.teachmeskills.springbootproject.entities.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);

    Optional<User> getUser(String email, String password);

    void updateAddressAndPhoneNumber(String address, String phoneNumber, String email);

    String getUserFavoriteGenre(int id);
    int getUserDaysRegistered(int id);
    int getUserPurchasedBooksCount(int id);
    int getUserOrdersCount(int id);
}

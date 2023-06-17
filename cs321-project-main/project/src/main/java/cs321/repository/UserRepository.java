package cs321.repository;
import cs321.model.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}

package bot.findme.repository;

import bot.findme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {
    @Query(value = "select u from User u where u.user_id = :user_id")
    Optional<User> findByUserID(String user_id);
}

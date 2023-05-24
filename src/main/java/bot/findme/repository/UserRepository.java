package bot.findme.repository;

import bot.findme.model.Keyboard;
import bot.findme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {
    @Transactional
    @Modifying
    @Query(value = "update public.user set command = :command where user_id = :user_id", nativeQuery = true)
    void setCommand(String command,String user_id);

    @Transactional
    @Modifying
    @Query(value = "update public.user set value = :command where user_id = :user_id", nativeQuery = true)
    void setValue(String command,String user_id);

    @Query(value = "select u from User u where u.user_id = :user_id")
    Optional<User> findByUserID(String user_id);
}

package bot.findme.repository;

import bot.findme.model.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeyboardRepository extends JpaRepository <Keyboard,Long> {
    @Query(value = "select u from Keyboard u where u.menu = :menu")
    List<Keyboard> findByMenu(String menu);
}

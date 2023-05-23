package bot.findme.repository;

import bot.findme.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository <Menu,Long> {

}

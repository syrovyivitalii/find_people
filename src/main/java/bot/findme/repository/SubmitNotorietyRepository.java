package bot.findme.repository;

import bot.findme.model.SubmitNotoriety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
@Transactional
public interface SubmitNotorietyRepository extends JpaRepository <SubmitNotoriety,String> {
    @Query(value = "select u from SubmitNotoriety u where u.passport = :passport AND u.information IS NULL")
    Optional<SubmitNotoriety> findByPassport(String passport);

}

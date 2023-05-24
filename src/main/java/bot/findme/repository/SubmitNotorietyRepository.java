package bot.findme.repository;

import bot.findme.model.SubmitNotoriety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
@Transactional
public interface SubmitNotorietyRepository extends JpaRepository <SubmitNotoriety,String> {
    @Query(value = "select u from SubmitNotoriety u where u.passport = :passport")
    Optional<SubmitNotoriety> findByPassport(String passport);

    @Transactional
    @Modifying
    @Query(value = "update public.submit_notoriety set last_name = :command where passport = :passport", nativeQuery = true)
    void setLastName(String command,String passport);
}

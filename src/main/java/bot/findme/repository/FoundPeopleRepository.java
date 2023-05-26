package bot.findme.repository;

import bot.findme.model.FoundPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FoundPeopleRepository extends JpaRepository <FoundPeople,Long> {
    @Query(value = "SELECT u.information FROM FoundPeople u WHERE u.first_name = :firstName AND u.last_name = :lastName AND u.middle_name = :middleName AND u.date_of_birth = :dateOfBirth AND u.city = :city")
    List<String> findMatchingRows(@Param("firstName") String firstName, @Param("lastName") String lastName,@Param("middleName") String middleName,
                                       @Param("dateOfBirth") LocalDate dateOfBirth, @Param("city") String city);
}

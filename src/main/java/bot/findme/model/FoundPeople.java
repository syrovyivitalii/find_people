package bot.findme.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "found_people", schema = "public")
@Data
public class FoundPeople {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String middle_name;
    private LocalDate date_of_birth;
    private String city;
    private String information;

}

package bot.findme.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user", schema = "public")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user_id;
    private String command;
    private String value;
    private String set_passport;
    private String set_first_name;
    private String set_last_name;
    private String set_middle_name;
    private LocalDate set_date_of_birth;
    private String set_city;
}

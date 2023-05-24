package bot.findme.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "submit_notoriety", schema = "public")
@Data
public class SubmitNotoriety {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passport;
    private String first_name;
    private String last_name;
    private String middle_name;
    private String phone;
    private String information;
}

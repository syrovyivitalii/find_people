package bot.findme.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "replykeyboard", schema = "public")
@Data
public class Keyboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menu;
    private String keyboard;
}

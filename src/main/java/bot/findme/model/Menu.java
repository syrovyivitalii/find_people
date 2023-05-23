package bot.findme.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "menu", schema = "public")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menu;
}

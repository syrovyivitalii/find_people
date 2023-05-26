package bot.findme.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "questions", schema = "public")
@Data
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answer;
}

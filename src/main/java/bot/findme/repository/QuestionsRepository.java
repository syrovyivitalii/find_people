package bot.findme.repository;

import bot.findme.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository <Questions,Long> {
}

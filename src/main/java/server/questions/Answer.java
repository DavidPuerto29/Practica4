package server.questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase encargada de almacenar las respuestas a las preguntas
 * consta con la implementación de hibernate.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@Entity
@Getter @Setter  @NoArgsConstructor
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    public Answer(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    @Override
    public String toString() {
        return  "Id: " + id +
                " Answer: '" + answer + '\'' +
                " Correct: " + correct;
    }

}

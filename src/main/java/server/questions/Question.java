package server.questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de almacenar las preguntas del juego además de
 * sus correspondientes respuestas y categoría, consta con
 * la implementación de hibernate además con consultas definidas
 * utilizadas posteriormente en la aplicación.
 * Y dos métodos de gestión de aciertos/fallos.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@Entity
@Getter @Setter  @NoArgsConstructor
@NamedQuery(name = "get_QuestionsGame", query = "FROM Question q ORDER BY FUNCTION('RAND')")
@NamedQuery(name = "get_hardQuestions", query = "FROM Question q ORDER BY numFailure DESC")
@NamedQuery(name = "get_easyQuestions", query = "FROM Question q ORDER BY numCorrect DESC")

public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    private int numCorrect;
    private int numFailure;

    public Question(String question, List<Answer> answers, Category category, int numCorrect, int numFailure) {
        this.question = question;
        this.answers = answers;
        this.category = category;
        this.numCorrect = numCorrect;
        this.numFailure = numFailure;
    }

    @Override
    public String toString() {
        return  "Id: " + id +
                " Question: '" + question + '\'' +
                " Answers: " + answers +
                " Category: " + category +
                " Successes: " + numCorrect +
                " Errors: " + numFailure ;
    }


    public void addFailure(){
        this.numFailure++;
    }

    /**
     * Método utilizado para añadir un acierto
     * a la pregunta.
     */
    public void addCorrect(){
        this.numCorrect++;
    }
}

package server.dao;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.questions.Question;

import java.util.List;

/**
 * Colección de métodos de la clase Question, consta con un método
 * para agregar Question a la sql y eliminar, ademas cuenta con otro
 * método que busca 6 preguntas y las devuelve en una lista y por ultimo
 * cuenta con dos métodos que buscan las 5 preguntas mas fáciles y mas difíciles.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class QuestionDAO {
    /**
     * Crea la entidad question en la base de datos.
     * @param question Entidad creada.
     */
    public static void createSqlQuestion(Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            session.getTransaction().begin();
            session.persist(question);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina la entidad question en la base de datos.
     * @param question Entidad eliminada.
     */
    public static void deleteSqlQuestion(Question question){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            session.remove(question);
            session.getTransaction().commit();
        }
    }

    /**
     * Es la consulta utilizada en cada partida para obtener las preguntas,
     * se obtienen 6 preguntas de la base de datos y se devuelven en una lista.
     *
     * @return La lista con las preguntas correspondientes
     */
    public static List<Question> findSqlQuestionsGame(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Question> q = session.createNamedQuery("get_QuestionsGame", Question.class);
            q.setMaxResults(6);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Es la consulta utilizada en para obtener las preguntas más difíciles,
     * se obtienen las 5 preguntas más difíciles de la base de datos y se devuelven en una lista.
     *
     * @return La lista con las preguntas correspondientes
     */
    public static List<Question> findSqlHardQuestions(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Question> q = session.createNamedQuery("get_hardQuestions", Question.class);
            q.setMaxResults(5);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Es la consulta utilizada en para obtener las preguntas más fáciles,
     * se obtienen las 5 preguntas más fáciles de la base de datos y se devuelven en una lista.
     *
     * @return La lista con las preguntas correspondientes
     */
    public static List<Question> findSqlEasyQuestions(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Question> q = session.createNamedQuery("get_easyQuestions", Question.class);
            q.setMaxResults(5);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Método usado para cuando el usuario contesta una pregunta
     * que se actualice el numero de aciertos y de fallos.
     *
     * @param question Pregunta que es actualizada.
     */
    public static void updateSqlQuestion(Question question){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            session.merge(question);
            session.getTransaction().commit();
        }
    }
}

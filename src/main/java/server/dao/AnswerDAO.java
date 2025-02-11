package server.dao;

import org.hibernate.Session;
import server.questions.Answer;

/**
 * Colección de métodos de la clase Answser, consta con un método
 * para agregar Answers a la sql y eliminar.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class AnswerDAO {

    /**
     * Guarda la entidad answer en la base de datos.
     * @param answer Entidad guardada.
     */
    public static void createSqlAnswer(Answer answer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.persist(answer);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina la entidad answer en la base de datos.
     * @param answer Entidad eliminada.
     */
    public static void deleteSqlAnswer(Answer answer){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session.remove(answer);
            session.getTransaction().commit();
        }
    }

}

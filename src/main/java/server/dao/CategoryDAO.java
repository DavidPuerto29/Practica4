package server.dao;

import org.hibernate.Session;
import server.questions.Category;

/**
 * Colección de métodos de la clase Category, consta con un método
 * para agregar Category a la sql y eliminar.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class CategoryDAO {
    /**
     * Crea la entidad category en la base de datos.
     * @param category Entidad creada.
     */
    public static void createSqlCategory(Category category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            session.getTransaction().begin();
            session.persist(category);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina la entidad category en la base de datos.
     * @param category Entidad eliminada.
     */
    public static void deleteSqlAnswer(Category category){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            session.remove(category);
            session.getTransaction().commit();
        }
    }

}

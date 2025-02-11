package server.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import server.games.Game;
import server.games.Player;
import server.questions.Answer;
import server.questions.Category;
import server.questions.Question;

/**
 * Clase destinada a configuración para el gestor de datos
 * sql de hibernate.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try (StandardServiceRegistry registry = new
                    StandardServiceRegistryBuilder().build();) {
                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(Question.class)
                        .addAnnotatedClass(Answer.class)
                        .addAnnotatedClass(Category.class)
                        .addAnnotatedClass(Player.class)
                        .addAnnotatedClass(Game.class)
                       // .addAnnotatedClass(Category.class)
                        //.addAnnotatedClass(...) Otras clases
                        .buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

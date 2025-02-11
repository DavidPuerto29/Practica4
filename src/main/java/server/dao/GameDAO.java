package server.dao;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.games.Game;

import java.util.List;

/**
 * Colección de métodos de la clase Game, consta con un método
 * para agregar Game a la sql y eliminar, también otro método
 * para eliminar todos los juegos de la sql y ods métodos de consulta
 * uno destinado a encontrar las 10 mejores partidas y el otro destinado
 * a encontrar todas las partidas.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class GameDAO {
    /**
     * Crea la entidad game en la base de datos.
     * @param game Entidad creada.
     */
    public static void createSqlGame(Game game) {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
            session.getTransaction().begin();
            session.persist(game);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina la entidad game en la base de datos.
     * @param game Entidad eliminada.
     */
    public static void deleteSqlGame(Game game){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            session.remove(game);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina todas las entidades game de la base de datos.
     */
    public static int AllDeleteSqlGame(){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            Query query = session.createNamedQuery("delete_all_games");
            int rowsDeleted = query.executeUpdate();
            session.getTransaction().commit();
            return rowsDeleted;
        }
    }

    /**
     * Busca todas las entidades game de la base de datos.
     */
    public static List<Game> findAllGames(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Game> q = session.createNamedQuery("get_all_games", Game.class);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Busca las 10 partidas con más puntuación de la base de datos y las
     * devuelve en una lista.
     *
     * @return La lista con los 10 juegos con más puntuación.
     */
    public static List<Game> findTop10Games(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Game> q = session.createNamedQuery("get_top10Games", Game.class);
            q.setMaxResults(10);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}

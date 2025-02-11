package server.dao;

import jakarta.persistence.NoResultException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import server.games.Player;

/**
 * Colección de métodos de la clase Player, consta con un método
 * para agregar Player a la sql, eliminar y actualizar,
 * otro método para encontrar a un jugador por nombre y contraseña
 * y por ultimo contiene un método para resetear todos los contadores
 * maxScore de los jugadores de la base de datos.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class PlayerDAO {
    /**
     * Guarda la entidad player en la base de datos.
     * @param player Entidad guardada.
     */
    public static void createSqlPlayer(Player player) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.persist(player);
            session.getTransaction().commit();
        }
    }

    /**
     * Elimina la entidad player en la base de datos.
     * @param player Entidad eliminada.
     */
    public static void deleteSqlPlayer(Player player){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session.remove(player);
            session.getTransaction().commit();
        }
    }

    /**
     * Realiza una consulta en la base de datos que busca a un jugador
     * mediante su usuario y su contraseña, esta consulta se usa para
     * loguear a un usuario antes de jugar.
     *
     * @param name Nombre de usuario proporcionado.
     * @param pass Contraseña del usuario proporcionado.
     * @return En caso de que se encuentre al usuario lo devuelve y si no es
     * encontrado devuelve null.
     */
    public static Player findSqlPlayer(String name,String pass){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Player> q = session.createNamedQuery("get_player", Player.class);
            q.setParameter("name", name);
            q.setParameter("pass", pass);
            return (Player) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Método usado para actualizar al jugador al final de una partida para
     * registrar los nuevos datos.
     *
     * @param player Jugador que es actualizado.
     */
    public static void updatePlayer(Player player){
        try(Session session = HibernateUtil.getSessionFactory().openSession();){
            session.getTransaction().begin();
            session.merge(player);
            session.getTransaction().commit();
        }
    }

    /**
     * Utilizado para cuando desde la clase servidor se eliminan todas las partidas
     * ya, que también hay que reiniciar el contador de maxScore de los usuarios a 0.
     *
     * @return Devuelve el número de jugadores actualizados.
     */
    public static int maxScore0SqlPlayer(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            Query query = session.createNamedQuery("player_resetMaxScore");
                int rowsDeleted = query.executeUpdate();
            session.getTransaction().commit();
            return rowsDeleted;
        }
    }
}

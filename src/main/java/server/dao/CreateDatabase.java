package server.dao;

/**
 * En caso de que la base de datos no esté creada esta clase la crea,
 * tan solo ejecutando el main.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class CreateDatabase {
    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
    }
}

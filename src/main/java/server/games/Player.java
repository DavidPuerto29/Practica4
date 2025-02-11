package server.games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de almacenar la información del usuario
 * que está jugando, consta con la implementación de hibernate además con consultas
 * definidas utilizadas posteriormente en la aplicación.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@Entity
@Getter @Setter  @NoArgsConstructor
@NamedQuery(name = "get_player", query = "FROM Player p WHERE p.name = :name AND p.pass = :pass")
@NamedQuery(name = "player_resetMaxScore", query = "UPDATE Player p SET p.maxScore = 0")
public class Player {
    @Id
    private String name;

    private String pass;
    private LocalDate registrationDate;
    private int maxScore;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Game> game = new ArrayList<>();


    public Player(String name, String pass, LocalDate registrationDate, int maxScore, List<Game> game) {
        this.name = name;
        this.pass = pass;
        this.registrationDate = registrationDate;
        this.maxScore = maxScore;
        this.game = game;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", registrationDate=" + registrationDate +
                ", maxScore=" + maxScore +
                ", game=" + game +
                '}';
    }

    /**
     * Método utilizado para actualizar el score máximo del jugador,
     * al final de cada partida es llamado y si la puntuación de la partida
     * es mayor al score máximo lo actualiza.
     *
     * @param points Los puntos a comprobar.
     * @return True si el valor es mayor que el score máximo del player.
     */
    public boolean updateMaxScore(int points){
        return maxScore < points;
    }

    /**
     * Método utilizado para añadir una partida a la lista
     * de partidas del jugador, cada vez que se comienza
     * una partida es llamado para almacenarlo en el jugador.
     *
     * @param game Partida llamada para guardarla en el jugador.
     */
    public void addGame(Game game){
        this.game.add(game);
    }

}

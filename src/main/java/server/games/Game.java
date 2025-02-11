package server.games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase destinada a almacenar cada partida jugada por un jugador,
 * consta con la implementación de hibernate además con consultas
 * definidas utilizadas posteriormente en la aplicación.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@Entity
@Getter @Setter  @NoArgsConstructor
@NamedQuery(name = "get_top10Games", query = "SELECT g FROM Game g ORDER BY g.score DESC")
@NamedQuery(name = "delete_all_games", query = "DELETE FROM Game")
@NamedQuery(name = "get_all_games", query = "SELECT p FROM Game p")
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_name")
    private Player player;

    private int score;
    private LocalDateTime date;

    public Game(Player player, int score, LocalDateTime date) {
        this.player = player;
        this.score = score;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Player_name: " + player.getName() +
                " Score: " + score +
                " Date: " + date ;
    }

    /**
     * Método encargado de añadir un punto al jugador,
     * utilizado para cuando el jugador contesta una pregunta
     * correctamente.
     */
    public void addOneScore(){
        this.score++;
    }
}

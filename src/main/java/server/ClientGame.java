package server;

import server.games.Game;
import server.games.Player;
import server.questions.Answer;
import server.questions.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static server.dao.GameDAO.createSqlGame;
import static server.dao.PlayerDAO.findSqlPlayer;
import static server.dao.PlayerDAO.updatePlayer;
import static server.dao.QuestionDAO.findSqlQuestionsGame;
import static server.dao.QuestionDAO.updateSqlQuestion;

/**
 * Es el host de nuestro juego, contiene toda la lógica del mismo
 * con las entradas/salidas necesarias para el usuario y para el
 * propio trivial, es llamado desde la clase ServerClient
 * para que pueda haber varias instancias en ejecución a la vez.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class ClientGame extends Thread{
    Socket socket;

    public ClientGame(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        runGame();
    }

    private void runGame(){
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            boolean gameInProcess = true;
            boolean userAutentication = false;
            boolean userWantsToPlay = true;
            boolean correctFormatAnswer = false;
            int counTryToAcces = 0;
            Player playerSql = null;

            while(gameInProcess) {

                //Autenticación de usuario
                while(!userAutentication){
                    pw.println("Introduzca el usuario:");
                        String name = br.readLine();

                    pw.println("Introduzca la contraseña:");
                        String pass = br.readLine();

                    //Buscamos al jugador
                    playerSql = findSqlPlayer(name, pass);
                    if(playerSql != null) {
                        userAutentication = true;
                            pw.println("true");
                                pw.println("codigo 10");
                    }else{
                        pw.println("false");
                            pw.println("Usuario incorrecto");
                                //Contador para solo permitir 3 intentos de acceso.
                                counTryToAcces++;
                        if(counTryToAcces == 3){
                            pw.println("codigo 11");
                                //Se realizan estos cambios para finalizar el programa sin excepciones.
                                userWantsToPlay = false;
                                    gameInProcess = false;
                                        break;
                        }
                    }
                }

                //Mientras el usuario quiera jugar.
                while(userWantsToPlay){
                    //Creación de juego actual y de sus preguntas.
                    Game gameNow = new Game(playerSql,0, LocalDateTime.now());
                    playerSql.addGame(gameNow);
                    List<Question> questionsGame = findSqlQuestionsGame();

                    int count = 0;
                    for (int i = 0; i <= 5; ++i) {  //modificado
                        count++;
                        Answer correctAnswer = new Answer();
                        //Mezclamos las preguntas.
                        Collections.shuffle(questionsGame.get(i).getAnswers());

                        pw.println(+count+"ºPregunta");
                        pw.println(questionsGame.get(i).getQuestion());
                        for (int var = 0; var <= 3; ++var) {
                            pw.println(questionsGame.get(i).getAnswers().get(var).getAnswer());
                                //Aprovechamos para guardar la pregunta correcta.
                                if (questionsGame.get(i).getAnswers().get(var).isCorrect()){
                                    correctAnswer = questionsGame.get(i).getAnswers().get(var);
                                }
                        }

                        String answer = br.readLine();
                        int userAnswer = Integer.parseInt(answer);
                        //Para sincronizarlo con el ArrayList.
                        userAnswer--;
                        if(questionsGame.get(i).getAnswers().get(userAnswer).isCorrect()){
                            //Añadimos el acierto a la pregunta.
                            questionsGame.get(i).addCorrect();
                                updateSqlQuestion(questionsGame.get(i));

                            gameNow.addOneScore();
                            //Para evitar tocs y una 's' innecesaria.
                            if(gameNow.getScore() == 0) {
                                pw.println("Bien, has acertado, tienes 1 punto.");
                            }else{
                                pw.println("Bien, has acertado, tienes "+ gameNow.getScore() +" puntos.");
                            }
                        }else{
                            //Añadimos el fallo a la pregunta.
                            questionsGame.get(i).addFailure();
                                updateSqlQuestion(questionsGame.get(i));

                            if (gameNow.getScore() == 1) {
                                pw.println("Lo siento, has fallado, la respuesta correcta era " + correctAnswer.getAnswer() + ". Tienes " +gameNow.getScore()+ " punto.");
                            }else {
                                pw.println("Lo siento, has fallado, la respuesta correcta era " + correctAnswer.getAnswer() + ". Tienes " +gameNow.getScore()+ " puntos");
                            }
                        }

                        //Proceso de guardado de la partida.
                        if(i == 5){
                            //Comprobamos si es mayor que la puntuación máxima recogida.
                            if(playerSql.updateMaxScore(gameNow.getScore())){
                                playerSql.setMaxScore(gameNow.getScore());
                            }
                            //Guardamos datos en la sql.
                            createSqlGame(gameNow);
                            updatePlayer(playerSql);
                        }
                    }
                    pw.println("Juego finalizado. Tu puntuación es de " +gameNow.getScore()+ " puntos. Has jugado un total de " + playerSql.getGame().size()+ " partidas y tu puntuación más alta ha sido " +playerSql.getMaxScore()+ " .");

                    //Pregunta al usuario si quiere seguir jugando.
                    pw.println("¿Quieres jugar otra partida?");

                    while(!correctFormatAnswer) {
                        String playerDecision = br.readLine();
                        if (playerDecision.equals("si")) {
                            correctFormatAnswer = true;
                                pw.println("La próxima partida comenzara pronto...");
                                    pw.println("Bienvenido de nuevo");
                        }else if(playerDecision.equals("no")){
                            userWantsToPlay = false;
                                correctFormatAnswer = true;
                                    //Enviamos la orden a la variable del usuario.
                                        pw.println("false");
                                             pw.println("Adiooss...");
                                                gameInProcess = false;
                        }
                    }
                    correctFormatAnswer = false;
                }
            }
            socket.close();
        } catch (SocketException e) {
            System.err.println("El usuario ha sido desconectado.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        }
    }
}

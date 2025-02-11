package server;

import server.games.Game;
import server.games.Player;
import server.questions.Question;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static server.dao.GameDAO.*;
import static server.dao.PlayerDAO.createSqlPlayer;
import static server.dao.PlayerDAO.maxScore0SqlPlayer;
import static server.dao.QuestionDAO.findSqlEasyQuestions;
import static server.dao.QuestionDAO.findSqlHardQuestions;

/**
 * Menu de administrador del servidor de trivial en este
 * se pueden realizar varias acciones de gestión y consulta
 * de la base de datos, además de poder encender el servidor.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class Server {
    public static void main(String[] args) {
        boolean loop = true;
        boolean serverStarted = false;

        while (loop) {
            System.out.println("""
                         ╔═══════════════════|Menu Servidor|═════════════════════╗  \r
                         ║	                                        			 ║\r
                    	 ║	           1 Arrancar el servidor                    ║\r
                    	 ║	           2 Consultar histórico de jugadas	         ║\r
                    	 ║	           3 Consultar el top 10 de jugadas	         ║\r
                    	 ║	           4 Resetear resultados			  	     ║\r
                    	 ║	           5 Preguntas más difíciles y más fáciles   ║\r	
                    	 ║	           6 Crear un jugador       			     ║\r
                    	 ║	                                        			 ║\r
                    	 ║	                      7 Salir 			      	   	 ║\r
                         ╚═══════════════════════════════════════════════════════╝\r
                    """);
            Integer op = writeInteger();

            switch (op) {
                case 1:
                    if(!serverStarted){
                        ServerClient sv = new ServerClient(48120);
                            sv.start();
                                serverStarted = true;
                                    System.out.println("Servidor iniciado.");
                    }else{
                        System.out.println("El servidor ya esta iniciado.");
                    }
                    break;
                case 2:
                    List<Game> games = findAllGames();
                        if(!games.isEmpty()) {
                            showListGames(games);
                        }else{
                            System.out.println("No hay partidas registradas.");
                        }
                    break;
                case 3:
                        List<Game> topGames = findTop10Games();
                            if(!topGames.isEmpty()) {
                                showListGames(topGames);
                            }else{
                                System.out.println("No hay partidas registradas.");
                            }
                    break;
                case 4:
                    System.out.println("¿Estas seguro?, todos los datos almacenados se borraran.");
                        String confirmation = writeString();
                        if(confirmation.equals("si")){
                            int rows = AllDeleteSqlGame();
                                System.out.println("Eliminadas: "+rows+" partidas.");
                            int rowsPlayer = maxScore0SqlPlayer();
                                System.out.println("Max Score reseteado a: "+rowsPlayer+" jugadores.");
                        }else{
                            System.out.println("Operación cancelada, volviendo al menu principal...");
                        }
                    break;
                case 5 :
                        List<Question> hardQuestions = findSqlHardQuestions();
                        List<Question> easyQuestions = findSqlEasyQuestions();
                            if(!hardQuestions.isEmpty() && !easyQuestions.isEmpty()) {
                                showListTwoQuestions(hardQuestions, easyQuestions);
                            }else{
                                System.out.println("No hay las suficientes preguntas registradas.");
                            }
                    break;
                case 6:
                        createUser();
                    break;
                case 7:
                    loop = false;
                    break;
                case null:
                    System.out.println("Introduzca una opción correcta.");
                    break;
                default:
                    System.out.println("Seleccione una opción valida.");
            }
        }
        System.out.println("Programa finalizado.");
    }

    /**
     * Este método se utiliza cuando al usuario se le pide una entrada de un dato entero
     * debido a que se puede producir una excepción, si el usuario introduce otro dato
     * se gestiona la excepción, se informa al usuario de que introduzca un dato correcto.
     *
     * @return En caso de que el dato no sea numérico se devolverá un null.
     */
    private static Integer writeInteger(){
        Scanner num = new Scanner(System.in);
        try{
            return num.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Introduzca un dato correcto.");
            return null;
        }
    }

    /**
     * Para evitar la repetición de código cada vez que el usuario tenga
     * que introducir una cadena de caracteres se llama a este método.
     * @return devuelve la cadena de caracteres introducida por el usuario.
     */
    private static String writeString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().toLowerCase();
    }

    /**
     *  Método usado para que el usuario introduzca los datos
     *  del nuevo jugador a registrar, una vez introducidos los datos
     *  se llama a la consulta sql para registrar al jugador en la
     *  base de datos.
     */
    private static void createUser(){
        List<Game> gamePlayer = new ArrayList<>();
        System.out.println("Introduzca el usuario: ");
            String name = writeString();
                System.out.println("Introduzca la contraseña: ");
                    String pass = writeString();
                        createSqlPlayer(new Player(name,pass,LocalDate.now(),0,gamePlayer));
                            System.out.println("Usuario creado correctamente.");
    }

    /**
     * Usado para mostrar por consola una lista de juegos,
     * en este caso se utiliza para mostrár todas las partidas
     * de la base de datos y también para el top 10.
     *
     * @param list La lista que contiene las entidades y es mostrada.
     */
    private static void showListGames(List<Game> list){
        for(Game lista : list){
            System.out.println(lista);
        }
    }

    /**
     * Método usado para mostrar las dos listas de la opción 5
     * primero se muestran las preguntas mas difíciles y después
     * las mas fáciles.
     *
     * @param hardQuestions Lista que contiene las preguntas más difíciles.
     * @param easyQuestions Lista que contiene las preguntas más fáciles.
     */
    private static void showListTwoQuestions(List<Question> hardQuestions,List<Question> easyQuestions){
        System.out.println("Preguntas mas difíciles:");
        for(Question question : hardQuestions){
            System.out.println(question);
        }
        System.out.println("Preguntas mas fáciles:");
        for(Question question2 : easyQuestions){
            System.out.println(question2);
        }
    }
}

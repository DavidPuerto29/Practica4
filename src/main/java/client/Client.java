package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Programa que usa el cliente para conectarse a nuestro juego,
 * cuenta con las implementaciónes necesarias para realizar
 * las acciones de entrada/salida correctamente y sincronizadamente.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class Client {
    public static void main(String[] args) {
        int port = 48120;
        boolean userAutentication = false;
        boolean userWantsToPlay = true;
        boolean correctFormatAnswer;
        boolean correctFormatContestation = false;
        int counTryToAcces = 0;

        try {
            Socket socket = new Socket("localhost", port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Autenticación de usuario
            while(!userAutentication) {
                System.out.println(in.readLine());

                String name = writeString();
                    out.println(name);

                System.out.println(in.readLine());

                String pass = writeString();
                    out.println(pass);

                String messageForComprobation = in.readLine();
                    userAutentication = Boolean.parseBoolean(messageForComprobation);

                //Para recibir el código correctamente.
                String loggin = in.readLine();
                    if(loggin.equals("codigo 10")){
                        System.out.println("Bienvenido al trivial.");
                    }

                //Contador para solo permitir 3 intentos de acceso.
                counTryToAcces++;
                if(counTryToAcces == 3 && !userAutentication){
                    String error = in.readLine();
                        if (error.equals("codigo 11")){
                            System.out.println("Se ha superado el limite de intentos, inténtelo mas tarde.");
                                //Se realizan estos cambios para finalizar el programa sin excepciones.
                                    userWantsToPlay = false;
                                        break;
                        }
                }
            }

            //Preguntas al usuario y respuestas.
            while(userWantsToPlay) {

                //Usuario en partida.
                for (int var = 1; var <= 6; ++var) {
                    //Reiniciamos la variable
                    correctFormatAnswer = false;
                    correctFormatContestation = false;

                    //Impresión de contador de la pregunta, y sus respuestas.
                    System.out.println("╔═════════════╗");
                    System.out.println("║ "+in.readLine()+"  ║");
                    System.out.println("╚═════════════╝");
                    System.out.println(in.readLine());
                    for (int i = 1; i <= 4; ++i) {
                        System.out.println(i+"º "+in.readLine());
                    }

                    //Para evitar que se envíe una cadena de texto vacía y se genere una excepción.
                    while (!correctFormatAnswer) {
                        String respuesta = writeString();
                        if (respuesta.equals("1") || respuesta.equals("2") || respuesta.equals("3") || respuesta.equals("4")) {
                            out.println(respuesta);
                                correctFormatAnswer = true;
                        } else {
                            System.out.println("Introduzca una respuesta del 1 al 4.");
                        }
                    }
                    System.out.println(in.readLine());
                }
                System.out.println(in.readLine());

                //Se le pregunta al usuario si quiere otra partida.
                System.out.println(in.readLine());
                while(!correctFormatContestation) {
                    String playerDecision = writeString();
                        if (playerDecision.equals("si")) {
                            correctFormatContestation = true;
                                out.println(playerDecision);
                                    System.out.println(in.readLine());
                                        System.out.println(in.readLine());
                        }else if(playerDecision.equals("no")){
                            correctFormatContestation = true;
                                out.println(playerDecision);
                                    String messageForGameComprobation = in.readLine();
                                        userWantsToPlay = Boolean.parseBoolean(messageForGameComprobation);
                                            System.out.println(in.readLine());
                        }else{
                            System.out.println("Introduzca una opción correcta.");
                        }
                }
            }
            socket.close();
        } catch (ConnectException | UnknownHostException e) {
            System.err.println("Error no ha sido posible conectar al servidor.");
        } catch (IOException e) {
            System.err.println("Ha habido un error de entrada/salida.");
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

}

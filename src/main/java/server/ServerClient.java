package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase encargada de esperar conexiones de clientes
 * y llamar a la clase ClientGame cuando se detecta
 * a algún usuario.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
public class ServerClient extends Thread{
    private final int port;

    public ServerClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                Socket s = serverSocket.accept();
                    new ClientGame(s).start();
            }
        } catch (IOException e) {
            System.err.println("Error al crear una instancia.");
        }
    }
}

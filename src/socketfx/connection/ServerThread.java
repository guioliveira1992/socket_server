/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfx.connection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import socketfx.FXMLInitController;
import socketfx.MainSocketFx;

/**
 *
 * @author Guilherme
 */
public class ServerThread extends Thread {

    private final int port;
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;
    private DataOutputStream out = null;
    private static InputStream in = null;
    private InputStream inp = null;
    private BufferedReader brinp = null;
    private static AtomicBoolean running = new AtomicBoolean(false);
    public static List<Socket> socketClients = new ArrayList<Socket>();

    ObservableList<String> obsUsuario;

    public ServerThread(int port) {
        this.port = port;
    }

    /**
     * Método que inicia a Thread
     */
    public void run() {
        running.set(true);

        getInstanceThread();

        
    }

    /**
     * Método que abre a conexão com o server socket e aguarda novas conexões
     * 
     */
    public synchronized void getInstanceThread() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server OK");
        } catch (IOException e) {
            e.printStackTrace();

        }

        
        while (this.running.get()) {
                try {
                    socket = serverSocket.accept();

                    in = new DataInputStream(socket.getInputStream());

                    this.socketClients.add(socket);

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

                // inicia a thread que faz a comunicação de I/O com cliente da comunicação
                new ComunnicationThread(socket).start();

        }
    }

    /**
     * @throws IOException 
     * Método que envia o objeto socket e DataInputStream para receber os dados enviados pelo cliente
     */
    public synchronized void receiveString() throws IOException {
        new ComunnicationThread(socket).receive(in);
    }

   /**
    * Método que finaliza a comuniçao da aplicação server
    */
    public synchronized void fecharSocket() {

        if (socketClients.size() > 0) {
            for (int ii = 0; ii < socketClients.size(); ii++) {
                try {
                    in.close();
                    socketClients.get(ii).shutdownInput();
                    socketClients.get(ii).shutdownOutput();
                    socketClients.get(ii).close();

                    socketClients.remove(ii);
                } catch (IOException ex) {
                    System.out.println("erro ao fechar socket");
                    //Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                System.out.println("erro ao fechat server");
                //Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        running.set(false);

        
    }

}

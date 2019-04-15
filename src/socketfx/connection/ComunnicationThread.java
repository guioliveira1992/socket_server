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
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import socketfx.FXMLInitController;
import socketfx.MainSocketFx;
import socketfx.file.Arquivo;

/**
 *
 * @author Guilherme
 */
class ComunnicationThread extends Thread {

    private InputStream inp = null;
    private BufferedReader brinp = null;
    private static Socket socket = null;
    private static AtomicBoolean running = new AtomicBoolean(false);
    public static List<String> inetAddressList = new ArrayList<String>();

    public static Map<String, String> socketMap = new HashMap<String, String>();

    ObservableList<String> obsUsuario;

    public ComunnicationThread(Socket clientSocket) {

        if (clientSocket != null) {
            this.socket = clientSocket;

        }

    }

    /**
     * Método que inicia a Thread 
     */
    public void run() {
        try {
            if (socket != null) {
                receive(inp);
            }
        } catch (IOException ex) {
            Logger.getLogger(ComunnicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param in
     * Método que recebe dos dados enviados pelos usuários através da comunicação socket
     * @throws IOException 
     */
    public synchronized void receive(InputStream in) throws IOException {
        if (socket != null) {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            String stringIn = brinp.readLine();

            String textoSplit[] = stringIn.split(" ");
            inetAddressList.add(textoSplit[2]);
            socketMap.put(socket.getInetAddress().toString(), textoSplit[2]);
            FXMLInitController.setSocket(inetAddressList);

            if (stringIn != null && !stringIn.isEmpty()) {
                List<String> linhaList = new ArrayList<String>();

                Arquivo arquivo = new Arquivo();
                linhaList = arquivo.lerArquivo(textoSplit[2]);
                linhaList.add(stringIn);
                arquivo.gravarArquivo(linhaList, textoSplit[2]);
            }

        }
    }

    /**
     * Método que finaliza a comuniçao da aplicação server.
     * Obs.: Este método é só para garantir a integridade do software
     */
    public void fecharSocket() {

        try {
            if (inp != null) {
                inp.close();
            }
            if (inp != null) {
                brinp.close();
            }

        } catch (IOException ex) {
            System.out.println("erro ao fechar socket!");
        }

        this.running.set(false);

    }

}

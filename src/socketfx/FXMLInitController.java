/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfx;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socketfx.connection.ServerThread;
import socketfx.file.Arquivo;

/**
 *
 * @author Guilherme
 */
public class FXMLInitController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button btnFecharSocket;

    @FXML
    private TextField txPorta;

    @FXML
    private Label lbPorta;

    @FXML
    public ListView<String> lvUsuario;

    @FXML
    public ListView<String> lvMensagem;

    @FXML
    private Button btAtualizarUsuario;

    @FXML
    private Button btMensagemUsuario;

    @FXML
    private Label lbStatusConexao;

    public static List<String> usuarioList = new ArrayList<String>();

    static ObservableList<String> obsUsuario;
    static ObservableList<String> obsMensagem;

    private static List<Socket> socketList = new ArrayList<Socket>();

    /**
     * @param event Método que realiza a ação do botão para abrir/iniciar
     * comunicação server socket
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (txPorta != null && !txPorta.getText().isEmpty()) {
            System.out.println("You clicked me!");
            lbStatusConexao.setText("CONEXÃO ATIVA!");
            new ServerThread(Integer.parseInt(txPorta.getText())).start();

        }
    }

    /**
     *
     * @param event Método que realiza a ação do botão para finalizar
     * comunicação server socket
     */
    @FXML
    private void fecharSocket(ActionEvent event) {
        if (txPorta != null && !txPorta.getText().isEmpty()) {
            System.out.println("You clicked me!");
            lbStatusConexao.setText("CONEXÃO FINALIZADA!");

            new ServerThread(Integer.parseInt(txPorta.getText())).fecharSocket();

        }
    }

    /**
     *
     * @param event Método que realiza a ação do botão para atualizar os
     * usuários online na comunicação socket
     */
    @FXML
    private void atualizarUsuario(ActionEvent event) {
        if (obsUsuario != null && !obsUsuario.isEmpty()) {

            lvUsuario.setItems(obsUsuario);

        }
    }

    /**
     *
     * @param event Método que realiza a ação do botão para ler o arquivo de
     * mensagens enviadas pelos usuários através da comunicação socket
     */
    @FXML
    private void lerMensagemUsuario(ActionEvent event) {
        if (obsUsuario != null && !obsUsuario.isEmpty()) {
            String nomeUsuario = lvUsuario.getSelectionModel().getSelectedItem();
            Arquivo arquivo = new Arquivo();
            List<String> mensagemView = new ArrayList<String>();
            String mensagem = new String();
            List<String> linhaList = arquivo.lerArquivo(nomeUsuario);
            for (String linha : linhaList) {
                String vLinha[] = linha.split(" ");
                for (int i = 0; i < vLinha.length; i++) {
                    if (i != 2) {
                        mensagem += vLinha[i] += " ";
                    } else {
                        mensagem += " - ";
                    }
                }
                mensagem += "\n";
                mensagemView.add(mensagem);
            }

            obsMensagem = FXCollections.observableArrayList(mensagemView);
            lvMensagem.setItems(obsMensagem);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public static void setSocket(List<String> usuarios) {

        usuarioList = usuarios;

        obsUsuario = FXCollections.observableArrayList(usuarios);

    }

}

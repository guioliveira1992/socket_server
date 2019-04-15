/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketfx.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import ldsutils.TXTPersist;

/**
 *
 * @author soft7
 */
public class Arquivo {

    /**
     * 
     * @param nomeArquivo
     * Método que realiza a leitura do arquivo onde estão armazenadas as mensagens do clientes 
     * @return 
     */
    public List<String> lerArquivo(String nomeArquivo) {
       
        List<String> linhaList = new ArrayList<String>();
        try {
            linhaList = TXTPersist.readFromFile(".\\file\\"+nomeArquivo+".txt");
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Erro ao ler arquivo", "Erro", 2);
        }
        return linhaList;
    }
    
    /**
     * @param linhaList
     * Método que realiza a gavação das mensagens dos clientes no arquivo
     * @param nomeArquivo 
     */
    public void gravarArquivo(List<String> linhaList, String nomeArquivo){
        
        try {
            TXTPersist.saveToFile(linhaList, ".\\file\\"+nomeArquivo+".txt");
            JOptionPane.showMessageDialog(null, "Mensagem Salva com sucesso!", "Sucesso", 2);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gravar arquivo", "Erro", 2);
        }
    }

}

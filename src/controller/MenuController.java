package controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.plaf.basic.BasicLabelUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Backup;
import model.exception.BackupException;

public class MenuController implements Initializable{
    @FXML
    private TextField sourceField;

    @FXML
    private TextField destinyField;

    @FXML
    private TextField ignoreField;

    @FXML
    private Button btnStart;

    @FXML
    private TextArea displayConsole;
    
    Thread backupRun = new Thread() {
        @Override
        public void run() {
            runBackup();
        };
    };

    @FXML
    public void OnBtnStartAction(){
            if (backupRun.isAlive()) {
                System.out.println("Already running...");
                return;
            } 
            backupRun.start();
            return;
    }
    private void redirectSystemStreams() {

        // Define o PrintStream personalizado para logs
        /*
         * O que está acontecendo nesse código?
         * o PrintStream é responsável por escrever no console o log
         * O que estamos fazendo é sobreescrevendo o métodos e coloco ele dentro do textarea
        */
        PrintStream logStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                displayConsole.appendText(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                displayConsole.appendText(new String(b, off, len));
            }
        });

        System.setOut(logStream);
        System.setErr(logStream);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redirectSystemStreams();
    }

    private void runBackup (){
        String sourceStringURL = sourceField.getText();
        String destinyStringURL = destinyField.getText();
        String ignoreStringURL = ignoreField.getText();
        try {
            Backup.run(sourceStringURL, destinyStringURL, ignoreStringURL);
            System.out.println("...");
        } catch (BackupException e) {
            e.printStackTrace();
        }
    }
}

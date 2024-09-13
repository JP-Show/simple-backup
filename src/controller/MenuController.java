package controller;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Backup;
import model.BufferWriter;
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
    private Button btnPause;

    @FXML
    private TextArea displayConsole;

    private boolean isBackupRunning = false;

    private BufferWriter bufferWriter;

    private Backup backupTask;

    @FXML
    public void OnBtnStartAction(){
            if (isBackupRunning) {
                System.out.println("Already running...");
                return;
            }
            String sourceStringURL = sourceField.getText();
            String destinyStringURL = destinyField.getText();
            String ignoreStringURL = ignoreField.getText();
            
            //Here, we need to create a new Thread every backup, because if we reutilize same Thread, we get a IllegalThreadStateException
            
            // backupTask = new Thread() {
            //     @Override
            //     public void run() {
            //         try {
            //             Backup.run(sourceStringURL, destinyStringURL, ignoreStringURL);
            //             System.out.println("Backup Concluido com sucesso");
            //         } catch (BackupException e) {
            //             System.err.printf("\nError: "+e.getMessage() + "\n" + "Cause: " + e.getCause());
            //         }finally{
            //             isBackupRunning = false;
            //         }
            //         interrupt();
            //     }
            // };

            backupTask = new Backup(sourceStringURL, destinyStringURL, ignoreStringURL){
                @Override
                public void run() {
                    super.run();
                    isBackupRunning = false;
                }
            };
            backupTask.start();

            isBackupRunning = true;
            
            return;
    }
    
    @FXML
    public void OnBtnPauseAction(){
        if(isBackupRunning){
            backupTask.pause();
            isBackupRunning = false;
        } else {
            backupTask.unPause();
            isBackupRunning = true;
        }
    }

    @FXML
    public void OnBtnStopAction(){
        if(backupTask.isAlive()){
            backupTask.finish();
            isBackupRunning = false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayConsole.setEditable(false);
        bufferWriter = new BufferWriter(displayConsole);
        bufferWriter.start();
        redirectSystemStreams();
        System.out.println("Ready");
    }

    private void redirectSystemStreams() {
        // Define o PrintStream personalizado para logs
        /*
         * O que está acontecendo nesse código?
         * o PrintStream é responsável por escrever no console o log
         * O que estamos fazendo é sobreescrevendo o métodos e coloco ele dentro do textarea
        */
        PrintStream logStream = new PrintStream(new OutputStream() {
            private StringBuilder buffer = new StringBuilder();
            @Override
            public void write(int b) {
                bufferWriter.addToBufffer(String.valueOf(b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String str = new String(b, off, len);
                bufferWriter.addToBufffer(str);
            }
        });
        System.setOut(logStream);
        System.setErr(logStream);
    }

}

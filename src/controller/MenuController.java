package controller;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Backup;
import model.BufferWriter;

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
    @FXML
    private Text status;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button btnSendToList;
    @FXML
    private VBox vboxIgnore;

    private boolean isBackupRunning = false;
    private boolean isPaused = false;

    private BufferWriter bufferWriter;

    private Backup backupTask;
    @FXML
    private final Set<Text> listIgnore = new HashSet<>();

    @FXML
    public void OnBtnStartAction(){
            if (isBackupRunning) {
                System.out.println("Already running...");
                return;
            }
            bufferWriter.cleanerBuffer();
            String sourceStringURL = sourceField.getText();
            String destinyStringURL = destinyField.getText();
            String ignoreStringURL = ignoreField.getText();
            
            //Here, we need to create a new Thread every backup, because if we reutilize same Thread, we get a IllegalThreadStateException
            backupTask = new Backup(sourceStringURL, destinyStringURL, ignoreStringURL, progressBar){
                @Override
                public void run() {
                    super.run();
                    status.setText("Done");
                    isBackupRunning = false;
                    System.gc();
                }
            };
            backupTask.start();
            isBackupRunning = true;
            status.setText("Running");
            return;

    }
    
    @FXML
    public void OnBtnPauseAction(){
        if(isBackupRunning && !isPaused){
            status.setText("Paused");
            backupTask.pause();
            isPaused = true;
        } else if (isPaused){
            backupTask.unPause();
            isPaused = false;
            status.setText("Running");
        }
    }

    @FXML
    public void OnBtnStopAction(){
        if(backupTask.isAlive()){
            status.setText("Stopped");
            backupTask.finish();
            isBackupRunning = false;
        }
    }

    @FXML
    public void OnbtnSendToListAction(){
        String src = ignoreField.getText();
        Text ignoreText = new Text(src);
        ignoreField.setText("");
        ignoreText.setOnMouseClicked(event -> {
            listIgnore.remove(ignoreText);
            vboxIgnore.getChildren().remove(ignoreText);
        });
        vboxIgnore.getChildren().add(ignoreText);
        listIgnore.add(ignoreText);
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

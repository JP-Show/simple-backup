package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Backup;

public class MenuController{
    @FXML
    private TextField sourceField;

    @FXML
    private TextField destinyField;

    @FXML
    private TextField ignoreField;

    @FXML
    private Button btnStart;
    
    @FXML
    public void OnBtnStartAction(){
        
        try {
            String sourceStringURL = sourceField.getText();
            String destinyStringURL = destinyField.getText();
            String ignoreStringURL = ignoreField.getText();
            Backup.run(sourceStringURL, destinyStringURL, ignoreStringURL);
            
        } catch (Exception e) {
            // TODO: handle exception
        }
       
        
    }
}

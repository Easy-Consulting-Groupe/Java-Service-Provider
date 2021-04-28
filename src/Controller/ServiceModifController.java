/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Utils.dbConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Nour
 */
public class ServiceModifController implements Initializable {

    @FXML
    private Button btn0;
    @FXML
    private TextField tftitle;
    @FXML
    private TextField tfdec;
    @FXML
    private TextField tfpricee;
    @FXML
    private Button btn1;
    @FXML
    private Label idup;
    private dbConnection dc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dc = new dbConnection();
    }    

    @FXML
    private void Back(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/servicepovidermain/ServiceShow.fxml"));
            Parent root = loader.load();
            //ServiceShowController irc = loader.getController();
            btn0.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(ServiceShowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void modfierService(ActionEvent event) {
        try{
            Connection conn =dc.getConnection();
    String requete="update service set title = ?,descrption = ?,price = ? where id="+idup.getText()+"' ";
            PreparedStatement pst;
            pst = conn.prepareStatement(requete);
             pst.setString(1, tftitle.getText());
            pst.setString(2, tfdec.getText());
            pst.setInt(3, Integer.valueOf(tfpricee.getText()));
            
            
            pst.executeUpdate();
   
        } catch (SQLException ex) {
            Logger.getLogger(ServiceModifController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void setTitleUp(String tftitle) {
        this.tftitle.setText(tftitle);
    }
    public void setDecrUp(String tfdec) {
        this.tfdec.setText(tfdec);
    }
    public void setPriceUp(String tfpricee) {
        this.tfpricee.setText(tfpricee);
    }
    public void setIdUp(String idup) {
        this.idup.setText(idup);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esprit.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import esprit.entities.Comment;
import esprit.entities.Forum;
import esprit.entities.Post;
import esprit.services.CommentCRUD;
import esprit.services.CurseFilterService;
import esprit.services.ForumCRUD;
import esprit.services.PostCRUD;
import esprit.tools.MyConnection;
import java.sql.Connection;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class DetailForumController implements Initializable {

    @FXML
    private Label restTitleForum;
    @FXML
    private Label restDescriptionFourm;
    @FXML
    public TableView<Post> tablePost;
    @FXML
    private TableColumn<Post, String> col_title;
    @FXML
    private TableColumn<Post, String> col_description;
    @FXML
    private TableColumn<Post, Integer> col_views;
    @FXML
    private TableColumn<Post, Integer> col_noc;
  
    ObservableList<Post> oblistpost = FXCollections.observableArrayList();
    ForumCRUD pf = new ForumCRUD();
    PostCRUD pc = new PostCRUD();
    CommentCRUD cc = new CommentCRUD();
    @FXML
    private Label idF;

    @FXML
    private TextField tfTitlePost;
    @FXML
    private HTMLEditor tfDescriptionPost;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnmodif;
    @FXML
    private Button btnDetailsPost;
    @FXML
    private Label idP;
    Stage stage;
    @FXML
    private Button btnR;

    Forum forumTest;
    @FXML
    private Button profile;
    @FXML
    private ImageView btnprofile;
    @FXML
    private Label userlabel;
    @FXML
    private ImageView logout;
    private Label entetForum;
    @FXML
    private Label Tforum;
    @FXML
    private Label tPost;
    private PieChart statPost;
    ObservableList <PieChart.Data> statPostdata;
       private Connection con;
    @FXML
    private Button btnClear;
    /**
     * Initializes the controller class.
     */
     public DetailForumController()
    {
    con = MyConnection.getInstance().getCnx();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idF.setVisible(false);
        idP.setVisible(false);
    }

    private void clearAll() {
        idP.setText("");
        tfTitlePost.setText("");
        tfDescriptionPost.setHtmlText("");

    }

    public void setTfTitlePost(String tfTitlePost) {
        this.tfTitlePost.setText(tfTitlePost);
    }

    public void setTfDescriptionPost(String tfDescriptionPost) {
        this.tfDescriptionPost.setHtmlText(tfDescriptionPost);
    }

    public void setIdP(String idP) {
        this.idP.setText(idP);
    }

    public void setRestDescriptionFourm(String restDescriptionFourm) {
        this.restDescriptionFourm.setText(restDescriptionFourm);
    }

    public void setTforum(String Tforum) {
        this.Tforum.setText(Tforum);
    }

    public void settPost(String tPost) {
        this.tPost.setText(tPost);
    }

    public void setRestTitleForum(String restTitleForum) {
        this.restTitleForum.setText(restTitleForum);
    }

    public void setRestIdFourm(String idF) {
        this.idF.setText(idF);
    }

    private void initTable(int id) {
        try {
            /*String idfs = idtest.getText();
            Integer idf = Integer.valueOf(idfs);*/

            oblistpost = (ObservableList<Post>) pc.readAllpost2(id);
            col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_noc.setCellValueFactory(new PropertyValueFactory<>("noc"));
            col_views.setCellValueFactory(new PropertyValueFactory<>("views"));
            
            tablePost.setItems(oblistpost);
        } catch (SQLException ex) {
            Logger.getLogger(DetailForumController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initTable(ObservableList<Post> posts) {
        // String idfs = idtest.getText();
        // Integer idf = Integer.valueOf(idfs);
        //oblistpost = (ObservableList<Post>) pc.readAllpost2(idf);
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_noc.setCellValueFactory(new PropertyValueFactory<>("noc"));
        
        col_views.setCellValueFactory(new PropertyValueFactory<>("views"));
        tablePost.setItems(posts);

    }

    @FXML
    private void ajouter(ActionEvent event) {

        PostCRUD pc = new PostCRUD();
        Post p = new Post();
        String tTitle = CurseFilterService.cleanText(tfTitlePost.getText());
        String tDescription = CurseFilterService.cleanText(tfDescriptionPost.getHtmlText());
        p.setTitle(tTitle);
        p.setDescription(tDescription);
        Integer i = Integer.valueOf(idF.getText());
        p.setIdF(i);
        pc.addPost(p, i);
     
        clearAll();
        initTable(i);
       System.out.println( p.getDate());
       
       Image img = new Image("/esprit/img/ok.png");
            Notifications notifAdd = Notifications.create()
                    .title("add complet")
                    .text("saved avec sucees")
                    .graphic(new ImageView(img))
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT);
            notifAdd.show();

    }

    @FXML
    private void select(MouseEvent event) {
        Post P = tablePost.getSelectionModel().getSelectedItem();
        tfTitlePost.setText(P.getTitle());
        tfDescriptionPost.setHtmlText(P.getDescription());
        Integer idPost = P.getId();
        idP.setText(idPost.toString());
        String id = Integer.toString(P.getId());
        String tpost = tfTitlePost.getText();
        tPost.setText(">" + tpost);
    }

    @FXML
    private void delete(ActionEvent event) throws SQLException {
        Integer i = Integer.valueOf(idF.getText());
        Post dis = tablePost.getSelectionModel().getSelectedItem();
        if (!dis.equals(null)) {
            pc.delete(dis.getId());
            initTable(i);
            clearAll();
        }
    }

    @FXML
    private void update(ActionEvent event) throws SQLException {
        Integer i = Integer.valueOf(idF.getText());
        Post P = tablePost.getSelectionModel().getSelectedItem();
        if (P == null) {
            JOptionPane.showMessageDialog(null, "There is nothing selected !");
        } else {

            pc.update(P.getId(), CurseFilterService.cleanText(tfTitlePost.getText()), CurseFilterService.cleanText(tfDescriptionPost.getHtmlText()));
            initTable(i);
            clearAll();

        }

    }

    @FXML
    private void DetailsPost(ActionEvent event) throws SQLException {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/esprit/view/DetailPost.fxml"));
            Parent root = loader.load();
            String v = idP.getText();
            int  idPclicked = Integer.valueOf(v);
            DetailPostController dp = loader.getController();
            dp.setDescriptionP(tfDescriptionPost.getHtmlText());
            dp.setTitleP(tfTitlePost.getText());
            dp.setIdPc(idP.getText());
            dp.setIdForum(idF.getText());
            dp.setTitleForum(restTitleForum.getText());
            dp.setDescriptionFourm(restDescriptionFourm.getText());
            dp.setTF(restTitleForum.getText());
            dp.setTP(">" + tfTitlePost.getText());
                pc.updatePostview(idPclicked);
                initTable(idPclicked);
            try {
                dp.initTable((ObservableList<Comment>) cc.readAllcomment2(Integer.parseInt(idP.getText())));
                // image(dp);
               
                
            } catch (SQLException ex) {
                Logger.getLogger(ForumController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tfTitlePost.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(DetailPostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void Return(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/esprit/view/Forum.fxml"));
            Parent root = loader.load();

            ForumController df = loader.getController();
            df.setTfDescriptionForum(restDescriptionFourm.getText());
            df.setTfTitleForum(restTitleForum.getText());
            df.setTfIdForum(idF.getText());
            df.setTitleF(restTitleForum.getText());
            tfTitlePost.getScene().setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(ForumController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void exit(MouseEvent event) {
        stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Clear(ActionEvent event) {
         idP.setText("");
        tfTitlePost.setText("");
        tfDescriptionPost.setHtmlText("");
    }

  

   

}

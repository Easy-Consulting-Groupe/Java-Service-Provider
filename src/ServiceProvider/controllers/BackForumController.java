/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiceProvider.controllers;

import Entities.Comment;
import Entities.Forum;
import Entities.Post;
import Entities.User;
import Services.CommentCRUD;
import Services.ForumCRUD;
import Services.PostCRUD;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Utils.dbConnection;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class BackForumController implements Initializable {

    @FXML
    private TableView<Forum> tableFourm;
    @FXML
    private TableColumn<Forum, String> col_title;
    @FXML
    private TableColumn<Forum, String> col_dec;
    @FXML
    private TableColumn<Forum, Integer> col_id;

    ObservableList<Forum> oblistdisc = FXCollections.observableArrayList();
    ObservableList<Post> oblistpost = FXCollections.observableArrayList();
    ObservableList<Comment> oblistcomment = FXCollections.observableArrayList();

    PostCRUD pc = new PostCRUD();
    CommentCRUD cc = new CommentCRUD();
    ForumCRUD fc = new ForumCRUD();
    @FXML
    private TableView<Post> tablePost;
    @FXML
    private TableColumn<Post, Integer> col_idP;
    @FXML
    private TableColumn<Post, String> col_titleP;
    @FXML
    private TableColumn<Post, String> col_description;
    @FXML
    private TableColumn<Post, Integer> col_views;
    @FXML
    private TableColumn<Post, Integer> col_noc;

    @FXML
    private TableView<Comment> tableComment;
    @FXML
    private TableColumn<Comment, Integer> col_idC;
    @FXML
    private TableColumn<Comment, String> col_content;
    @FXML
    private TableColumn<Comment, Integer> col_rating;
    @FXML
    private PieChart statPostviews;
    ObservableList<PieChart.Data> statPostdata;
   private Connection con;
    @FXML
    private PieChart statPostnoc;
    @FXML
    private Button profile;
    @FXML
    private ImageView btnprofile;
    @FXML
    private Button btnforum;
    @FXML
    private Button btnDashboard;
    @FXML
    private Label Forum;
    @FXML
    private Button PDF;
    User user = null;
    
   public BackForumController()
    {
    con = dbConnection.getInstance().getConnection();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTableF();
    }
    public void setUser(User u) {
        this.user = u;
        System.out.println(user.getNom());
    }

    public void loadData2(int idd) {
        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();
        String query2 = "SELECT views FROM post WHERE frm_id ='" + idd + "';";
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery(query2);
            while (rs.next()) {
                pieChartData2.add(new PieChart.Data(rs.getString(1), rs.getInt(1)));

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        statPostviews.setData(pieChartData2);
        statPostviews.setStartAngle(90);
        for (final PieChart.Data data : statPostviews.getData()) {
            data.nameProperty().set((int) data.getPieValue() + " views ");
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //   JOptionPane.showMessageDialog((int)data.getPieValue()+"post"+data.getName(),"information");
                }
            });

        }

    }
 public void loadData1(int idd) {
        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();
        String query2 = "SELECT noc FROM post WHERE frm_id ='" + idd + "';";
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery(query2);
            while (rs.next()) {
                pieChartData2.add(new PieChart.Data(rs.getString(1), rs.getInt(1)));

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        statPostnoc.setData(pieChartData2);
        statPostnoc.setStartAngle(90);
        for (final PieChart.Data data : statPostnoc.getData()) {
            data.nameProperty().set((int) data.getPieValue() + " nombre of comment ");
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //   JOptionPane.showMessageDialog((int)data.getPieValue()+"post"+data.getName(),"information");
                }
            });

        }

    }
    private void initTableF() {

        try {
            oblistdisc = (ObservableList<Forum>) fc.readAlldiscc();
            col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_dec.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tableFourm.setItems(oblistdisc);

        } catch (SQLException ex) {
            Logger.getLogger(ForumController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAction(MouseEvent event) {
        Forum F = tableFourm.getSelectionModel().getSelectedItem();

        initTableP(F.getId());
        loadData2(F.getId());
        loadData1(F.getId());
    }

    private void initTableP(int id) {
        try {
            /*String idfs = idtest.getText();
            Integer idf = Integer.valueOf(idfs);*/

            oblistpost = (ObservableList<Post>) pc.readAllpost2(id);
            col_idP.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_titleP.setCellValueFactory(new PropertyValueFactory<>("title"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_noc.setCellValueFactory(new PropertyValueFactory<>("noc"));

            col_views.setCellValueFactory(new PropertyValueFactory<>("views"));
            tablePost.setItems(oblistpost);
        } catch (SQLException ex) {
            Logger.getLogger(DetailForumController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initTableC(int i) {
        try {
            oblistcomment = (ObservableList<Comment>) cc.readAllcomment2(i);
            col_idC.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_content.setCellValueFactory(new PropertyValueFactory<>("content"));
            col_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            tableComment.setItems(oblistcomment);
        } catch (SQLException ex) {
            Logger.getLogger(DetailPostController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void select(MouseEvent event) {
        Post P = tablePost.getSelectionModel().getSelectedItem();

        initTableC(P.getId());

    }

    @FXML
    private void ForumFront(ActionEvent event) {
        try {

            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/ServiceProvider/view/Forum.fxml"));
            Parent root = loader.load();

        ForumController dc = loader.getController();
            
          
            Forum.getScene().setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(ForumController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
@FXML
    private void PDF(ActionEvent event) {

        try {
            ForumCRUD es = new ForumCRUD();
            Forum panier = new Forum();
            //PanierService panierService = new PanierService();
            // panier = panierService.getCurrentPanierByUserID(Main.user_id);

            String file_name = "C:\\Users\\Mohamed Aziz Mhadhbi\\Desktop\\ForumPostCommentList.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            document.open();
            Paragraph p = new Paragraph("  ");
            Image img = Image.getInstance("C:\\Users\\Mohamed Aziz Mhadhbi\\Desktop\\1.png");

            img.scaleAbsolute(100, 100);
            img.setAbsolutePosition(450, 630);
            document.add(img);
//            
            document.add(p);
            document.add(p);
            document.add(p);
            //Paragraph par = new Paragraph("     La liste des Forum ");
            //Paragraph parr = new Paragraph("    _______________");
            //par.setAlignment(Element.ALIGN_CENTER);
           // parr.setAlignment(Element.ALIGN_CENTER);

            //document.add(par);
           // document.add(parr);
            document.add(p);
            document.add(p);
            document.add(p);

            Connection connexion = dbConnection.getInstance().getConnection();
            PreparedStatement psF = null;
            ResultSet rsF = null;

            PreparedStatement psP = null;
            ResultSet rsP = null;

            PreparedStatement psC = null;
            ResultSet rsC = null;

            String queryF = "select * from forum";

            psF = (PreparedStatement) connexion.prepareStatement(queryF);
            rsF = psF.executeQuery();
            int i = 1;
            int j = 1;
            int k = 1;
            ForumCRUD ser = new ForumCRUD();
            while (rsF.next()) {
                Paragraph p2 = new Paragraph("Fourm n°" + i);
                document.add(p2);

                Paragraph para = new Paragraph("Title Forum : " + rsF.getString("title") + " \n Description   :" + rsF.getString("description"));
                document.add(para);
                int idForum = rsF.getInt("id");
                document.add(new Paragraph("  "));

                String queryP = "select * from post where frm_id= '" + idForum + "';";
                psP = (PreparedStatement) connexion.prepareStatement(queryP);
                rsP = psP.executeQuery();
                while (rsP.next()) {
                    document.add(new Paragraph("                  "));
                    Paragraph p2P = new Paragraph("          Post n°" + j);
                    document.add(p2P);

                    Paragraph paraP = new Paragraph("          Title Post : " + rsP.getString("title") + "\n          Description   :" + rsP.getString("description")+"\n          Views : "+rsP.getInt("views")+"\n          Number of comments : "+rsP.getInt("noc")+"\n          Date : "+rsP.getDate("creat_at"));
                    document.add(paraP);
                    j++;
                    
                    document.add(new Paragraph("  "));
                    int idPost = rsP.getInt("id");
                    String queryC = "select * from comment where pst_id = '" + idPost + "';";
                    psC = (PreparedStatement) connexion.prepareStatement(queryC);
                    rsC = psC.executeQuery();
                    while (rsC.next()) {
                        Paragraph p2C = new Paragraph("                    Comment n°" + k);
                        document.add(p2C);

                        Paragraph paraC = new Paragraph("                    Content Comment : " + rsC.getString("content")+"\n                    Date :"+rsC.getDate("creat_at")+"\n                    Rating for the Post :"+rsC.getInt("rating"));
                        document.add(paraC);
                        
                        document.add(new Paragraph("  "));
                        k++;

                    }
                    k = 1;
                }
                j = 1;

                i++;
            }

            Paragraph parrr = new Paragraph("________________________________");
            parrr.setAlignment(Element.ALIGN_CENTER);
            document.add(parrr);

//            double a= ser.calcul_total(panier.getId());
//               Paragraph p1 = new Paragraph("Prix Total :  "+a + "  DT" ,redF);
//               
//               p1.setAlignment(Element.ALIGN_CENTER);
//               
//                document.add(p1);
            document.close();
            System.out.println("finished");
            
            Desktop.getDesktop().open(new File(file_name));
        } catch (Exception e) {
            System.err.println(e);
        }

    }
    
}

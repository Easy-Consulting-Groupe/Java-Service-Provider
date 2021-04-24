/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esprit.services;

import com.sun.xml.internal.messaging.saaj.soap.impl.DetailEntryImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import esprit.controller.DetailForumController;
import esprit.entities.Forum;
import esprit.entities.Post;
import esprit.tools.MyConnection;

/**
 *
 * @author ASUS
 */
public class PostCRUD {

    public List<Post> getPosts() {
        List<Post> myList = new ArrayList();
        try {
            String requete = "SELECT * FROM post";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Post f = new Post();
                f.setId(rs.getInt(1));
                f.setTitle(rs.getString("title"));
                f.setDescription(rs.getString("description"));
                f.setNoc(0);
                f.setViews(0);
                myList.add(f);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public ObservableList<Post> readAllpost(String idd) throws SQLException {

        ObservableList oblistpost = FXCollections.observableArrayList();
        Statement st = MyConnection.getInstance().getCnx().createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM `post` WHERE frm_id = '" + idd + "';");

        //System.out.println(idd);
        while (rs.next()) {

            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            int views = rs.getInt("views");
            int noc = rs.getInt("noc");
            String creat_at = rs.getString("creat_at");

            Post d = new Post(id, title, description, views, noc, creat_at);
            oblistpost.add(d);

        }
        return oblistpost;
    }

    public ObservableList<Post> readAllpost2(int idd) throws SQLException {

        ObservableList oblistpost = FXCollections.observableArrayList();
        Statement st = MyConnection.getInstance().getCnx().createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM `post` WHERE frm_id = '" + idd + "';");

        //System.out.println(idd);
        while (rs.next()) {

            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            int views = rs.getInt("views");
            int noc = rs.getInt("noc");
            String creat_at = rs.getString("creat_at");

            Post d = new Post(id, title, description, views, noc, creat_at);
            oblistpost.add(d);

        }
        return oblistpost;
    }

    public void addPost(Post p, int i) {
       
        try {
            String requete = "INSERT INTO post (usr_id,frm_id,title,description,views,noc,creat_at)"
                    + "VALUES (1,?,?,?,?,?,sysdate())";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setInt(1, i);
            pst.setString(2, p.getTitle());
            pst.setString(3, p.getDescription());
            pst.setInt(4, 0);
            pst.setInt(5, 0);
            
            pst.executeUpdate();
            System.out.println("Post added!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public boolean delete(int dd) throws SQLException {

        PreparedStatement pre = MyConnection.getInstance().getCnx().prepareStatement("DELETE FROM post WHERE id ='" + dd + "' ;");
        pre.executeUpdate();
        JOptionPane.showMessageDialog(null, "post supprimé avec succées");
        return true;
    }
public boolean updatePostview(int id) throws SQLException {
        PreparedStatement pre = MyConnection.getInstance().getCnx().prepareStatement("UPDATE post SET views= views+1   WHERE id='" + id + "' ;");
       // JOptionPane.showMessageDialog(null, "Post update avec succées");
        pre.executeUpdate();
        return true;
    }
    public boolean update(int id, String title, String description) throws SQLException {
        PreparedStatement pre = MyConnection.getInstance().getCnx().prepareStatement("UPDATE post SET title= '" + title + "' , description='" + description + "' WHERE id='" + id + "' ;");
        JOptionPane.showMessageDialog(null, "Post update avec succées");
        pre.executeUpdate();
        return true;
    }

    public ObservableList<Post> readAlldiscc() throws SQLException {

        ObservableList oblistdisc = FXCollections.observableArrayList();

        Statement st = MyConnection.getInstance().getCnx().createStatement();
        ResultSet rs = st.executeQuery("select * from  post ;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            int noc = rs.getInt("noc");
            int views = rs.getInt("views");

            Post d = new Post(id, title, description,noc,views);
            oblistdisc.add(d);

        }
        return oblistdisc;
    }
    
    public Post findById(int id){

        try {
            String request = "SELECT * FROM post where id = "+ id;
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            ResultSet rs = pst.executeQuery(request);
            Post post = new Post();
            while (rs.next()) {
             
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setDescription(rs.getString("description"));
                post.setViews(rs.getInt("views"));
                post.setNoc(rs.getInt("noc"));
                post.setDate(rs.getString("date"));
                post.setIdF(rs.getInt("idF"));

                    
                
            }
            return post;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
      public Double getAvgRating(int value) throws SQLException 
    {
        int count = 0;
        Double total = 0.0;
        ResultSet rs = MyConnection.getInstance().getCnx().createStatement().executeQuery("select * from  comment where pst_id = '" +value+"';");
            while (rs.next()) {
        total = total + rs.getDouble("rating");
        count ++;
                }
        if(count>0)return total/count;
        return 0.0;
    }

}

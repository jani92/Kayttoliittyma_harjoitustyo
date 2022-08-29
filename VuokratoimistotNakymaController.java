package vuokratoimistot;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Jani Rytkönen, Niko Ryynänen
 */
public class VuokratoimistotNakymaController implements Initializable {

    @FXML
    private Button btnNewWindow;

/**
 * Initialize, jossa luodaan SQL-taulut
 * @param url Url-osoite
 * @param rb Resource Bundle
 */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            createDatabase(conn, "r8_vuokratoimistot");

            createTable(conn,
                    "CREATE TABLE toimipiste ("
                    + "toimipisteID INT NOT NULL PRIMARY KEY,"
                    + "sijainti VARCHAR(25) NOT NULL,"
                    + "lahiosoite VARCHAR(50) NOT NULL,"
                    + "postitoimipaikka VARCHAR(25) NOT NULL,"
                    + "postinro VARCHAR(5) NOT NULL,"
                    + "email VARCHAR(50) NOT NULL,"
                    + "puhnro VARCHAR(20) NOT NULL)"
            );

            createTable(conn,
                    "CREATE TABLE palvelut ("
                    + "palveluID INT NOT NULL PRIMARY KEY,"
                    + "toimipisteID INT NOT NULL,"
                    + "palvelunimi VARCHAR(50) NOT NULL,"
                    + "palvelutyyppi VARCHAR(40) NOT NULL,"
                    + "palvelukuvaus VARCHAR(150) NOT NULL,"
                    + "hinta DOUBLE(8,2) NOT NULL)"
            );

            createTable(conn,
                    "CREATE TABLE asiakas ("
                    + "asiakasID INT NOT NULL PRIMARY KEY,"
                    + "etunimi VARCHAR(25) NOT NULL,"
                    + "sukunimi VARCHAR(30) NOT NULL,"
                    + "lahiosoite VARCHAR(50) NOT NULL,"
                    + "postitoimipaikka VARCHAR(25) NOT NULL,"
                    + "postinro VARCHAR(5) NOT NULL,"
                    + "email VARCHAR(50) NOT NULL,"
                    + "puhelinnro VARCHAR(20) NOT NULL)"
            );
            
            createTable(conn,
                    "CREATE TABLE varaukset ("
                    + "varausID INT NOT NULL PRIMARY KEY,"
                    + "asiakasID INT NOT NULL,"
                    + "toimipisteID INT NOT NULL,"
                    + "varattu DATE,"
                    + "vahvistus DATE,"
                    + "varattuALK DATE,"
                    + "varattuLOPP DATE,"
                    + "FOREIGN KEY (toimipisteID) REFERENCES toimipiste(toimipisteID) ON DELETE CASCADE,"
                    + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID) ON DELETE CASCADE)"
            );

            createTable(conn,
                    "CREATE TABLE laskut ("
                    + "laskuID INT NOT NULL PRIMARY KEY,"
                    + "varausID INT NOT NULL,"
                    + "asiakasID INT NOT NULL,"
                    + "etunimi VARCHAR(20) NOT NULL,"
                    + "sukunimi VARCHAR(30) NOT NULL,"
                    + "lahiosoite VARCHAR(50) NOT NULL,"
                    + "postitoimipaikka VARCHAR(50) NOT NULL,"
                    + "postinro VARCHAR(5) NOT NULL, "
                    + "email VARCHAR(50) NOT NULL,"                            
                    + "summa DOUBLE (8,2) NOT NULL,"
                    + "FOREIGN KEY (varausID) REFERENCES varaukset(varausID),"
                    + "FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID))"
            );

            createTable(conn,
                    "CREATE TABLE varauksen_palvelut ("
                    + "varausID INT NOT NULL PRIMARY KEY,"
                    + "palveluID INT NOT NULL,"
                    + "lkm INT NOT NULL,"
                    + "FOREIGN KEY (varausID) REFERENCES varaukset (varausID) ON DELETE CASCADE,"
                    + "FOREIGN KEY (palveluID) REFERENCES palvelut (palveluID) ON DELETE CASCADE)"
            );

            } catch (SQLException e) {
            System.out.println("Tapahtui virhe:" + e);
        }
    } 
    
    /**
   * Yhteyden avaus SQL-serveriin
   * @param connString Verkko oisoite merkkijonona
   * @return connString
   * @throws SQLException SQLException
   */
    private static Connection openConnection(String connString) throws SQLException {
        Connection con = DriverManager.getConnection(connString);
        System.out.println("\t>> Yhteys ok");
        return con;
    }
        
    /**
    * SQL-databasen luominen
    * @param c Yhteys
    * @param db Databasen nimi merkkijonona
    * @throws SQLException SQLException
    */
    private static void createDatabase(Connection c, String db) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery("DROP DATABASE IF EXISTS " + db);
        System.out.println("\t>> Tietokanta" + db + "tuhottu");

        stmt.executeQuery("CREATE DATABASE " + db);
        System.out.println("\t>> Tietokanta" + db + "luotu");

        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Kaytetaan tietokantaa " + db);
    }
    
    /**
    * SQL-taulukon luonti
    * @param c Yhteys
    * @param sql Statement
    * @throws SQLException SQLException
    */
    private static void createTable(Connection c, String sql) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.execute(sql);
        System.out.println("\t>> Taulu luotu");
    }

    /**
     * 
     * @param event Action handleri
     * @throws IOException virheentunnistus
     */
    @FXML    
    private void handleBtnNewWindow(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("RaportointiFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Raportointi");
        stage.setScene(scene);
        stage.show();
    }
    
    /**VaraustenhallintaFXMLcontroller luokan avaus painike
     * 
     * @param event Action handleri
     * @throws IOException virheentunnistus
     */
    @FXML
    private void openVaraustenhallinta(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("VaraustenhallintaFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Varaustenhallinta");
        stage.setScene(scene);
        stage.show();
    }
    
    /** LaskujenhallintaFXMLcontroller luokan avaus painike
     * 
     * @param event Action handleri
     * @throws IOException virheentunnistus
     */
    @FXML
    private void openLaskujenhallinta(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("LaskujenhallintaFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Laskujenhallinta");
        stage.setScene(scene);
        stage.show();
    }
    
    /** Asiakashallintacontroller luokan avaus painike
     * 
     * @param event Action handleri
     * @throws IOException virheentunnistus
     */
    @FXML
    private void openAsiakashallinta(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("AsiakashallintaFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Asiakashallinta");
        stage.setScene(scene);
        stage.show();
    }
    
    /** ToimitilahallintaFXMLcontroller luokan avaus painike
     * 
     * @param event Action handleri
     * @throws IOException virheentunnistus
     */    
    @FXML
    private void openToimitilahallinta(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("ToimitilahallintaFXML.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Toimitilojen hallinta");
        stage.setScene(scene);
        stage.show();
    }
}


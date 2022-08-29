/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vuokratoimistot;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author Teemu Mäkelä, Tapio Meriläinen
 */
public class RaportointiFXMLController implements Initializable {

    int toimipisteID;
    String sijainti;
    String lahiosoite;
    String postitoimipaikka;
    String postinro;
    String email;
    String puhelinnro;
    String tieto1;
    String tieto2;
    String tieto3;
    @FXML private TextArea Tulokset;

    @FXML private ChoiceBox raportointiCB;
    
    @FXML private Label raportointiLbl;
    @FXML private Label raportointiLbl2;
    @FXML private Label raportointiLbl3;
    
    @FXML private CheckBox toimitila1Checkbox;
    @FXML private CheckBox toimitila2Checkbox;
    @FXML private CheckBox toimitila3Checkbox;
    @FXML private CheckBox toimitila4Checkbox;
    @FXML private CheckBox toimitila5Checkbox;
    @FXML private CheckBox toimitila6Checkbox;
    @FXML private CheckBox toimitila7Checkbox;
    @FXML private CheckBox toimitila8Checkbox;
    
    @FXML private CheckBox lisapalvelu1Checkbox;
    @FXML private CheckBox lisapalvelu2Checkbox;
    @FXML private CheckBox lisapalvelu3Checkbox;
    @FXML private CheckBox lisapalvelu4Checkbox;
    @FXML private CheckBox lisapalvelu5Checkbox;
    @FXML private CheckBox lisapalvelu6Checkbox;
    @FXML private CheckBox lisapalvelu7Checkbox;
    @FXML private CheckBox lisapalvelu8Checkbox;
    @FXML private Button tulostaRaportti;

    @FXML private ListView toimitilaTulokset;
    @FXML private TextField haeToimitilaaKentta;

    ObservableList<String> osoitteet;

    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb){
        EventHandler<ActionEvent> kasittelija = event -> {
            try {
                haeToimitilaa();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        };
        tulostaRaportti.setOnAction(kasittelija);


        raportointiCB.getItems().addAll("Tammikuu","Helmikuu","Maaliskuu","Huhtikuu","Toukokuu","Kesäkuu", "Heinäkuu",
            "Elokuu", "Syyskuu", "Lokakuu", "Marraskuu", "Joulukuu");
    }
    private void haeToimitilaa() throws SQLException{
        Connection con = openConnection("jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");
        useDatabase(con,"r8_vuokratoimistot");
        try {
            toimipisteID = Integer.parseInt(haeToimitilaaKentta.getText());
            Statement haeTilat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = haeTilat.executeQuery("SELECT lahiosoite, postitoimipaikka, postinro, email, puhnro FROM toimipiste WHERE toimipisteID = " + toimipisteID + "");
            rs.absolute(1);
            tieto1 = ("Valitun tilan tiedot: " + "\n" + rs.getString(1) +"\n"+ rs.getString(2) +"\n"+ rs.getString(3));
            Tulokset.setText(tieto1);
        } catch (Exception exception) {
            System.out.println("Virhe...");
        }
        try {
            Statement haeVaraukset = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs2 = haeVaraukset.executeQuery("SELECT varattuALK, varattuLOPP FROM varaukset WHERE toimipisteID = " + toimipisteID + "");
            rs2.absolute(1);
            tieto2 = ("Valitun tilan varaukset:" +"\n"+ rs2.getString(1) + "  -  " + rs2.getString(2));
        }catch (SQLException sqlException){
            System.out.println("Ei varauksia tilalle.");
            tieto2 = "Ei varauksia";
        }
        try {
            Statement haePalvelut = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs3 = haePalvelut.executeQuery("SELECT palveluID, palvelunimi, palvelutyyppi, palvelukuvaus, hinta FROM palvelut WHERE toimipisteID = " + toimipisteID + "");
            rs3.absolute(1);
            tieto3 = ("Valitun tilan palvelut: " + "\n" + "Palvelun ID: " + rs3.getString(1) + "\n" + "Palvelun nimi: " + rs3.getString(2) + "\n" + "Palvelun tyyppi: " +rs3.getString(3) + "\n" + "Palvelun kuvaus: "+rs3.getString(4)+"\n" + "Palvelun hinta: " + rs3.getString(5));
        }catch (SQLException sqlException){
            System.out.println("Ei palveluita toimitilalla");
            tieto3 = "Ei palveluita toimitilalla";
        }
        Tulokset.setText(tieto1 +"\n"+ tieto2 + "\n" + tieto3);
    }
    private static void useDatabase(Connection c, String db) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Kaytetaan tietokantaa " + db);
    }
    private static Connection openConnection(String connString) throws SQLException {

        Connection con = DriverManager.getConnection(connString);
        System.out.println("\t>> Yhteys ok");
        return con;
    }
}

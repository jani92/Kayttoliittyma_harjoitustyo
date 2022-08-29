package vuokratoimistot;

import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * 
 * @author Jani Rytkönen, Niko Ryynänen
 */

public class AsiakashallintaController implements Initializable {

    int asiakasID;

    String etunimi;

    String sukunimi;

    String lahiosoite;

    String postitoimipaikka;

    String postinro;

    String email;

    String puhelinnro;

    @FXML
    private TextField txtAsiakasid;
    @FXML
    private TextField txtEtunimi;
    @FXML
    private TextField txtSukunimi;
    @FXML
    private TextField txtLahiosoite;
    @FXML
    private TextField txtPostitoimipaikka;
    @FXML
    private TextField txtPostinumero;
    @FXML
    private TextField txtSahkopostiosoite;
    @FXML
    private TextField txtPuhelinnumero;
    @FXML
    private Button btnHaeAsiakasHallinta;
    @FXML
    private Button btnHaeAsiakasHallinta1;
    @FXML
    private Button btnHaeAsiakasHallinta11;
    @FXML
    private Button btnHaeAsiakasHallinta111;

    /**
     * Asiakkaan tiedot hakeva painike
     *
     * @param event Action handleri
     */
    @FXML
    private void haeTiedot(ActionEvent event) {

        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            try {
                asiakasID = Integer.parseInt(txtAsiakasid.getText());
                txtEtunimi.setText(txtEtunimi.getText());
                sukunimi = txtSukunimi.getText();
                lahiosoite = txtLahiosoite.getText();
                postitoimipaikka = txtPostitoimipaikka.getText();
                postinro = txtPostinumero.getText();
                email = txtSahkopostiosoite.getText();
                puhelinnro = txtPuhelinnumero.getText();

            } catch (Exception e) {

                System.out.println("Syöte väärässä muodossa:" + e);

                
            }
            haeAsiakas(conn, txtAsiakasid, txtEtunimi, txtSukunimi, txtLahiosoite, txtPostitoimipaikka, txtPostinumero, txtSahkopostiosoite, txtPuhelinnumero);
            closeConnection(conn);

        } catch (SQLException e) {

            System.out.println("Tapahtui virhe:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Asiakasta ei ole olemassa");
            about.show();
        }
    }

    /**
     * ASiakkaan päivittävä painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void paivitaTiedot(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        try {
            asiakasID = Integer.parseInt(txtAsiakasid.getText());
            etunimi = txtEtunimi.getText();
            sukunimi = txtSukunimi.getText();
            lahiosoite = txtLahiosoite.getText();
            postitoimipaikka = txtPostitoimipaikka.getText();
            postinro = txtPostinumero.getText();
            email = txtSahkopostiosoite.getText();
            puhelinnro = txtPuhelinnumero.getText();
            try {

                updateAsiakas(conn, asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro);

            } catch (SQLException e) {

                System.out.println("AsiakasID:tä ei ole! " + e);

                Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
                about.setTitle("Virhe!");
                about.setHeaderText("AsiakasID:tä ei ole!");
                about.show();
            }

            closeConnection(conn);

        } catch (Exception e) {

            System.out.println("Tapahtui virhe:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Syöte väärässä muodossa!");
            about.show();
        }
    }

    /**
     * Asiakkaan lisäävä painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void lisaaAsiakas(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        try {
            asiakasID = Integer.parseInt(txtAsiakasid.getText());
            etunimi = txtEtunimi.getText();
            sukunimi = txtSukunimi.getText();
            lahiosoite = txtLahiosoite.getText();
            postitoimipaikka = txtPostitoimipaikka.getText();
            postinro = txtPostinumero.getText();
            email = txtSahkopostiosoite.getText();
            puhelinnro = txtPuhelinnumero.getText();

            try {

                addAsiakas(conn, asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro);

            } catch (SQLException e) {

                System.out.println("Syöte väärässä muodossa:" + e);

                Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
                about.setTitle("Virhe!");
                about.setHeaderText("AsiakasID on jo käytössä!");
                about.show();
            }

            closeConnection(conn);

        } catch (Exception e) {

            System.out.println("Tapahtui virhe:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Syöte väärässä muodossa!");
            about.show();
        }
    }

    /**
     * Asiakkaan poistava painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void poistaAsiakas(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        asiakasID = Integer.parseInt(txtAsiakasid.getText());

        deleteAsiakas(conn, asiakasID);

        System.out.println(etunimi + "\n" + sukunimi + "\n" + lahiosoite + "\n" + postitoimipaikka + "\n" + postinro + "\n" + email + "\n" + puhelinnro);

        closeConnection(conn);
        
        txtAsiakasid.clear();
        txtEtunimi.clear();
        txtSukunimi.clear();
        txtLahiosoite.clear();
        txtPostitoimipaikka.clear();
        txtPostinumero.clear();
        txtSahkopostiosoite.clear();
        txtPuhelinnumero.clear();
    }

    /**
     *
     * @param url url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Yhteyden avaus SQL-serveriin
     *
     * @param connString Verkko oisoite merkkijonona
     * @return connString
     * @throws SQLException SQLException virheentunnistus
     */
    private static Connection openConnection(String connString) throws SQLException {
        Connection con = DriverManager.getConnection(connString);
        System.out.println("\t>> Yhteys ok");
        return con;
    }

    /**
     * Yhteyden sulkeminen SQL-serveriin
     *
     * @param c Yhteys
     * @throws SQLException SQLException virheentunnistus
     */
    private static void closeConnection(Connection c) throws SQLException {
        if (c != null) {
            c.close();
        }
        System.out.println("\t>> Tietokantayhteys suljettu");
    }

    /**
     * SQL-databasen kayttoonotto
     *
     * @param c Yhteys
     * @param db Databasen nimi merkkijonona
     * @throws SQLException SQLException
     */
    private static void useDatabase(Connection c, String db) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Kaytetaan tietokantaa " + db);
    }

    /**
     * Asiakkan lisäys metodi
     *
     * @param c yhteys
     * @param asiakasID Asiakkaan id
     * @param etunimi Asiakkaan etunimi
     * @param sukunimi Asiakkaan sukunimi
     * @param lahiosoite Asiakkaan lahiosoite
     * @param postitoimipaikka Asiakkaan postitoimipaikka
     * @param postinro Asiakkaan postinumero
     * @param email Asiakkaan email
     * @param puhelinnro Asiakkaan puhelinnumero
     * @throws SQLException virheentunnistus
     */
    private static void addAsiakas(Connection c, int asiakasID, String etunimi, String sukunimi, String lahiosoite, String postitoimipaikka,
            String postinro, String email, String puhelinnro) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO asiakas (asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );
        
        ps.setInt(1, asiakasID);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        ps.setString(4, lahiosoite);
        ps.setString(5, postitoimipaikka);
        ps.setString(6, postinro);
        ps.setString(7, email);
        ps.setString(8, puhelinnro);

        ps.execute();

        System.out.println("\t>> Lisatty asiakas " + "Id:" + asiakasID + " Etunimi:" + etunimi + " Sukunimi:" + sukunimi + " ");

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Asiakas lisätty");
        about.setHeaderText("Asiakas [" + asiakasID + "] " + etunimi + " " + sukunimi + "\nlisätty onnistuneesti");
        about.show();
    }

    /**
     * Asiakkaan tietojen muokkaus metodi
     *
     * @param c yhteys
     * @param asiakasID Asiakkaan id
     * @param etunimi Asiakkaan etunimi
     * @param sukunimi Asiakkaan sukunimi
     * @param lahiosoite Asiakkaan lahiosoite
     * @param postitoimipaikka Asiakkaan postitoimipaikka
     * @param postinro Asiakkaan postinumero
     * @param email Asiakkaan email
     * @param puhelinnro Asiakkaan puhelinnumero
     * @throws SQLException virheentunnistus
     */
    private static void updateAsiakas(Connection c, int asiakasID, String etunimi, String sukunimi, String lahiosoite, String postitoimipaikka,
            String postinro, String email, String puhelinnro) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "UPDATE asiakas "
                + "SET etunimi= '" + etunimi + "',"
                + "sukunimi= '" + sukunimi + "',"
                + "lahiosoite= '" + lahiosoite + "',"
                + "postitoimipaikka= '" + postitoimipaikka + "',"
                + "postinro= '" + postinro + "',"
                + "email= '" + email + "',"
                + "puhelinnro= '" + puhelinnro + "'"
                + "WHERE asiakasID= " + asiakasID + ""
        );

        ps.execute();

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Asiakkaan tiedot päivitetty");
        about.setHeaderText("Asiakkaan [" + asiakasID + "] " + etunimi + " " + sukunimi + "\ntiedot päivitetty onnistuneesti");
        about.show();
    }

    /**
     * Asiakkaan poistava metodi
     *
     * @param c yhteys
     * @param asiakasID asiakkaan ID
     * @throws SQLException virheentunnistus
     */
    public static void deleteAsiakas(Connection c, int asiakasID) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "DELETE  "
                + "FROM asiakas "
                + "WHERE asiakasID = ?"
        );

        ps.setInt(1, asiakasID);

        ps.execute();

        System.out.println("\t>> Poistettu asiakas " + asiakasID);

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Asiakas poistettu");
        about.setHeaderText("Asiakkaan [" + asiakasID + "] tiedot poistettu!");
        about.show();

    }

    /**
     * Asiakkaan tiedot hakeva metodi
     *
     * @param c yhteys
     * @param asiakasID Asiakkaan id
     * @param etunimi Asiakkaan etunimi
     * @param sukunimi Asiakkaan sukunimi
     * @param lahiosoite Asiakkaan lahiosoite
     * @param postitoimipaikka Asiakkaan postitoimipaikka
     * @param postinro Asiakkaan postinumero
     * @param email Asiakkaan email
     * @param puhelinnro Asiakkaan puhelinnumero
     * @throws SQLException virheentunnistus
     */
    private static void haeAsiakas(Connection c, TextField txtAsiakasid, TextField txtEtunimi, TextField txtSukunimi, TextField txtLahiosoite,
            TextField txtPostitoimipaikka, TextField txtPostinumero, TextField txtSahkopostiosoite, TextField txtPuhelinnumero) throws SQLException {

        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = stmt.executeQuery("SELECT asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, puhelinnro FROM asiakas WHERE asiakasID = " + txtAsiakasid.getText() + "");

        rs.absolute(1);

        txtAsiakasid.setText(rs.getString("asiakasid"));

        txtEtunimi.setText(rs.getString("etunimi"));

        txtSukunimi.setText(rs.getString("sukunimi"));

        txtLahiosoite.setText(rs.getString("lahiosoite"));

        txtPostitoimipaikka.setText(rs.getString("postitoimipaikka"));

        txtPostinumero.setText(rs.getString("postinro"));

        txtSahkopostiosoite.setText(rs.getString("email"));

        txtPuhelinnumero.setText(rs.getString("puhelinnro"));

    }
    
    /** Tekstikentät tyhjentävä painike
     * 
     * @param event 
     */
    @FXML
    private void btnClearFields(ActionEvent event) {
        
            txtAsiakasid.clear();
            txtEtunimi.clear();
            txtSukunimi.clear();
            txtLahiosoite.clear();
            txtPostitoimipaikka.clear();
            txtPostinumero.clear();
            txtSahkopostiosoite.clear();
            txtPuhelinnumero.clear();
        
    }

}

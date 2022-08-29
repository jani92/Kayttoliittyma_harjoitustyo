package vuokratoimistot;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jani Rytkönen, Niko Ryynänen
 */
public class ToimitilahallintaFXMLController implements Initializable {

    @FXML
    private TextField txtToimipisteID;
    @FXML
    private TextField txtSijanti;
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

    int toimipisteID;

    String sijainti;

    String lahiosoite;

    String postitoimipaikka;

    String postinro;

    String email;

    String puhelinnro;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
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
     * Toimipisteen lisaava metodi
     *
     * @param c yhteys
     * @param toimipisteID Toimipisteen id
     * @param sijainti Toimipisteen sijainti
     * @param lahiosoite Toimipisteen lahiosoite
     * @param postitoimipaikka Toimipisteen
     * @param postinro Toimipisteen
     * @param email Toimipisteen
     * @param puhelinnro Toimipisteen
     * @throws SQLException Virheentunnistus
     */
    private static void addToimipiste(Connection c, int toimipisteID, String sijainti, String lahiosoite, String postitoimipaikka,
            String postinro, String email, String puhelinnro) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO toimipiste (toimipisteID, sijainti, lahiosoite, postitoimipaikka, postinro, email, puhnro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)"
        );

        ps.setInt(1, toimipisteID);
        ps.setString(2, sijainti);
        ps.setString(3, lahiosoite);
        ps.setString(4, postitoimipaikka);
        ps.setString(5, postinro);
        ps.setString(6, email);
        ps.setString(7, puhelinnro);

        ps.execute();

        System.out.println("\t>> Lisatty toimipiste " + "Id:" + toimipisteID);

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Toimipiste lisätty");
        about.setHeaderText("Toimipiste [" + toimipisteID + "] " + sijainti + "\nlisätty onnistuneesti");
        about.show();
    }

    /**
     * Toimipisteen muokkaava metodi
     *
     * @param c yhteys
     * @param toimipisteID Toimipisteen id
     * @param sijainti Toimipisteen sijainti
     * @param lahiosoite Toimipisteen lahiosoite
     * @param postitoimipaikka Toimipisteen
     * @param postinro Toimipisteen
     * @param email Toimipisteen
     * @param puhelinnro Toimipisteen
     * @throws SQLException Virheentunnistus
     */
    private static void updateToimipiste(Connection c, int toimipisteID, String sijainti, String lahiosoite, String postitoimipaikka,
            String postinro, String email, String puhelinnro) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "UPDATE toimipiste "
                + "SET sijainti= '" + sijainti + "',"
                + "lahiosoite= '" + lahiosoite + "',"
                + "postitoimipaikka= '" + postitoimipaikka + "',"
                + "postinro= '" + postinro + "',"
                + "email= '" + email + "',"
                + "puhnro= '" + puhelinnro + "'"
                + "WHERE toimipisteID= " + toimipisteID + ""
        );

        ps.execute();

        System.out.println("\t>> Muokattu toimipiste " + "Id:" + toimipisteID);

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Toimipisteen tiedot päivitetty");
        about.setHeaderText("Toimipisteen [" + toimipisteID + "] " + sijainti + "\ntiedot päivitetty onnistuneesti");
        about.show();
    }

    /**
     * Toimipisteen poistava metodi
     *
     * @param c yhteys
     * @param toimipisteID Toimipisteen ID
     * @throws SQLException virheentunnistus
     */
    public static void deleteToimipiste(Connection c, int toimipisteID) throws SQLException {

        PreparedStatement ps = c.prepareStatement(
                "DELETE  "
                + "FROM toimipiste "
                + "WHERE toimipisteID = ?"
        );
        ps.setInt(1, toimipisteID);

        ps.execute();

        System.out.println("\t>> Poistettu toimipiste " + toimipisteID);

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Toimitila poistettu");
        about.setHeaderText("Toimitilan [" + toimipisteID + "] tiedot poistettu!");
        about.show();
    }

    /**
     * Toimipisteen tietojen hakeva metodi
     *
     * @param c Yhteys
     * @param txtToimipisteID toimipisteen tekstikenttä
     * @param txtSijanti toimipisteen tekstikenttä
     * @param txtLahiosoite toimipisteen tekstikenttä
     * @param txtPostitoimipaikka toimipisteen tekstikenttä
     * @param txtPostinumero toimipisteen tekstikenttä
     * @param txtSahkopostiosoite toimipisteen tekstikenttä
     * @param txtPuhelinnumero toimipisteen tekstikenttä
     * @throws SQLException virheentunnistus
     */
    private static void haeToimipiste(Connection c, TextField txtToimipisteID, TextField txtSijanti, TextField txtLahiosoite,
            TextField txtPostitoimipaikka, TextField txtPostinumero, TextField txtSahkopostiosoite, TextField txtPuhelinnumero) throws SQLException {

        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT toimipisteID, sijainti, lahiosoite, postitoimipaikka, postinro, email, puhnro "
                + "FROM toimipiste WHERE toimipisteID = " + txtToimipisteID.getText() + "");

        rs.absolute(1);

        txtToimipisteID.setText(rs.getString("toimipisteID"));

        txtSijanti.setText(rs.getString("sijainti"));

        txtLahiosoite.setText(rs.getString("lahiosoite"));

        txtPostitoimipaikka.setText(rs.getString("postitoimipaikka"));

        txtPostinumero.setText(rs.getString("postinro"));

        txtSahkopostiosoite.setText(rs.getString("email"));

        txtPuhelinnumero.setText(rs.getString("puhnro"));

    }

    /**
     * Toimipisteen tiedot hakeva painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void btnHaeTiedot(ActionEvent event) throws SQLException {

        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            haeToimipiste(conn, txtToimipisteID, txtSijanti, txtLahiosoite, txtPostitoimipaikka, txtPostinumero, txtSahkopostiosoite, txtPuhelinnumero);

        } catch (SQLException e) {

            System.out.println("Tapahtui virhe:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Toimipistettä ei ole olemassa");
            about.show();
        }
    }

    /**
     * Toimipisteen tiedot päivittävä painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void btnPaivitaTiedot(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        try {
            toimipisteID = Integer.parseInt(txtToimipisteID.getText());
            sijainti = txtSijanti.getText();
            lahiosoite = txtLahiosoite.getText();
            postitoimipaikka = txtPostitoimipaikka.getText();
            postinro = txtPostinumero.getText();
            email = txtSahkopostiosoite.getText();
            puhelinnro = txtPuhelinnumero.getText();

            updateToimipiste(conn, toimipisteID, sijainti, lahiosoite, postitoimipaikka, postinro, email, puhelinnro);

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
     * Toimipisteen uudet tiedot lisäävä painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void btnLisaaToimipiste(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        try {
            toimipisteID = Integer.parseInt(txtToimipisteID.getText());
            sijainti = txtSijanti.getText();
            lahiosoite = txtLahiosoite.getText();
            postitoimipaikka = txtPostitoimipaikka.getText();
            postinro = txtPostinumero.getText();
            email = txtSahkopostiosoite.getText();
            puhelinnro = txtPuhelinnumero.getText();

            try {
                addToimipiste(conn, toimipisteID, sijainti, lahiosoite, postitoimipaikka, postinro, email, puhelinnro);

            } catch (SQLException e) {
                System.out.println("Syöte väärässä muodossa:" + e);

                Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
                about.setTitle("Virhe!");
                about.setHeaderText("ToimitilaID on jo käytössä!");
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
     * Toimipisteen poistava painike
     *
     * @param event Action handleri
     * @throws SQLException virheentunnistus
     */
    @FXML
    private void btnPoistaToimipiste(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        toimipisteID = Integer.parseInt(txtToimipisteID.getText());

        deleteToimipiste(conn, toimipisteID);

        System.out.println("Poistettu toimipiste");

        closeConnection(conn);
        
        txtToimipisteID.clear();
        txtSijanti.clear();
        txtLahiosoite.clear();
        txtPostitoimipaikka.clear();
        txtPostinumero.clear();
        txtSahkopostiosoite.clear();
        txtPuhelinnumero.clear(); 
        
    }
    
    /** Tekstikentät tyhjentävä painike
     * 
     * @param event Action handleri
     */
    @FXML
    private void btnClearFields(ActionEvent event) {
        
        txtToimipisteID.clear();
        txtSijanti.clear();
        txtLahiosoite.clear();
        txtPostitoimipaikka.clear();
        txtPostinumero.clear();
        txtSahkopostiosoite.clear();
        txtPuhelinnumero.clear();        
        
    }

}

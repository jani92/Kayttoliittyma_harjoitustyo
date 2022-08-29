package vuokratoimistot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * 
 * @author Jani Rytkönen, Niko Ryynänen
 */

public class VaraustenhallintaFXMLController implements Initializable {
    
    @FXML
    TextField asiakasID;
    @FXML
    TextField toimipisteID;
    @FXML
    TextField varattu;
    @FXML
    TextField vahvistus;

    String varattuALK;

    String varattuLOPP;

    String varaus1;

    String vahvistus1;

    String varattu1;

    int asiakasID1;

    int toimipisteID111;

    int toimipisteIDD;

    int palveluID;

    int varausIDD;

    int varaus123;

    int toimipisteIDDD;

    String palvelunimi;

    String palvelutyyppi;

    String palvelukuvaus;

    double hinta;
    @FXML
    private TextField txtToimipisteID;
    @FXML
    private TextField txtPalvelunnimi;
    @FXML
    private TextField txtPalvelutyyppi;
    @FXML
    private TextField txtPalvelunkuvaus;
    @FXML
    private TextField txtPalvelunhinta;
    @FXML
    private TextField txtPalveluIDD;
    @FXML
    private TextField txxtTilaus;
    @FXML
    private TextField txxtAsiakasID;
    @FXML
    private TextField txxtToimipisteID;
    @FXML
    private TextField txxtVarausALK;
    @FXML
    private TextField txxtVarausLOPP;
    @FXML
    private TextField txxtVahvistus;
    @FXML
    private TextField txxxtasiakasID;
    @FXML
    private TextField txxxtVarattu;
    @FXML
    private TextField txxxtVahvistus;
    @FXML
    private TextField txxxtVarausALK;
    @FXML
    private TextField txxtVaraustehty;
    @FXML
    private TextField txxxtToimipisteID;
    @FXML
    private TextField txxxtVarausLOPP;
    @FXML
    private TextField txtxVarausID;
    @FXML
    private TextField txtToimipiste;
    @FXML
    private TextField txtAsiakkaannimi;
    @FXML
    private TextField txtAsiakkaansukunimi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    /** Palveluiden muokkaus ikkunan avaava painike
     *
     * @param event Action handleri
     * @throws IOException Virheentunnistus
     */
    @FXML
    private void openLisapalveluhallinta(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MuokkaaLisapalveluja.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("MuokkaaLisapalveluja");
        stage.setScene(scene);
        stage.show();
    }

    /** Toimitilojen muokkaus ikkunan avaava painike
     *
     * @param event Action handleri
     * @throws IOException Virheentunnistus
     */
    @FXML
    private void openMuokkaavarattujatoimitiloja(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MuokkaaVarattujaToimitiloja.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Muokkaa varattuja toimitiloja");
        stage.setScene(scene);
        stage.show();

    }

    /** Uuden varauksen luonti ikkunan avaava painike
     *
     * @param event Action handleri
     * @throws IOException Virheentunnistus
     */
    @FXML
    private void openLuoUusiVaraus(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("uusiVaraus.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Luo uusi varaus");
        stage.setScene(scene);
        stage.show();
    }

    /** Lisäpalvelun tietojenhaku metodi
     *
     * @param c Yhteys
     * @param txtPalveluID Tekstikenttä palvelun id:lle
     * @param txtToimipisteID Tekstikenttä palvelun toimipisteelle
     * @param txtPalvelunnimi Tekstikenttä palvelun nimelle
     * @param txtPalvelutyyppi Tekstikenttä palvelun tyypille
     * @param txtPalvelunkuvaus Tekstikenttä palvelun kuvaukselle
     * @param txtPalvelunhinta Tekstikenttä palvelun hinnalle
     * @throws SQLException Virheentunnistus
     */
    private static void haeLisapalvelu(Connection c, TextField txtPalveluID, TextField txtToimipisteID, TextField txtPalvelunnimi, TextField txtPalvelutyyppi, TextField txtPalvelunkuvaus, TextField txtPalvelunhinta) throws SQLException {
        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT palveluID, toimipisteID, palvelunimi, palvelutyyppi, palvelukuvaus, hinta FROM palvelut WHERE palveluID = " + txtPalveluID.getText() + "");

        rs.absolute(1);

        txtPalveluID.setText(rs.getString("palveluID"));

        txtToimipisteID.setText(rs.getString("toimipisteID"));

        txtPalvelunnimi.setText(rs.getString("palvelunimi"));

        txtPalvelutyyppi.setText(rs.getString("palvelutyyppi"));

        txtPalvelunkuvaus.setText(rs.getString("palvelukuvaus"));

        txtPalvelunhinta.setText(rs.getString("hinta"));
    }

    /** Palveluiden lisäys metodi
     *
     * @param c yhteys
     * @param palveluID Palvelun id
     * @param toimipisteID toimipisteen id
     * @param palvelunimi Palvelunnimi
     * @param palvelutyyppi Palveluntyyppi
     * @param palvelukuvaus Palvelun kuvaus
     * @param hinta Palvelun hinta
     * @throws SQLException
     */
    private static void addLisapalvelu(Connection c, int palveluID, int toimipisteIDDD, String palvelunimi, String palvelutyyppi, String palvelukuvaus,
            double hinta) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO palvelut (palveluID, toimipisteID, palvelunimi, palvelutyyppi, palvelukuvaus, hinta) "
                + "VALUES (?, ?, ?, ?, ?, ?)"
        );

        ps.setInt(1, palveluID);
        ps.setInt(2, toimipisteIDDD);
        ps.setString(3, palvelunimi);
        ps.setString(4, palvelutyyppi);
        ps.setString(5, palvelukuvaus);
        ps.setDouble(6, hinta);

        ps.execute();

        System.out.println("\t>> Lisatty lisapalvelu " + "PalveluId:" + palveluID + " ToimipisteID:" + toimipisteIDDD + " Palvelunnimi:" + palvelunimi);
        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Palvelu lisätty");
        about.setHeaderText("Palvelu [" + palveluID + "] " + palvelunimi + "\nlisätty onnistuneesti");
        about.show();
    }

    /**
     * Palvelun muokkaus metodi
     *
     * @param c yhteys
     * @param palveluID Palvelun id
     * @param toimipisteID toimipisteen id
     * @param palvelunimi Palvelunnimi
     * @param palvelutyyppi Palveluntyyppi
     * @param palvelukuvaus Palvelun kuvaus
     * @param hinta Palvelun hinta
     * @throws SQLException virheentunnistus
     */
    private static void updateLisapalvelu(Connection c, int palveluID, int toimipisteIDDD, String palvelunimi, String palvelutyyppi, String palvelukuvaus,
            double hinta) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "UPDATE palvelut "
                + "SET palveluID= '" + palveluID + "',"
                + "toimipisteID= '" + toimipisteIDDD + "',"
                + "palvelunimi= '" + palvelunimi + "',"
                + "palvelutyyppi= '" + palvelutyyppi + "',"
                + "palvelukuvaus= '" + palvelukuvaus + "',"
                + "hinta= '" + hinta + "'"
                + "WHERE palveluID= " + palveluID + ""
        );
        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Palvelun tiedot päivitetty");
        about.setHeaderText("Palvelun [" + palveluID + "] " + palvelunimi + "\ntiedot päivitetty onnistuneesti");
        about.show();

        ps.execute();
    }

    /** Palvelun poisto metodi
     *
     * @param c yhteys
     * @param palveluID Palvelun id
     * @throws SQLException virheentunnistus
     */
    public static void deleteLisapalvelu(Connection c, int palveluID) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE  "
                + "FROM palvelut "
                + "WHERE palveluID = ?"
        );
        ps.setInt(1, palveluID);

        ps.execute();

        System.out.println("\t>> Poistettu palvelu " + palveluID);
        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Palvelu poistettu");
        about.setHeaderText("Palvelun [" + palveluID + "] tiedot poistettu!");
        about.show();
    }

    /** Varauksen lisäys metodi
     *
     * @param c Yhteys
     * @param varaus123 Varauksen id
     * @param asiakasID1 Varauksen asiakkaan id
     * @param toimipisteID111 Varauksen toimipisteen id
     * @param varattu1 Varauksen varauspäivä
     * @param vahvistus1 Varauksen vahvistuspäivä
     * @param varattuALK Varauksen alkupäivä
     * @param varattuLOPPv Varauksen loppupäivä
     * @throws SQLException Virheentunnistus
     */
    private void addVaraus(Connection c, int varaus123, int asiakasID1, int toimipisteID111, String varattu1, String vahvistus1,
            String varattuALK, String varattuLOPP) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO varaukset (varausID, asiakasID, toimipisteID, varattu, vahvistus, varattuALK, varattuLOPP) "
                + "VALUES (?, ?, ?, STR_TO_DATE(?, '%d.%m.%Y'), STR_TO_DATE(?, '%d.%m.%Y'), STR_TO_DATE(?, '%d.%m.%Y'), STR_TO_DATE(?, '%d.%m.%Y'))"
        );
        ps.setInt(1, varaus123);
        ps.setInt(2, asiakasID1);
        ps.setInt(3, toimipisteID111);
        ps.setString(4, varattu1);
        ps.setString(5, vahvistus1);
        ps.setString(6, varattuALK);
        ps.setString(7, varattuLOPP);

        ps.execute();

        System.out.println("\t>> Lisatty varaus " + "VarausId:" + varaus123);

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Varaus lisätty");
        about.setHeaderText("Varaus [" + varaus123 + "] \nlisätty onnistuneesti");
        about.show();
    }

    /** Varauksen muokkaus metodi
     *
     * @param c Yhteys
     * @param varaus123 Varauksen id
     * @param asiakasID1 Varauksen asiakkaan id
     * @param toimipisteID111 Varauksen toimipisteen id
     * @param varattu1 Varauksen varauspäivä
     * @param vahvistus1 Varauksen vahvistuspäivä
     * @param varattuALK Varauksen alkupäivä
     * @param varattuLOPPv Varauksen loppupäivä
     * @throws SQLException Virheentunnistus
     */
    private static void updateVaraus(Connection c, int varaus123, int asiakasID1, int toimipisteID111, String varattu1, String vahvistus1,
            String varattuALK, String varattuLOPP) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "UPDATE varaukset SET "
                + "asiakasID= '" + asiakasID1 + "',"
                + "toimipisteID= '" + toimipisteID111 + "',"
                + "varattu= STR_TO_DATE('" + varattu1 + "', '%d.%m.%Y'),"
                + "vahvistus= STR_TO_DATE('" + vahvistus1 + "', '%d.%m.%Y'),"
                + "varattuALK= STR_TO_DATE('" + varattuALK + "', '%d.%m.%Y'),"
                + "varattuLOPP= STR_TO_DATE('" + varattuLOPP + "', '%d.%m.%Y')"
                + "WHERE varausID= " + varaus123 + ""
        );

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Varauksen tiedot päivitetty");
        about.setHeaderText("Varauksen ID: [" + varaus123 + "] \ntiedot päivitetty onnistuneesti");
        about.show();

        ps.execute();
    }

    /** Varauksen poisto metodi
     *
     * @param c Yhteys
     * @param varaus123 Varauksen id
     * @throws SQLException Virheentunnistus
     */
    public static void deleteVaraus(Connection c, int varaus123) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE  "
                + "FROM varaukset "
                + "WHERE varausID = ?"
        );
        ps.setInt(1, varaus123);

        ps.execute();

        System.out.println("\t>> Poistettu varaus " + varaus123);
        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Varaus poistettu");
        about.setHeaderText("Varauksen [" + varaus123 + "] tiedot poistettu!");
        about.show();
    }

    /** Varauksen haku metodi
     *
     * @param c Yhteys
     * @param txxtTilaus Tekstikenttä varaud id syötteelle
     * @param txxtAsiakasID Tekstikenttä varaud id syötteelle
     * @param txxtToimipisteID Tekstikenttä varaud id syötteelle
     * @param txxtVaraustehty Tekstikenttä varaud id syötteelle
     * @param txxtVahvistus Tekstikenttä varaud id syötteelle
     * @param txxtVarausALK Tekstikenttä varaud id syötteelle
     * @param txxtVarausLOPP Tekstikenttä varaud id syötteelle
     * @throws SQLException Virheentunnistus
     */
    private static void haeVaraus(Connection c, TextField txxtTilaus, TextField txxtAsiakasID, TextField txxtToimipisteID,
            TextField txxtVaraustehty, TextField txxtVahvistus, TextField txxtVarausALK, TextField txxtVarausLOPP) throws SQLException {
        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT varausID, asiakasID, toimipisteID, DATE_FORMAT(varattu, '%d.%m.%Y') varattu,"
                + " DATE_FORMAT(vahvistus, '%d.%m.%Y') vahvistus, DATE_FORMAT(varattuALK, '%d.%m.%Y') varattuALK,"
                + " DATE_FORMAT(varattuLOPP, '%d.%m.%Y') varattuLOPP FROM varaukset WHERE varausID = " + txxtTilaus.getText() + "");

        rs.absolute(1);

        txxtTilaus.setText(rs.getString("varausID"));

        txxtAsiakasID.setText(rs.getString("asiakasID"));

        txxtToimipisteID.setText(rs.getString("toimipisteID"));

        txxtVaraustehty.setText(rs.getString("varattu"));

        txxtVahvistus.setText(rs.getString("vahvistus"));

        txxtVarausALK.setText(rs.getString("varattuALK"));

        txxtVarausLOPP.setText(rs.getString("varattuLOPP"));
    }

    /** Asiakkaan etunimen haku metodi
     * @param c Yhteys
     * @param txtAsiakkaannimi Tekstikenttä asiakkaan etunimen syötteelle
     * @param txxtAsiakasID Tekstikenttä asiakas id syötteelle
     */
    private static void haeVarausAsiakasEtunimi(Connection c, TextField txtAsiakkaannimi, TextField txxtAsiakasID) throws SQLException {
        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT etunimi, sukunimi FROM asiakas WHERE asiakasID = " + txxtAsiakasID.getText() + "");

        rs.absolute(1);

        txtAsiakkaannimi.setText(rs.getString("etunimi"));

    }

    /** Asiakkaan sukunimen haku metodi
     * @param c Yhteys
     * @param txtAsiakkaansukunimi Tekstikenttä asiakkaan sukunimen syötteelle
     * @param txxtAsiakasID Tekstikenttä asiakas id syötteelle
     */
    private static void haeVarausAsiakasSukunimi(Connection c, TextField txtAsiakkaansukunimi, TextField txxtAsiakasID) throws SQLException {
        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT sukunimi FROM asiakas WHERE asiakasID = " + txxtAsiakasID.getText() + "");

        rs.absolute(1);

        txtAsiakkaansukunimi.setText(rs.getString("sukunimi"));

    }

    /** Toimipisteen sijainnin haku metodi
     * @param c Yhteys
     * @param txtToimipiste Tekstikenttä toimipisteen sijainnin syötteelle
     * @param txxtToimipisteID Tekstikenttä toimipiste id syötteelle
     */    
    private static void haeVarausToimipiste(Connection c, TextField txtToimipiste, TextField txxtToimipisteID) throws SQLException {
        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT sijainti FROM toimipiste WHERE toimipisteID = " + txxtToimipisteID.getText() + "");

        rs.absolute(1);

        txtToimipiste.setText(rs.getString("sijainti"));

    }

    /** Uuden varauksen lisäys painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnLuoUusiVaraus(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        varaus123 = Integer.parseInt(txtxVarausID.getText());
        asiakasID1 = Integer.parseInt(txxxtasiakasID.getText());
        toimipisteID111 = Integer.parseInt(txxxtToimipisteID.getText());
        varattu1 = txxxtVarattu.getText();
        vahvistus1 = txxxtVahvistus.getText();
        varattuALK = txxxtVarausALK.getText();
        varattuLOPP = txxxtVarausLOPP.getText();

        addVaraus(conn, varaus123, asiakasID1, toimipisteID111, varattu1, vahvistus1, varattuALK, varattuLOPP);

        closeConnection(conn);

    }

    /** Palvelun poisto painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnPoistaLisapalvelu(ActionEvent event) throws SQLException {
        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        palveluID = Integer.parseInt(txtPalveluIDD.getText());

        deleteLisapalvelu(conn, palveluID);
        System.out.println("Poistettu lisäpalvlu ID: " + palveluID);
        closeConnection(conn);
        
        txtPalveluIDD.clear();
        txtToimipisteID.clear();
        txtPalvelunnimi.clear();
        txtPalvelutyyppi.clear();
        txtPalvelunkuvaus.clear();
        txtPalvelunhinta.clear();
        
    }

    /** Palvelun lisäys painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnLisaaLisapalvelu(ActionEvent event) throws SQLException {

        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        palveluID = Integer.parseInt(txtPalveluIDD.getText());
        toimipisteIDDD = Integer.parseInt(txtToimipisteID.getText());
        palvelunimi = txtPalvelunnimi.getText();
        palvelutyyppi = txtPalvelutyyppi.getText();
        palvelukuvaus = txtPalvelunkuvaus.getText();
        hinta = Double.parseDouble(txtPalvelunhinta.getText());

        try {
            addLisapalvelu(conn, palveluID, toimipisteIDDD, palvelunimi, palvelutyyppi, palvelukuvaus, hinta);
        } catch (SQLException e) {
            System.out.println("Syöte väärässä muodossa:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("PalveluID on jo käytössä!");
            about.show();
        }
        closeConnection(conn);

    }

    /** Palvelun muokkaus painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnMuokkaaLisapalvelua(ActionEvent event) throws SQLException {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            palveluID = Integer.parseInt(txtPalveluIDD.getText());
            toimipisteIDDD = Integer.parseInt(txtToimipisteID.getText());
            palvelunimi = txtPalvelunnimi.getText();
            palvelutyyppi = txtPalvelutyyppi.getText();
            palvelukuvaus = txtPalvelunkuvaus.getText();
            hinta = Double.parseDouble(txtPalvelunhinta.getText());

            updateLisapalvelu(conn, palveluID, toimipisteIDDD, palvelunimi, palvelutyyppi, palvelukuvaus, hinta);

            closeConnection(conn);
        } catch (Exception e) {
            System.out.println("Tapahtui virhe:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Syöte väärässä muodossa!");
            about.show();
        }           
    }

    /** Palvelun haku painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnHaepalvelu(ActionEvent event) throws SQLException {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");
            haeLisapalvelu(conn, txtPalveluIDD, txtToimipisteID, txtPalvelunnimi, txtPalvelutyyppi, txtPalvelunkuvaus, txtPalvelunhinta);
            closeConnection(conn);

        } catch (SQLException e) {
            System.out.println("Tapahtui virhe:" + e);
            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Palvelua ei ole olemassa");
            about.show();
        }

    }

    /** Varauksen poisto painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnPoistaVaraus(ActionEvent event) throws SQLException {
        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        varaus123 = Integer.parseInt(txxtTilaus.getText());

        deleteVaraus(conn, varaus123);
        System.out.println("Poistettu Varaus ID: " + varaus123);
        closeConnection(conn);
        
        txxtTilaus.clear();    
        txxtAsiakasID.clear();
        txxtToimipisteID.clear();
        txxtVarausALK.clear();
        txxtVarausLOPP.clear();
        txxtVahvistus.clear();
        txxtVaraustehty.clear();
        txtToimipiste.clear();
        txtAsiakkaannimi.clear();
        txtAsiakkaansukunimi.clear();
        
    }

    /** Varauksen muokkaus painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnMuokkaaVaraus(ActionEvent event) throws SQLException {
        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        varaus123 = Integer.parseInt(txxtTilaus.getText());
        asiakasID1 = Integer.parseInt(txxtAsiakasID.getText());
        toimipisteID111 = Integer.parseInt(txxtToimipisteID.getText());
        varattu1 = txxtVaraustehty.getText();
        vahvistus1 = txxtVahvistus.getText();
        varattuALK = txxtVarausALK.getText();
        varattuLOPP = txxtVarausLOPP.getText();

        updateVaraus(conn, varaus123, asiakasID1, toimipisteID111, varattu1, vahvistus1, varattuALK, varattuLOPP);

        closeConnection(conn);
    }

    /** Varauksen haku painike
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnHaeVaraus(ActionEvent event) {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            haeVaraus(conn, txxtTilaus, txxtAsiakasID, txxtToimipisteID, txxtVaraustehty, txxtVahvistus, txxtVarausALK, txxtVarausLOPP);
            haeVarausAsiakasEtunimi(conn, txtAsiakkaannimi, txxtAsiakasID);
            haeVarausAsiakasSukunimi(conn, txtAsiakkaansukunimi, txxtAsiakasID);
            haeVarausToimipiste(conn, txtToimipiste, txxtToimipisteID);

        } catch (SQLException e) {
            System.out.println("Tapahtui virhe:" + e);
            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("Varausta ei ole olemassa");
            about.show();
        }
    }
    /** Uuden varauksen tekstikentät tyhjentävä painike
     * 
     * @param event Action handleri
     */
    @FXML
    private void btnClearFields(ActionEvent event) {
        
        txtxVarausID.clear();
        txxxtasiakasID.clear();
        txxxtToimipisteID.clear();
        txxxtVarattu.clear();
        txxxtVahvistus.clear();
        txxxtVarausALK.clear();
        txxxtVarausLOPP.clear();
    }
    /** Palveluiden tekstikentät tyhjentävä painike
     * 
     * @param event 
     */
    @FXML
    void btnTyhjennaKentat(ActionEvent event) {
        
        txtPalveluIDD.clear();
        txtToimipisteID.clear();
        txtPalvelunnimi.clear();
        txtPalvelutyyppi.clear();
        txtPalvelunkuvaus.clear();
        txtPalvelunhinta.clear();
    }
}

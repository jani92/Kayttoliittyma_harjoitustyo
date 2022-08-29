package vuokratoimistot;

import java.io.File;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Jani Rytkönen, Niko Ryynänen
 */
public class LaskujenhallintaFXMLController implements Initializable {

    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private TextField txtLaskuid;

    int laskuID;

    private static final String CUSTOMER_NAME = "__CUST_NAME__";
    private static final String CUSTOMER_ADDRESS = "__CUST_ADDR__";
    private static final String CUSTOMER_EMAIL = "__CUST_EMAIL__";
    private static final String INVOICE_DATE = "__DATE__";
    private static final String INVOICE_DUE_DATE = "__DUE_DATE__";
    private static final String TABLE_CONTENT = "__TABLE__";

    @FXML
    private WebView webView;
    @FXML
    private TextField txtLaskunlahetyspvm;
    @FXML
    private TextField txtAsiakkaanosoite;
    @FXML
    private TextField txtAsiakkansposti;
    @FXML
    private TextField txtLaskurerapvm;
    @FXML
    private TextField txtPalvelunselite;
    @FXML
    private TextField txtHinta;
    @FXML
    private TextField txtMaara;

    String AsiakkaanEtunimi;

    String AsiakkaanSukunimi;

    String Laskunlahetyspvm;

    String AsiakkaanLahiosoite;

    String AsiakkaanPostioimipaikka;

    String AsiakkaanPostinumero;

    String Asiakkansposti;

    String Laskurerapvm;

    String Palvelu;

    String Palvelunselite;

    int AsiakasID;

    int varausID;

    double Hinta;

    int Maara;

    double Summa;
    @FXML
    private RadioButton radSahkoposti;
    @FXML
    private RadioButton radPaperi;
    @FXML
    private TextField txtAsiakasEtunimi;
    @FXML
    private TextField txtPalvelunimi;
    @FXML
    private TextField txtAsiakasid;
    @FXML
    private TextField txtAsiakasSukunimi;
    @FXML
    private TextField txtAsiakasPostitoimipaikka;
    @FXML
    private TextField txtAsiakasPostinumero;
    @FXML
    private TextField txtPalveluID;
    @FXML
    private TextField txtVarausID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /** HTML
     *
     * @param service palvelu
     * @param desc kuvaus
     * @param unit yksikkö
     * @param qty määrä
     * @param total kokonaismäärä
     * @return palauta
     */
    private String createRow(String service, String desc, String unit, String qty, String total) {

        return "<tr>"
                + "<td class=\"service\">" + service + "</td>"
                + "<td class=\"desc\">" + desc + "</td>"
                + "<td class=\"unit\">" + unit + "€</td>"
                + "<td class=\"qty\">" + qty + "</td>"
                + "<td class=\"total\">" + total + "€</td>";

    }

    /** HTML sivun lataus
     * 
     * @param path Väylä
     */
    private void loadWebPage(String path) {
        WebEngine engine = webView.getEngine();
        File f = new File(path);
        engine.load(f.toURI().toString());
    }

    /**
     * Yhteyden avaus SQL-serveriin
     *
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
     * Yhteyden sulkeminen SQL-serveriin
     *
     * @param c Yhteys
     * @throws SQLException SQLException
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
     * Laskun tietojen lisäys metodi
     *
     * @param c Yhteys
     * @param laskuID Laskun id
     * @param varausID Varauksen id
     * @param AsiakasID Asiakkaan id
     * @param AsiakkaanEtunimi Asiakkaan etunimi
     * @param AsiakkaanSukunimi Asiakkaan sukunimi
     * @param AsiakkaanLahiosoite Asiakkaan lähiosoite
     * @param AsiakkaanPostioimipaikka Asiakkaan postitoimipaikka
     * @param AsiakkaanPostinumero Asiakkaan postinumero
     * @param Asiakkansposti Asiakkaan sähköposti
     * @param Summa Laskun summa
     * @throws SQLException Virheentunnistus
     */
    private static void addLasku(Connection c, int laskuID, int varausID, int AsiakasID, String AsiakkaanEtunimi, String AsiakkaanSukunimi, String AsiakkaanLahiosoite,
            String AsiakkaanPostioimipaikka, String AsiakkaanPostinumero, String Asiakkansposti, double Summa) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO laskut(laskuID, varausID, asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email, summa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        ps.setInt(1, laskuID);
        ps.setInt(2, varausID);
        ps.setInt(3, AsiakasID);
        ps.setString(4, AsiakkaanEtunimi);
        ps.setString(5, AsiakkaanSukunimi);
        ps.setString(6, AsiakkaanLahiosoite);
        ps.setString(7, AsiakkaanPostioimipaikka);
        ps.setString(8, AsiakkaanPostinumero);
        ps.setString(9, Asiakkansposti);
        ps.setDouble(10, Summa);

        ps.execute();

        Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
        about.setTitle("Lasku lisätty");
        about.setHeaderText("Lasku [" + laskuID + "] " + "\nlisätty onnistuneesti");
        about.show();

    }

    /**
     * Asiakkaan tietojenhaku metodi
     *
     * @param c Yhteys
     * @param txtAsiakasid Asiakkaan id
     * @param txtAsiakasEtunimi Asiakkaan etunimi
     * @param txtAsiakasSukunimi Asiakkaan sukunimi
     * @param txtAsiakkaanosoite Asiakkaan lähiosoite
     * @param txtAsiakasPostitoimipaikka Asiakkaan postitoimipaikka
     * @param txtAsiakasPostinumero Asiakkaan postinumero
     * @param txtAsiakkansposti Asiakkaan sähköposti
     * @throws SQLException
     */
    private static void haeLaskuAsiakas(Connection c, TextField txtAsiakasid, TextField txtAsiakasEtunimi, TextField txtAsiakasSukunimi, TextField txtAsiakkaanosoite,
            TextField txtAsiakasPostitoimipaikka, TextField txtAsiakasPostinumero, TextField txtAsiakkansposti) throws SQLException {

        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = stmt.executeQuery("SELECT asiakasID, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, email FROM asiakas WHERE asiakasID = " + txtAsiakasid.getText() + "");

        rs.absolute(1);

        txtAsiakasid.setText(rs.getString("asiakasid"));

        txtAsiakasEtunimi.setText(rs.getString("etunimi"));

        txtAsiakasSukunimi.setText(rs.getString("sukunimi"));

        txtAsiakkaanosoite.setText(rs.getString("lahiosoite"));

        txtAsiakasPostitoimipaikka.setText(rs.getString("postitoimipaikka"));

        txtAsiakasPostinumero.setText(rs.getString("postinro"));

        txtAsiakkansposti.setText(rs.getString("email"));

    }

    /**
     * Palvelun tietojenhaku metodi
     *
     * @param c Yhteys
     * @param txtPalveluID Palvelun id
     * @param txtPalvelunimi Palvelunnimi
     * @param txtPalvelunselite Palvelun selite
     * @param txtHinta Palvelun hinta
     * @throws SQLException Virheentunnistus
     */
    private static void haeLaskuPalvelu(Connection c, TextField txtPalveluID, TextField txtPalvelunimi, TextField txtPalvelunselite, TextField txtHinta) throws SQLException {

        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = stmt.executeQuery("SELECT palveluID, palvelunimi, palvelutyyppi, hinta FROM palvelut WHERE palveluID = " + txtPalveluID.getText() + "");

        rs.absolute(1);

        txtPalveluID.setText(rs.getString("palveluID"));

        txtPalvelunimi.setText(rs.getString("palvelunimi"));

        txtPalvelunselite.setText(rs.getString("palvelutyyppi"));

        txtHinta.setText(rs.getString("hinta"));

    }
    
    /** 
     * Varausid:n hakumetodi 
     * 
     * @param c Yhteys
     * @param txtAsiakasid Asiakkaan id
     * @param txtVarausID Varausken id
     * @throws SQLException Virheentunnistus
     */
    private static void haeLaskuVaraus(Connection c,TextField txtAsiakasid, TextField txtVarausID) throws SQLException {

        Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet rs = stmt.executeQuery("SELECT varausID FROM varaukset WHERE asiakasID = " + txtAsiakasid.getText() + "");

        rs.absolute(1);

        txtVarausID.setText(rs.getString("varausID"));

    }

    /** Laskun lähetysnapin toiminnallisuus
     * 
     * @param event ActionHandleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnLahetaLasku(ActionEvent event) throws SQLException {

        if(radSahkoposti.isSelected()){
            
            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Lähetetty");
            about.setHeaderText("Sähköpostilasku lähetetty onnistuneesti!");
            about.show();
        }
        if(radPaperi.isSelected()){
            
            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Lähetetty");
            about.setHeaderText("Paperilasku lähetetty onnistuneesti!");
            about.show();
        }
       
    }

    /**
     * Painike asiakkaan tietojen hakuun
     *
     * @param event ActionHandleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnHaeAsiakas(ActionEvent event) throws SQLException {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            try {
                AsiakasID = Integer.parseInt(txtAsiakasid.getText());
                AsiakkaanEtunimi = txtAsiakasEtunimi.getText();
                AsiakkaanSukunimi = txtAsiakasSukunimi.getText();
                AsiakkaanLahiosoite = txtAsiakkaanosoite.getText();
                AsiakkaanPostioimipaikka = txtAsiakasPostitoimipaikka.getText();
                AsiakkaanPostinumero = txtAsiakasPostinumero.getText();
                Asiakkansposti = txtAsiakkansposti.getText();

            } catch (Exception e) {

                System.out.println("Syöte väärässä muodossa:" + e);

            }
            haeLaskuAsiakas(conn, txtAsiakasid, txtAsiakasEtunimi, txtAsiakasSukunimi, txtAsiakkaanosoite, txtAsiakasPostitoimipaikka, txtAsiakasPostinumero, txtAsiakkansposti);

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
     * Painike laskuntietojen esikatseluun
     *
     * @param event Action handleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnLaskunEsikatselu(ActionEvent event) throws SQLException {
        try {
            Connection conn = openConnection(
                    "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                    + "3306?user=opiskelija&password=opiskelija1");

            useDatabase(conn, "r8_vuokratoimistot");

            try {

                File source = new File("./Reports/lasku_template.html");
                File dest = new File("./Reports/lasku100.html");

                Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);

                AsiakkaanEtunimi = txtAsiakasEtunimi.getText() + " " + txtAsiakasSukunimi.getText();
                AsiakkaanLahiosoite = txtAsiakkaanosoite.getText();
                Asiakkansposti = txtAsiakkansposti.getText();
                Laskunlahetyspvm = txtLaskunlahetyspvm.getText();
                Laskurerapvm = txtLaskurerapvm.getText();

                Palvelu = txtPalvelunimi.getText();
                Palvelunselite = txtPalvelunselite.getText();
                Hinta = Double.parseDouble(txtHinta.getText());
                Maara = Integer.parseInt(txtMaara.getText());
                Summa = Hinta * Maara;

                String content = new String(Files.readAllBytes(dest.toPath()), UTF_8);
                content = content.replaceAll(CUSTOMER_NAME, AsiakkaanEtunimi);
                content = content.replaceAll(CUSTOMER_ADDRESS, AsiakkaanLahiosoite);
                content = content.replaceAll(CUSTOMER_EMAIL, Asiakkansposti);
                content = content.replaceAll(INVOICE_DATE, Laskunlahetyspvm);
                content = content.replaceAll(INVOICE_DUE_DATE, Laskurerapvm);
                content = content.replaceAll(TABLE_CONTENT, createRow(Palvelu, Palvelunselite, String.valueOf(Hinta), String.valueOf(Maara), String.valueOf(Summa)));

                Files.write(dest.toPath(), content.getBytes(UTF_8));

                loadWebPage(dest.toPath().toString());

            } catch (Exception e) {
                System.out.println("Syöte väärässä muodossa:" + e);
                closeConnection(conn);
            }

        } catch (SQLException e) {
            System.out.println("Tapahtui virhe:" + e);
        }

    }

    /**
     * Palvelun tietojenhaku painike
     *
     * @param event ActionHandleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnHaepalvelu(ActionEvent event) throws SQLException {
        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        haeLaskuPalvelu(conn, txtPalveluID, txtPalvelunimi, txtPalvelunselite, txtHinta);
        haeLaskuVaraus(conn, txtAsiakasid, txtVarausID);
        closeConnection(conn);
    }

    /**
     * Laskun tietojen lisäys painike
     *
     * @param event ActionHandleri
     * @throws SQLException Virheentunnistus
     */
    @FXML
    private void btnLaskunLisays(ActionEvent event) throws SQLException {
        Connection conn = openConnection(
                "jdbc:mariadb://maria.westeurope.cloudapp.azure.com:"
                + "3306?user=opiskelija&password=opiskelija1");

        useDatabase(conn, "r8_vuokratoimistot");

        laskuID = Integer.parseInt(txtLaskuid.getText());
        varausID = Integer.parseInt(txtVarausID.getText());
        AsiakasID = Integer.parseInt(txtAsiakasid.getText());
        AsiakkaanEtunimi = txtAsiakasEtunimi.getText();
        AsiakkaanSukunimi = txtAsiakasSukunimi.getText();
        AsiakkaanLahiosoite = txtAsiakkaanosoite.getText();
        AsiakkaanPostioimipaikka = txtAsiakasPostitoimipaikka.getText();
        AsiakkaanPostinumero = txtAsiakasPostinumero.getText();
        Asiakkansposti = txtAsiakkansposti.getText();
        Summa = Hinta * Maara;

        try {
            addLasku(conn, laskuID, varausID, AsiakasID, AsiakkaanEtunimi, AsiakkaanSukunimi, AsiakkaanLahiosoite, AsiakkaanPostioimipaikka, AsiakkaanPostinumero, Asiakkansposti, Summa);

        } catch (SQLException e) {

            System.out.println("Syöte väärässä muodossa:" + e);

            Alert about = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            about.setTitle("Virhe!");
            about.setHeaderText("LaskuID on jo käytössä!");
            about.show();
        }

        closeConnection(conn);
    }

}

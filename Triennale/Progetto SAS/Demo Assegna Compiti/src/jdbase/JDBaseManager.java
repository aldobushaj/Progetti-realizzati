package jdbase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class JDBaseManager {

    private static JDBaseManager manager;
    //ROBA DEL DATABASE
    public static final String DATABASE_PATH = "jdbc:sqlite:assegna_compiti.db";

    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public static final String TABLE_PREPARAZIONE = "TABLE_PREPARAZIONE";
    public static final String TABLE_PREPARAZIONI_DISPONIBILI = "TABLE_PREPARAZIONI_DISPONIBILI";
    public static final String TABLE_RICETTA = "TABLE_RICETTA";
    public static final String TABLE_COMPOSIZIONE_RICETTA = "TABLE_COMPOSIZIONE_RICETTA";
    public static final String TABLE_MENU = "TABLE_MENU";
    public static final String TABLE_COMPOSIZIONE_MENU = "TABLE_COMPOSIZIONE_MENU";
    public static final String TABLE_FOGLIO_RIEPILOGATIVO = "TABLE_FOGLIO_RIEPILOGATIVO";
    public static final String TABLE_COMPITI = "TABLE_COMPITI";
    public static final String TABLE_EVENTO = "TABLE_EVENTO";
    public static final String TABLE_CUOCHI = "TABLE_CUOCHI";
    public static final String TABLE_TURNI = "TABLE_TURNI";
    

    public static final String TABLE_PREPARAZIONE_ID = "TABLE_PREPARAZIONE_ID";
    public static final String TABLE_PREPARAZIONE_DESCRIZIONE = "TABLE_PREPARAZIONE_DESCRIZIONE";

    public static final String TABLE_PREPARAZIONI_DISPONIBILI_ID = "TABLE_PREPARAZIONI_DISPONIBILI_ID";
    public static final String TABLE_PREPARAZIONI_DISPONIBILI_QUANTITA_DISPONIBILI = "TABLE_PREPARAZIONI_DISPONIBILI_QUANTITA_DISPONIBILI";

    public static final String TABLE_RICETTA_ID = "TABLE_RICETTA_ID";
    public static final String TABLE_RICETTA_NOME = "TABLE_RICETTA_NOME";
    public static final String TABLE_RICETTA_DESCRIZIONE = "TABLE_RICETTA_DESCRIZIONE";

    public static final String TABLE_COMPOSIZIONE_RICETTA_ID_RICETTA = "TABLE_COMPOSIZIONE_RICETTA_ID_RICETTA";
    public static final String TABLE_COMPOSIZIONE_RICETTA_ID_PREPARAZIONE = "TABLE_COMPOSIZIONE_RICETTA_ID_PREPARAZIONE";
    public static final String TABLE_COMPOSIZIONE_RICETTA_QUANTITA = "TABLE_COMPOSIZIONE_RICETTA_QUANTITA";

    public static final String TABLE_MENU_ID = "TABLE_MENU_ID";
    public static final String TABLE_MENU_TITOLO = "TABLE_MENU_TITOLO";
    public static final String TABLE_MENU_PUBBLICATO = "TABLE_MENU_PUBBLICATO"; //boolean
    public static final String TABLE_MENU_CUOCO_CONSIGLIATO = "TABLE_MENU_CUOCO_CONSIGLIATO"; //boolean
    public static final String TABLE_MENU_PIATTI_CALDI = "TABLE_MENU_PIATTI_CALDI"; //boolean
    public static final String TABLE_MENU_RICHIEDE_CUCINA = "TABLE_MENU_RICHIEDE_CUCINA"; //boolean
    public static final String TABLE_MENU_BUFFET = "TABLE_MENU_BUFFET"; //boolean
    public static final String TABLE_MENU_FINGER_FOOD = "TABLE_MENU_FINGER_FOOD"; //boolean

    public static final String TABLE_COMPOSIZIONE_MENU_ID_MENU = "TABLE_COMPOSIZIONE_MENU_ID_MENU";
    public static final String TABLE_COMPOSIZIONE_MENU_ID_RICETTA = "TABLE_COMPOSIZIONE_MENU_ID_RICETTA";

    public static final String TABLE_FOGLIO_RIEPILOGATIVO_ID_EVENTO = "TABLE_FOGLIO_RIEPILOGATIVO_ID_EVENTO";
    public static final String TABLE_FOGLIO_RIEPILOGATIVO_ID_COMPITI = "TABLE_FOGLIO_RIEPILOGATIVO_ID_COMPITI";

    public static final String TABLE_COMPITI_ID = "TABLE_COMPITI_ID";
    public static final String TABLE_COMPITI_ID_EVENTO = "TABLE_COMPITI_ID_EVENTO";
    public static final String TABLE_COMPITI_ID_PROCEDURA = "TABLE_COMPITI_ID_PROCEDURA";
    public static final String TABLE_COMPITI_N_PORZIONI = "TABLE_COMPITI_N_PORZIONI";
    public static final String TABLE_COMPITI_STIMA_TEMPO = "TABLE_COMPITI_STIMA_TEMPO";
    public static final String TABLE_COMPITI_TIPO_PROCEDURA = "TABLE_COMPITI_TIPO_PROCEDURA";
    public static final String TABLE_COMPITI_QUANTITATIVO = "TABLE_COMPITI_QUANTITATIVO";
    public static final String TABLE_COMPITI_PRIORITA = "TABLE_COMPITI_PRIORITA"; //+ è bassa e + è importante

    public static final String TABLE_EVENTO_ID = "TABLE_EVENTO_ID";
    public static final String TABLE_EVENTO_NOME = "TABLE_EVENTO_NOME";
    public static final String TABLE_EVENTO_DATA = "TABLE_EVENTO_DATA";
    public static final String TABLE_EVENTO_ID_MENU = "TABLE_EVENTO_ID_MENU";
    public static final String TABLE_EVENTO_FOGLIO_RIEPILOGATIVO = "TABLE_EVENTO_FOGLIO_RIEPILOGATIVO";
    
    public static final String TABLE_CUOCHI_ID_CUOCO = "TABLE_CUOCHI_ID_CUOCO";
    public static final String TABLE_CUOCHI_NOME_CUOCO = "TABLE_CUOCHI_NOME_CUOCO";
    
    public static final String TABLE_TURNI_ID_CUOCO = "TABLE_TURNI_ID_CUOCO";
    public static final String TABLE_TURNI_ID_COMPITO = "TABLE_TURNI_ID_COMPITO";
    public static final String TABLE_TURNI_DATA_INIZIO = "TABLE_TURNI_DATA_INIZIO";
    public static final String TABLE_TURNI_DATA_FINE = "TABLE_TURNI_DATA_FINE";

    private Connection connection;

    private JDBaseManager() {
    }

    public static JDBaseManager getInstance() {
        if (manager == null) {
            manager = new JDBaseManager();
            manager.initDatabase();
        }
        return manager;
    }

    public Connection getConnection() {
        return connection;
    }

    public void openConnection() {
        if (connection == null) {
            try {
                //creo connessione al database
                connection = DriverManager.getConnection(JDBaseManager.DATABASE_PATH);
                System.out.println("La connessione a SQLite: " + DATABASE_PATH + " è stata eseguita con successo.");
            } catch (SQLException e) {
                System.out.println("La connessione a SQLite NON è stata eseguita con successo.");
            }
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("La connessione con SQLite Database è stata chiusa.");
            } catch (SQLException ex) {
                System.out.println("Errore chiusura connessione con SQLite Database.");
            }
        }
    }

    private void initDatabase() {
        openConnection();
        try {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                initTables();
            }
        } catch (SQLException e) {
            System.out.println("A new database has NOT been created: " + e.getMessage());
        }
    }

    private void initTables() {
        Statement stmt;
        String sql;
        System.out.println("Creazione tabelle in corso...");
        try {
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_PREPARAZIONE + "(" //tabella preparazione
                    + TABLE_PREPARAZIONE_ID + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_PREPARAZIONE_DESCRIZIONE + " text NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_RICETTA + "(" //tabella ricetta
                    + TABLE_RICETTA_ID + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_RICETTA_NOME + " text NOT NULL,"
                    + TABLE_RICETTA_DESCRIZIONE + " text NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_PREPARAZIONI_DISPONIBILI + "(" //tabella preparazioni_disponibili;
                    + TABLE_PREPARAZIONI_DISPONIBILI_ID + " integer PRIMARY KEY REFERENCES " + TABLE_PREPARAZIONE + "(" + TABLE_PREPARAZIONE_ID + "),"
                    + TABLE_PREPARAZIONI_DISPONIBILI_QUANTITA_DISPONIBILI + " integer NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPOSIZIONE_RICETTA + "(" //tabella composizione_ricetta
                    + TABLE_COMPOSIZIONE_RICETTA_ID_RICETTA + " integer NOT NULL REFERENCES " + TABLE_RICETTA + "(" + TABLE_RICETTA_ID + "),"
                    + TABLE_COMPOSIZIONE_RICETTA_ID_PREPARAZIONE + " integer NOT NULL REFERENCES " + TABLE_PREPARAZIONE + "(" + TABLE_PREPARAZIONE_ID + "),"
                    + TABLE_COMPOSIZIONE_RICETTA_QUANTITA + " integer NOT NULL,"
                    + "PRIMARY KEY(" + TABLE_COMPOSIZIONE_RICETTA_ID_RICETTA + "," + TABLE_COMPOSIZIONE_RICETTA_ID_PREPARAZIONE + "));";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_MENU + "(" //tabella menu;
                    + TABLE_MENU_ID + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_MENU_TITOLO + " text NOT NULL,"
                    + TABLE_MENU_PUBBLICATO + " integer NOT NULL,"
                    + TABLE_MENU_CUOCO_CONSIGLIATO + " integer NOT NULL,"
                    + TABLE_MENU_PIATTI_CALDI + " integer NOT NULL,"
                    + TABLE_MENU_RICHIEDE_CUCINA + " integer NOT NULL,"
                    + TABLE_MENU_BUFFET + " integer NOT NULL,"
                    + TABLE_MENU_FINGER_FOOD + " integer NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPOSIZIONE_MENU + "(" //tabella composizione_menu
                    + TABLE_COMPOSIZIONE_MENU_ID_MENU + " integer NOT NULL REFERENCES " + TABLE_MENU + "(" + TABLE_MENU_ID + "),"
                    + TABLE_COMPOSIZIONE_MENU_ID_RICETTA + " integer NOT NULL REFERENCES " + TABLE_RICETTA + "(" + TABLE_RICETTA_ID + "),"
                    + "PRIMARY KEY(" + TABLE_COMPOSIZIONE_MENU_ID_MENU + "," + TABLE_COMPOSIZIONE_MENU_ID_RICETTA + "));";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTO + "(" //tabella evento;
                    + TABLE_EVENTO_ID + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_EVENTO_ID_MENU + " integer NOT NULL REFERENCES " + TABLE_MENU + "(" + TABLE_MENU_ID + "),"
                    + TABLE_EVENTO_FOGLIO_RIEPILOGATIVO + " integer DEFAULT 0 NOT NULL,"
                    + TABLE_EVENTO_NOME + " text NOT NULL,"
                    + TABLE_EVENTO_DATA + " text NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPITI + "(" //tabella compiti;
                    + TABLE_COMPITI_ID + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_COMPITI_ID_EVENTO + " integer NOT NULL REFERENCES " + TABLE_EVENTO + "(" + TABLE_EVENTO_ID + "),"
                    + TABLE_COMPITI_ID_PROCEDURA + " integer NOT NULL,"
                    + TABLE_COMPITI_TIPO_PROCEDURA + " integer NOT NULL,"
                    + TABLE_COMPITI_N_PORZIONI + " integer NOT NULL,"
                    + TABLE_COMPITI_STIMA_TEMPO + " integer NOT NULL,"
                    + TABLE_COMPITI_QUANTITATIVO + " integer DEFAULT 0 NOT NULL,"
                    + TABLE_COMPITI_PRIORITA + " integer DEFAULT 0 NOT NULL)"; 
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CUOCHI + "(" //tabella cuochi;
                    + TABLE_CUOCHI_ID_CUOCO + " integer PRIMARY KEY AUTOINCREMENT,"
                    + TABLE_CUOCHI_NOME_CUOCO + " text NOT NULL);";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TURNI + "(" //tabella turni;
                    + TABLE_TURNI_ID_CUOCO + " integer " + TABLE_CUOCHI  + ","
                    + TABLE_TURNI_ID_COMPITO + " integer NOT NULL REFERENCES " + TABLE_COMPITI + "(" + TABLE_COMPITI_ID + "),"
                    + TABLE_TURNI_DATA_INIZIO + " date NOT NULL,"
                    + TABLE_TURNI_DATA_FINE + " date NOT NULL,"
                    + "PRIMARY KEY(" + TABLE_TURNI_ID_CUOCO + "," + TABLE_TURNI_DATA_INIZIO + "," + TABLE_TURNI_DATA_FINE + "));";
                   // + "PRIMARY KEY(" + TABLE_TURNI_ID_CUOCO + "," + TABLE_TURNI_ID_COMPITO + "));";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.execute(sql);
            
            System.out.println("Sono state create le tabelle.");

            //POPOLO LE TABELLE
            populateTables();

        } catch (SQLException e) {
            System.out.println("Errore generazione nelle tabelle: " + e.getMessage());
        }
    }

    private void populateTables() {
        System.out.println("Popolamento tabelle in corso...");
        int cont = 0;
        if (notPopulated(JDBaseManager.TABLE_PREPARAZIONE)) {
            eseguiQuery(sqlInsertPreparazione("Pane"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Speck"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Prataioli"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Maionese"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Mazzancolle"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Salsafuoco"));
            cont++;
            eseguiQuery(sqlInsertPreparazione("Mascarpone"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Ricotta"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Olive"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Patatine"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Seppia"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Insalata"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Cernia"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Baccalà"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Cipolla Carammellata"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Mele Tagliate"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Fragole Frullate"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Pasta Frolla"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Ananas A Rondelle"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Cioccolato Fuso"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Costine Di Maiale"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Patate Al Forno"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Carote Tagliate"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Funghi Porcini"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Pollo"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Semi Di Papavero"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Pistacchi"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Uvetta"));
            cont++;
             eseguiQuery(sqlInsertPreparazione("Pinoli"));
            cont++;
        }
        System.out.println("Popolata TABLE_PREPARAZIONE con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_PREPARAZIONI_DISPONIBILI)) {
            eseguiQuery(sqlInsertPreparazioneDisponibile(1, 10));
            cont++;
            eseguiQuery(sqlInsertPreparazioneDisponibile(5, 42));
            cont++;
            eseguiQuery(sqlInsertPreparazioneDisponibile(2, 1));
            cont++;
        }
        System.out.println("Popolata TABLE_PREPARAZIONI_DISPONIBILI con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_RICETTA)) {
            eseguiQuery(sqlInsertRicetta("Crostoni di pane con speck e prataioli", "Pane tostato con un filo d’olio. Farcito con del formaggio morbido, dello speck e dei prataioli. Il tutto decorato con erba cipollina."));
            cont++;
            eseguiQuery(sqlInsertRicetta("S'panito con maionese e mazzancolle", "Maionese aggiunta col sacco a poche sullo s’panito, servito con sopra una mazzancolla condita con olio e prezzemolo."));
            cont++;
            eseguiQuery(sqlInsertRicetta("Bicchierini mascarpone e salsafuoco", " Mascarpone (o la ricotta) con l’erba cipollina col sac-a-poche alternato ad uno strato di mascarpone con la gransalsa di cuori di carciofo e terminato con uno strato di salsa di fuoco."));
            cont++;
            eseguiQuery(sqlInsertRicetta("Salatini", "Un insieme di gustosi salatini, patatine e olive"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Insalata seppia arlecchino", "Un'insalata di mare a base di seppia appena pescata"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Guazzetto di cernia allo zafferano", "Risotto con sapori del Nord e del Sud con aggiunta di erbe dell'Abbruzzo"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Baccalà fritto con cipolla caramellata", "Il sapore del baccalà avvoloto in una cipolla caramellata e aromatizzata nel peperoncino"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Spiedini Di Frutta", "Fantasie di frutta"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Torta Di Mele", "Mele del Trentino avvolte in pasta frolla di fragole"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Ananas Al Cioccolato", "Ruote di ananas ricoperte di crema al ciccolato"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Costine fumè con patate", "Tenera carne affumicata con contorno di patate al forno"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Sfornato di carote e speck con salsa ai porcini", "Speck dell'Alto Adige arrotolato in una crema di porcini e carote"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Pollo e papavero", "Petti di polli al forno decorati con semi di papaveri croccanti"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Hamburger Vegetariano", "Variante del classico hamburger a base di verdure"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Tortino Di Verdure", "Mix di verdure cotte al vapore di forma circolare"));
            cont++;
            eseguiQuery(sqlInsertRicetta("Insalata autunnale", "Pistacchi,pinoli e uvetta all'insalata"));
            cont++;
        }
        System.out.println("Popolata TABLE_RICETTA con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_COMPOSIZIONE_RICETTA)) {
            eseguiQuery(sqlInsertComposizioneRicetta(1, 1, 50));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(1, 2, 100));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(1, 3, 50));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(2, 4, 150));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(2, 5, 200));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(3, 6, 250));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(3, 7, 400));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(3, 8, 100));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(4, 9, 50));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(4, 10, 250));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(5, 11, 450));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(5, 12, 400));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(6, 13, 600));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(7, 14, 350));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(7, 15, 400));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(9, 16, 250));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(9, 17, 750));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(9, 18, 900));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(10, 19, 200));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(10, 20, 30));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(11, 21, 40));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(11, 22, 45));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(12, 2, 80));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(12, 23, 60));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(12, 24, 50));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(13, 25, 100));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(13, 26, 250));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(14, 9, 30));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(14, 12, 650));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(14, 23, 90));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(15, 12, 200));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(15, 22, 150));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(15, 23, 100));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(16, 27, 90));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(16, 28, 40));
            cont++;
            eseguiQuery(sqlInsertComposizioneRicetta(16, 29, 90));
            cont++;
        }
        System.out.println("Popolata TABLE_COMPOSIZIONE_RICETTA con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_MENU)) {
            eseguiQuery(sqlInsertMenu("Aperitivo", 0, 0, 0, 0, 1, 0));
            cont++;
            eseguiQuery(sqlInsertMenu("Menù Di Carne", 0, 0, 0, 0, 1, 0));
            cont++;
            eseguiQuery(sqlInsertMenu("Menù Vegano", 0, 0, 0, 0, 0, 0));
            cont++;
            eseguiQuery(sqlInsertMenu("Buffet Di Frutta", 0, 0, 0, 0, 0, 0));
            cont++;
            eseguiQuery(sqlInsertMenu("Menù Di Mare", 0, 0, 0, 0, 0, 0));
            cont++;
        }
        System.out.println("Popolata TABLE_MENU con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_COMPOSIZIONE_MENU)) {
            eseguiQuery(sqlInsertComposizioneMenu(1, 1));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(1, 2));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(1, 3));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(1, 4));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(5, 5));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(5, 6));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(5, 7));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(4, 8));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(4, 9));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(4, 10));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(2, 11));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(2, 12));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(2, 13));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(3, 14));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(3, 15));
            cont++;
            eseguiQuery(sqlInsertComposizioneMenu(3, 16));
            cont++;
        }
        System.out.println("Popolata TABLE_COMPOSIZIONE_MENU con " + cont + " records");
        cont = 0;
        //QUESTA POPOLAZIONE DA PROBLEMI, LASCIAMO LA GESTIONE AL DRIVER, E ALL'UTENTE
        /*if(notPopulated(JDBaseManager.TABLE_COMPITI)){
            eseguiQuery(insertCompito(1, 3, 4 ,1, 30, 0));
            eseguiQuery(insertCompito(1, 2, 3, 1, 700, 0));
            eseguiQuery(insertCompito(1, 5, 2, 0, 700, 0));
            eseguiQuery(insertCompito(1, 2, 5, 0, 700, 0));
            eseguiQuery(insertCompito(2, 3, 4, 1, 30, 0));
            eseguiQuery(insertCompito(2, 2, 3, 1, 85, 0));
            eseguiQuery(insertCompito(5, 6, 6, 1, 0, 0));
            System.out.println("Popolata TABLE_COMPITI con 7 records");
        }*/
        if (notPopulated(JDBaseManager.TABLE_EVENTO)) {
            eseguiQuery(sqlInsertEvento("Matrimonio Ale&JD", Date.valueOf("2019-05-11"), 1));
            cont++;
            eseguiQuery(sqlInsertEvento("Festa Patronale Mellana", Date.valueOf("2019-07-17"), 2));
            cont++;
            eseguiQuery(sqlInsertEvento("Inaugurazione Lavazza", Date.valueOf("2019-11-19"), 3));
            cont++;
            eseguiQuery(sqlInsertEvento("Compleanno Massimo Boldi", Date.valueOf("2019-12-25"), 4));
            cont++;
            eseguiQuery(sqlInsertEvento("Festa Di Benvenuto Al Rettore", Date.valueOf("2020-01-23"), 5));
            cont++;
        }
        System.out.println("Popolata TABLE_EVENTO con " + cont + " records");
        cont = 0;
        if (notPopulated(JDBaseManager.TABLE_CUOCHI)) {
            eseguiQuery(sqlInsertCuoco("Antonino Cannavacciuolo"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Bruno Barbieri"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Carlo Cracco"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Joe Bastianich"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Gordon Ramsay"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Gualtiero Marchesi"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Alessandro Borghese"));
            cont++;
            eseguiQuery(sqlInsertCuoco("Cristina Bowerman"));
            cont++;
        }
        System.out.println("Popolata TABLE_CUOCHI con " + cont + " records");
        System.out.println("Popolamento tabelle terminato.");
    }

    private boolean notPopulated(String table) {
        boolean nPopulated = true;
        String sql = "SELECT count(*) as pop FROM " + table;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                if (rs.getInt("pop") > 0) {
                    nPopulated = false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in norPopulated: " + e.getMessage());
        }
        return nPopulated;
    }

    public String sqlInsertEvento(String nome, Date data, int idmenu) {
        return "INSERT INTO " + TABLE_EVENTO + " (" + TABLE_EVENTO_NOME + "," + TABLE_EVENTO_DATA + "," + TABLE_EVENTO_ID_MENU + ") VALUES('" + nome.replace("'", "''") + "','" + data.toString() + "'," + idmenu + ");";
    }

    public String sqlInsertMenu(String titolo, int pub, int cuocoConsigliato, int pc, int rc, int buffet, int ff) {
        return "INSERT INTO " + TABLE_MENU + " (" + TABLE_MENU_TITOLO + "," + TABLE_MENU_PUBBLICATO + "," + TABLE_MENU_CUOCO_CONSIGLIATO + "," + TABLE_MENU_PIATTI_CALDI + "," + TABLE_MENU_RICHIEDE_CUCINA + "," + TABLE_MENU_BUFFET + "," + TABLE_MENU_FINGER_FOOD + ") VALUES('" + titolo.replace("'", "''") + "'," + pub + "," + cuocoConsigliato + "," + pc + "," + rc + "," + buffet + "," + ff + ");";
    }

    public String sqlInsertRicetta(String nome, String descrizione) {
        return "INSERT INTO " + TABLE_RICETTA + " (" + TABLE_RICETTA_NOME + "," + TABLE_RICETTA_DESCRIZIONE + ") VALUES('" + nome.replace("'", "''") + "','" + descrizione.replace("'", "''") + "');";
    }

    public String sqlInsertPreparazione(String preparazione) {
        return "INSERT INTO " + TABLE_PREPARAZIONE + " (" + TABLE_PREPARAZIONE_DESCRIZIONE + ") VALUES('" + preparazione.replace("'", "''") + "');";
    }

    public String sqlInsertCompitoRicetta(int idevento, int idprocedura, int isricetta, int nporz, int stimaTempo) {
        return "INSERT INTO " + TABLE_COMPITI + " (" + TABLE_COMPITI_ID_EVENTO + "," + TABLE_COMPITI_ID_PROCEDURA + "," + TABLE_COMPITI_TIPO_PROCEDURA + "," + TABLE_COMPITI_N_PORZIONI + "," + TABLE_COMPITI_STIMA_TEMPO + ") VALUES(" + idevento + "," + idprocedura + "," + isricetta + "," + nporz + "," + stimaTempo + ");";
    }

    public String sqlInsertCompitoPreparazione(int idevento, int idprocedura, int isricetta, int nporz, int stimaTempo, int quantitativo) {
        return "INSERT INTO " + TABLE_COMPITI + " (" + TABLE_COMPITI_ID_EVENTO + "," + TABLE_COMPITI_ID_PROCEDURA + "," + TABLE_COMPITI_TIPO_PROCEDURA + "," + TABLE_COMPITI_N_PORZIONI + "," + TABLE_COMPITI_STIMA_TEMPO + "," + TABLE_COMPITI_QUANTITATIVO +") VALUES(" + idevento + "," + idprocedura + "," + isricetta + "," + nporz + "," + stimaTempo + "," + quantitativo + ");";
    }
    
    public String sqlInsertComposizioneMenu(int idmenu, int idricetta) {
        return "INSERT INTO " + TABLE_COMPOSIZIONE_MENU + " VALUES (" + idmenu + "," + idricetta + ");";
    }

    public String sqlInsertComposizioneRicetta(int idricetta, int idpreparazione, int quantita) {
        return "INSERT INTO " + TABLE_COMPOSIZIONE_RICETTA + " VALUES (" + idricetta + "," + idpreparazione + "," + quantita + ");";
    }

    public String sqlInsertPreparazioneDisponibile(int idpreparazione, int quant_disponibili) {
        return "INSERT INTO " + TABLE_PREPARAZIONI_DISPONIBILI + " VALUES (" + idpreparazione + "," + quant_disponibili + ");";
    }
    
    public String sqlInsertCuoco(String nomeCuoco){
        return "INSERT INTO " + TABLE_CUOCHI + " (" + TABLE_CUOCHI_NOME_CUOCO + ") VALUES ( '" + nomeCuoco + "' );";
    }

    public boolean eseguiQuery(String sql) {
        boolean result;
        try {
            Statement stmt = manager.getConnection().createStatement();
            result = stmt.execute(sql);
        } catch (SQLException ex) {
            System.out.println("Errore in eseguiQuery() -> fallita esecuzione query: " + sql + "\n Motivazione: " + ex.getMessage());
            result = false;
        }
        return result;
    }
}
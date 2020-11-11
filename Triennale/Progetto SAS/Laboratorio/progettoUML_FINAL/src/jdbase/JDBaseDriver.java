package jdbase;

import bestutilsever.Task;
import bestutilsever.Cook;
import bestutilsever.Event;
import bestutilsever.ReviewPaper;
import bestutilsever.Menu;
import bestutilsever.Preparation;
import bestutilsever.Procedure;
import bestutilsever.Recipe;
import bestutilsever.WorkShift;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author gianluca && christian && diego
 */
public final class JDBaseDriver {

    private static JDBaseDriver driver;
    private final JDBaseManager manager;

    private JDBaseDriver() {
        manager = JDBaseManager.getInstance();
    }

    public static JDBaseDriver getInstance() {
        if (driver == null) {
            driver = new JDBaseDriver();
        }
        return driver;
    }

    public void closeConnection() {
        manager.closeConnection();
    }

    public ArrayList<Event> selectEventi() {
        ArrayList<Event> eventi = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_EVENTO;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ReviewPaper fr = selectFoglioRiepilogativoByID(rs.getInt(JDBaseManager.TABLE_EVENTO_ID));
                Event eve = new Event(rs.getInt(JDBaseManager.TABLE_EVENTO_ID), selectMenuByID(rs.getInt(JDBaseManager.TABLE_EVENTO_ID_MENU)), fr, rs.getString(JDBaseManager.TABLE_EVENTO_NOME), rs.getString(JDBaseManager.TABLE_EVENTO_DATA));
                if (fr == null) { //se il foglio non esiste ancora..
                    activateFoglioRiepilogativo(rs.getInt(JDBaseManager.TABLE_EVENTO_ID));
                    saveCompitiMenu(eve.getMenu(), rs.getInt(JDBaseManager.TABLE_EVENTO_ID));
                    eve.setFoglioRiepilogativo(selectFoglioRiepilogativoByID(eve.getIdEvento()));
                }
                eventi.add(eve);
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectEventi: " + e.getMessage());
        }
        return eventi;
    }

    private Menu selectMenuByID(int menuID) {
        Menu menu = null;
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_MENU
                + " WHERE " + JDBaseManager.TABLE_MENU_ID + " = " + menuID;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                menu = new Menu(rs.getInt(JDBaseManager.TABLE_MENU_ID), rs.getString(JDBaseManager.TABLE_MENU_TITOLO), rs.getBoolean(JDBaseManager.TABLE_MENU_PUBBLICATO), rs.getBoolean(JDBaseManager.TABLE_MENU_CUOCO_CONSIGLIATO), rs.getBoolean(JDBaseManager.TABLE_MENU_PIATTI_CALDI), rs.getBoolean(JDBaseManager.TABLE_MENU_RICHIEDE_CUCINA), rs.getBoolean(JDBaseManager.TABLE_MENU_BUFFET), rs.getBoolean(JDBaseManager.TABLE_MENU_FINGER_FOOD), selectComposizioneMenu(rs.getInt(JDBaseManager.TABLE_MENU_ID)));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectMenuByID: " + e.getMessage());
        }
        return menu;
    }

    private ArrayList<Procedure> selectComposizioneMenu(int menuID) {
        ArrayList<Procedure> ricette = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_COMPOSIZIONE_MENU + " JOIN " + JDBaseManager.TABLE_RICETTA
                + " ON " + JDBaseManager.TABLE_COMPOSIZIONE_MENU_ID_RICETTA + " = " + JDBaseManager.TABLE_RICETTA_ID
                + " WHERE " + JDBaseManager.TABLE_COMPOSIZIONE_MENU_ID_MENU + " = " + menuID;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ricette.add(new Recipe(rs.getInt(JDBaseManager.TABLE_RICETTA_ID), rs.getString(JDBaseManager.TABLE_RICETTA_NOME), rs.getString(JDBaseManager.TABLE_RICETTA_DESCRIZIONE), getSubProcedure(rs.getInt(JDBaseManager.TABLE_RICETTA_ID))));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectComposizioneMenu: " + e.getMessage());
        }
        return ricette;
    }

    private ArrayList<Procedure> getSubProcedure(int ricettaID) {
        ArrayList<Procedure> subProcedure = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_COMPOSIZIONE_RICETTA + " JOIN " + JDBaseManager.TABLE_PREPARAZIONE
                + " ON " + JDBaseManager.TABLE_COMPOSIZIONE_RICETTA_ID_PREPARAZIONE + " = " + JDBaseManager.TABLE_PREPARAZIONE_ID
                + " WHERE " + JDBaseManager.TABLE_COMPOSIZIONE_RICETTA_ID_RICETTA + " = " + ricettaID;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                subProcedure.add(new Preparation(rs.getInt(JDBaseManager.TABLE_PREPARAZIONE_ID), rs.getInt(JDBaseManager.TABLE_COMPOSIZIONE_RICETTA_QUANTITA), rs.getString(JDBaseManager.TABLE_PREPARAZIONE_DESCRIZIONE)));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in getSubProcedure: " + e.getMessage());
        }
        return subProcedure;
    }

    private ReviewPaper selectFoglioRiepilogativoByID(int eventoId) {
        ReviewPaper fr = null;
        if (existsFoglioRiepilogativo(eventoId)) {
            fr = new ReviewPaper();

            String sql = "SELECT * FROM " + JDBaseManager.TABLE_COMPITI
                    + " WHERE " + JDBaseManager.TABLE_COMPITI_ID_EVENTO + " = " + eventoId;

            try {
                Statement stmt = manager.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                // loop through the result set
                while (rs.next()) {
                    ArrayList<WorkShift> turni = selectTurniByTask(rs.getInt(JDBaseManager.TABLE_COMPITI_ID));
                    Task compito;
                    Task sottoTask;
                    if (rs.getBoolean(JDBaseManager.TABLE_COMPITI_TIPO_PROCEDURA)) {
                        compito = new Task(rs.getInt(JDBaseManager.TABLE_COMPITI_ID), selectRicettaByID(rs.getInt(JDBaseManager.TABLE_COMPITI_ID_PROCEDURA)), rs.getInt(JDBaseManager.TABLE_COMPITI_N_PORZIONI), rs.getInt(JDBaseManager.TABLE_COMPITI_STIMA_TEMPO), null, rs.getInt(JDBaseManager.TABLE_COMPITI_PRIORITA));
                    } else {
                        Preparation prep = selectPreparazioneByID(rs.getInt(JDBaseManager.TABLE_COMPITI_ID_PROCEDURA));
                        prep.setAmount(rs.getInt(JDBaseManager.TABLE_COMPITI_QUANTITATIVO));
                        compito = new Task(rs.getInt(JDBaseManager.TABLE_COMPITI_ID), prep, rs.getInt(JDBaseManager.TABLE_COMPITI_N_PORZIONI), rs.getInt(JDBaseManager.TABLE_COMPITI_STIMA_TEMPO), null, rs.getInt(JDBaseManager.TABLE_COMPITI_PRIORITA));
                    }
                    compito.setWorkShifts(turni);
                    fr.addTask(compito);
                    //devo aggiungere anche le sub procedure del compito al foglio riepilogativo
                    for (Procedure subProcedura : compito.getProcedure().getSubProcedure()) {
                        sottoTask = new Task(rs.getInt(JDBaseManager.TABLE_COMPITI_ID), subProcedura, rs.getInt(JDBaseManager.TABLE_COMPITI_N_PORZIONI), rs.getInt(JDBaseManager.TABLE_COMPITI_STIMA_TEMPO), compito, 0);
                        compito.addObserver(sottoTask);
                        fr.addTask(sottoTask);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Errore lettura valori da Database in selectFoglioRiepilogativoByID: " + e.getMessage());
            }
        }
        return fr;
    }

    private Recipe selectRicettaByID(int ricID) {
        Recipe ricetta = null;
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_RICETTA
                + " WHERE " + JDBaseManager.TABLE_RICETTA_ID + " = " + ricID;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ricetta = new Recipe(rs.getInt(JDBaseManager.TABLE_RICETTA_ID), rs.getString(JDBaseManager.TABLE_RICETTA_NOME), rs.getString(JDBaseManager.TABLE_RICETTA_DESCRIZIONE), getSubProcedure(rs.getInt(JDBaseManager.TABLE_RICETTA_ID)));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectRicettaByID: " + e.getMessage());
        }
        return ricetta;
    }

    private Preparation selectPreparazioneByID(int prepID) {
        Preparation preparazione = null;
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_PREPARAZIONE
                + " WHERE " + JDBaseManager.TABLE_PREPARAZIONE_ID + " = " + prepID;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                preparazione = new Preparation(rs.getInt(JDBaseManager.TABLE_PREPARAZIONE_ID), 1, rs.getString(JDBaseManager.TABLE_PREPARAZIONE_DESCRIZIONE));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectPreparazioneByID: " + e.getMessage());
        }
        return preparazione;
    }

    public ArrayList<Preparation> getElencoPreparazioni() {
        ArrayList<Preparation> preparazioni = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_PREPARAZIONE;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                preparazioni.add(new Preparation(rs.getInt(JDBaseManager.TABLE_PREPARAZIONE_ID), -1, rs.getString(JDBaseManager.TABLE_PREPARAZIONE_DESCRIZIONE)));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in getElencoPreparazioni: " + e.getMessage());
        }
        return preparazioni;
    }

    private boolean existsFoglioRiepilogativo(int eventoId) {
        boolean result = false;
        String sql = "SELECT " + JDBaseManager.TABLE_EVENTO_FOGLIO_RIEPILOGATIVO + " FROM " + JDBaseManager.TABLE_EVENTO
                + " WHERE " + JDBaseManager.TABLE_EVENTO_ID + " = " + eventoId;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getInt(JDBaseManager.TABLE_EVENTO_FOGLIO_RIEPILOGATIVO) == 1;
            }
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore lettura valori da Database in existsFoglioRiepilogativo: " + e.getMessage());
        }
        return result;
    }

    public int insertTask(Task nc, int idEvento) {
        int id = 0;
        String sql;
        if (nc.getProcedure() instanceof Recipe) {
            sql = JDBaseManager.getInstance().sqlInsertCompitoRicetta(idEvento, nc.getProcedure().getIdProcedure(), nc.getProcedure().isRecipe() ? 1 : 0, nc.getquantity(), nc.getEstimatedTime());
        } else {
            sql = JDBaseManager.getInstance().sqlInsertCompitoPreparazione(idEvento, nc.getProcedure().getIdProcedure(), nc.getProcedure().isRecipe() ? 1 : 0, nc.getquantity(), nc.getEstimatedTime(), ((Preparation) nc.getProcedure()).getAmount());
        }
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            id = -1;
            System.out.println("Errore inserimento valori nel Database in insertTask: " + e.getMessage());
        }
        return id;
    }

    public boolean saveOrdinamentoAttualeCompiti(ArrayList<Task> compitiDaOrdinare) {
        boolean result = true;
        for (Task comp : compitiDaOrdinare) {
            String sql = "UPDATE " + JDBaseManager.TABLE_COMPITI
                    + " SET " + JDBaseManager.TABLE_COMPITI_PRIORITA + " = " + comp.getPriority()
                    + " WHERE " + JDBaseManager.TABLE_COMPITI_ID + " = " + comp.getIDTask() + ";";
            try {
                PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                result = false;
                System.out.println("Errore lettura valori da Database in saveOrdinamentoAttualeCompiti: " + e.getMessage());
            }
        }
        return result;
    }

    private boolean activateFoglioRiepilogativo(int idFoglioRiepilogativo) {
        boolean result = true;
        String sql = "UPDATE " + JDBaseManager.TABLE_EVENTO
                + " SET " + JDBaseManager.TABLE_EVENTO_FOGLIO_RIEPILOGATIVO + " = " + 1
                + " WHERE " + JDBaseManager.TABLE_EVENTO_ID + " = " + idFoglioRiepilogativo + ";";
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore lettura valori da Database in activateFoglioRiepilogativo: " + e.getMessage());
        }
        return result;

    }

    private boolean saveCompitiMenu(Menu menu, int idEvento) {
        boolean result = true;
        for (Procedure proc : menu.getComposition()) {
            String sql;
            if (proc instanceof Recipe) {
                sql = JDBaseManager.getInstance().sqlInsertCompitoRicetta(idEvento, proc.getIdProcedure(), proc.isRecipe() ? 1 : 0, 1, 1);
            } else {
                sql = JDBaseManager.getInstance().sqlInsertCompitoPreparazione(idEvento, proc.getIdProcedure(), proc.isRecipe() ? 1 : 0, 1, 1, ((Preparation) proc).getAmount());
            }
            try {
                PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                result = false;
                System.out.println("Errore lettura valori da Database in activateFoglioRiepilogativo: " + e.getMessage());
            }
        }
        return result;
    }

    public boolean eliminaTask(Task compitoSelezionato) {
        boolean result = true;
        String sql = "DELETE FROM " + JDBaseManager.TABLE_COMPITI
                + " WHERE " + JDBaseManager.TABLE_COMPITI_ID + " = " + compitoSelezionato.getIDTask();
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore aggiornamento valori in Database in aggiornaTask: " + e.getMessage());
        }
        return result;
    }

    public boolean aggiornaTask(Task compito) {
        boolean result = true;
        String sql = "UPDATE " + JDBaseManager.TABLE_COMPITI
                + " SET " + JDBaseManager.TABLE_COMPITI_N_PORZIONI + " = " + compito.getquantity()
                + " , " + JDBaseManager.TABLE_COMPITI_STIMA_TEMPO + " = " + compito.getEstimatedTime()
                + " , " + JDBaseManager.TABLE_COMPITI_PRIORITA + " = " + compito.getPriority();
        if (compito.getProcedure() instanceof Preparation) {
            sql = sql + " , " + JDBaseManager.TABLE_COMPITI_QUANTITATIVO + " = " + ((Preparation) compito.getProcedure()).getAmount();
        }
        sql = sql + " WHERE " + JDBaseManager.TABLE_COMPITI_ID + " = " + compito.getIDTask() + ";";
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore aggiornamento valori in Database in aggiornaTask: " + e.getMessage());
        }
        return result;
    }

    public ArrayList<Cook> getCuochiDisponibili(String dataInizio, String dataFine) {
        ArrayList<Cook> cuochi = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_CUOCHI
                + " WHERE " + JDBaseManager.TABLE_CUOCHI_ID_CUOCO + " NOT IN ("
                + " SELECT " + JDBaseManager.TABLE_TURNI_ID_CUOCO + " FROM " + JDBaseManager.TABLE_TURNI
                + " WHERE (" + JDBaseManager.TABLE_TURNI_DATA_INIZIO + " BETWEEN '" + dataInizio + "' AND '" + dataFine
                + "') OR (" + JDBaseManager.TABLE_TURNI_DATA_FINE + " BETWEEN '" + dataInizio + "' AND '" + dataFine
                + "'));";
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cuochi.add(new Cook(rs.getInt(JDBaseManager.TABLE_CUOCHI_ID_CUOCO), rs.getString(JDBaseManager.TABLE_CUOCHI_NOME_CUOCO)));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in getCuochiDisponibili: " + e.getMessage());
        }
        return cuochi;
    }

    /**
     * Restituisce un booleano che indica se l'inserimento del turno ha avuto
     * esito positivo o no.
     *
     * @param idTask
     * @param turno
     * @return
     */
    public boolean aggiungiTurno(int idTask, WorkShift turno) {
        boolean result = true;
        String sql = "INSERT INTO " + JDBaseManager.TABLE_TURNI + " ( " + JDBaseManager.TABLE_TURNI_ID_COMPITO + "," + JDBaseManager.TABLE_TURNI_ID_CUOCO + "," + JDBaseManager.TABLE_TURNI_DATA_INIZIO + "," + JDBaseManager.TABLE_TURNI_DATA_FINE + " ) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, idTask);
            pstmt.setInt(2, turno.getCook().getID());
            pstmt.setString(3, turno.getStartDateDB());
            pstmt.setString(4, turno.getEndDateDB());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore inserimento valori nel Database in insertTask: " + e.getMessage());
            result = false;
        }
        return result;
    }

    public ArrayList<WorkShift> selectTurniByTask(int idTask) {
        ArrayList<WorkShift> turni = new ArrayList<>();
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_TURNI
                + " WHERE " + JDBaseManager.TABLE_TURNI_ID_COMPITO + " = " + idTask;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt(JDBaseManager.TABLE_TURNI_ID_CUOCO) == 0) {
                    turni.add(new WorkShift(new Cook(0, ""), rs.getString(JDBaseManager.TABLE_TURNI_DATA_INIZIO), rs.getString(JDBaseManager.TABLE_TURNI_DATA_FINE)));
                } else {
                    turni.add(new WorkShift(selectCuocoById(rs.getInt(JDBaseManager.TABLE_TURNI_ID_CUOCO)), rs.getString(JDBaseManager.TABLE_TURNI_DATA_INIZIO), rs.getString(JDBaseManager.TABLE_TURNI_DATA_FINE)));
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectTurniByTask: " + e.getMessage());
        }
        return turni;
    }

    public Cook selectCuocoById(int idCuoco) {
        Cook cuoco = null;
        String sql = "SELECT * FROM " + JDBaseManager.TABLE_CUOCHI
                + " WHERE " + JDBaseManager.TABLE_CUOCHI_ID_CUOCO + " = " + idCuoco;
        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cuoco = new Cook(rs.getInt(JDBaseManager.TABLE_CUOCHI_ID_CUOCO), rs.getString(JDBaseManager.TABLE_CUOCHI_NOME_CUOCO));
            }
        } catch (SQLException e) {
            System.out.println("Errore lettura valori da Database in selectTurniByTask: " + e.getMessage());
        }
        return cuoco;
    }

    /**
     * Restituisce un booleano che indica se l'inserimento del turno ha avuto
     * esito positivo o no.
     *
     * @param idTask
     * @param turno
     * @return
     */
    public boolean rimuoviTurno(int idTask, WorkShift turno) {
        boolean result = true;
        String sql = "DELETE FROM " + JDBaseManager.TABLE_TURNI
                + " WHERE " + JDBaseManager.TABLE_TURNI_ID_COMPITO + " = " + idTask
                + " AND " + JDBaseManager.TABLE_TURNI_DATA_INIZIO + " = '" + turno.getStartDateDB()
                + "' AND " + JDBaseManager.TABLE_TURNI_DATA_FINE + " = '" + turno.getEndDateDB()
                + "' AND " + JDBaseManager.TABLE_TURNI_ID_CUOCO + " = " + turno.getCook().getID();
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore eliminazione turno in Database in rimuoviTurno: " + e.getMessage());
        }
        return result;
    }

    public boolean aggiornaTurno(int idTask, WorkShift oldTurno, WorkShift turno) {
        boolean result = true;
        String sql = "UPDATE " + JDBaseManager.TABLE_TURNI
                + " SET " + JDBaseManager.TABLE_TURNI_ID_CUOCO + " = " + turno.getCook().getID()
                + " , " + JDBaseManager.TABLE_TURNI_DATA_INIZIO + " = '" + turno.getStartDateDB()
                + "' , " + JDBaseManager.TABLE_TURNI_DATA_FINE + " = '" + turno.getEndDateDB()
                + "' WHERE " + JDBaseManager.TABLE_TURNI_ID_COMPITO + " = " + idTask
                + " AND " + JDBaseManager.TABLE_TURNI_DATA_INIZIO + " = '" + oldTurno.getStartDateDB()
                + "' AND " + JDBaseManager.TABLE_TURNI_DATA_FINE + " = '" + oldTurno.getEndDateDB()
                + "' AND " + JDBaseManager.TABLE_TURNI_ID_CUOCO + " = " + oldTurno.getCook().getID();
        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            result = false;
            System.out.println("Errore update valori nel Database in aggiornaTurno: " + e.getMessage());
        }
        return result;
    }
}

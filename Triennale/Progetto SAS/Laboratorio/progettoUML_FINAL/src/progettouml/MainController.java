package progettouml;

import bestutilsever.Task;
import bestutilsever.TaskCell;
import bestutilsever.Cook;
import bestutilsever.DataModel;
import bestutilsever.Preparation;
import bestutilsever.Recipe;
import bestutilsever.WorkShift;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import manager.CateringAppManager;
import manager.EventManager;

/**
 *
 * @author gianluca && christian && diego
 */
public class MainController implements Initializable {

    private DataModel model;
    private CateringAppManager cateringManager;
    private EventManager eventManager;

    //------------------------------------------|
    @FXML
    private AnchorPane paneListaEventi;

    @FXML
    private ListView lsvListaEventiElencoEventi;
    //------------------------------------|
    @FXML
    private AnchorPane paneDettagliEvento;

    @FXML
    private Label lblDettagliEventoName;
    @FXML
    private Label lblDettagliEventoData;
    @FXML
    private Label lblDettagliEventoMenuName;
    @FXML
    private Button btnDettagliEventoGestisci;
    //------------------------------------------|
    @FXML
    private AnchorPane paneGestisciEvento;

    @FXML
    private Label lblGestisciEventoNomeEvento;
    @FXML
    private Label lblGestisciEventoDataEvento;
    @FXML
    private Label lblGestisciEventoMenuEvento;
    @FXML
    private ListView lsvGestisciEventoFoglioRiepilogativo;

    //------------------------------------|
    @FXML
    private AnchorPane paneDettagliCompito;

    @FXML
    private Label lblAlert;
    @FXML
    private Label lblDettagliCompitoNomeRicetta;
    @FXML
    private Label lblDettagliCompitoDescrizioneRicetta;
    @FXML
    private TextField txtDettagliCompitoStimaTempo;
    @FXML
    private TextField txtDettagliCompitoNumeroPorzioni;
    @FXML
    private Label lblDettagliCompitoQuantita;
    @FXML
    private TextField txtDettagliCompitoQuantita;
    @FXML
    private ListView lsvDettagliCompitoElencoPreparazioni;
    @FXML
    private ComboBox<WorkShift> cmbDettagliCompitoElencoTurni;

    //------------------------------------------|
    public void goBackButton() {
        System.out.println("GO BACK BUTTON PRESSED");
        lsvGestisciEventoFoglioRiepilogativo.getSelectionModel().clearSelection();
        updateGUI(DataModel.PANE_VISUALIZZA_EVENTI);
    }

    public void modificaTurnoButton() {
        System.out.println("MODIFICA TURNO BUTTON PRESSED");
        WorkShift oldTurno = cmbDettagliCompitoElencoTurni.getSelectionModel().getSelectedItem();
        if (oldTurno == null) {
            return;
        }
        Dialog dialog = new Dialog();
        dialog.setTitle("Modifica Turno");
//dialog.setHeaderText("Look, a Custom Login Dialog");

// Set the button types.
        ButtonType modifyButton = new ButtonType("Modify", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButton, ButtonType.CANCEL);

// Create the username and password labels and fields.
        Label lblAlarmA = new Label("L'ora deve essere nel formato XY:ZW");
        lblAlarmA.setTextFill(Paint.valueOf("FF0000"));
        lblAlarmA.setVisible(false);
        Label lblAlarmB = new Label("Inserisci prima data, ora di inizio e fine turno");
        lblAlarmB.setTextFill(Paint.valueOf("FF0000"));
        lblAlarmB.setVisible(false);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker();
        TextField dataInizio = new TextField();
        TextField dataFine = new TextField();
        dataInizio.setPromptText(oldTurno.getStartDate());
        dataFine.setPromptText(oldTurno.getEndDate());
        //metto valori attuali
        datePicker.setValue(DataModel.LOCAL_DATE(oldTurno.getDate()));
        dataInizio.setText(oldTurno.getStartDate());
        dataFine.setText(oldTurno.getEndDate());
        ListView lsvDisponibilita = new ListView();
        model.resetCuochiDisponibili();
        ComboBox<Preparation> elencoPreparazioni = new ComboBox();
        Button btnCheck = new Button("Visualizza Disponibilità");

        btnCheck.setOnMouseClicked((Event event) -> {
            boolean isOk = true;
            String oraInizio = dataInizio.getText().trim();
            String oraFine = dataFine.getText().trim();
            String data = null;
            if (datePicker.getValue() != null) {
                data = datePicker.getValue().toString();
            }
            if (oraInizio == null || oraFine == null || data == null || oraInizio.isEmpty() || oraFine.isEmpty() || data.isEmpty()) {
                System.out.println("OOOOOOOOOOOOOOOOOO ehhhhhhhhhhhhh OOOOOOOOOOOOOOOOOOOOOOOOOOo GET WREKT!");
                lblAlarmB.setVisible(true);
                isOk = false;
            }
            if (!checkOra(dataInizio.getText()) || !checkOra(dataFine.getText())) {
                lblAlarmA.setVisible(true);
                isOk = false;
            }
            if (oraInizio != null && oraFine != null && oraInizio.compareTo(oraFine) >= 0) {
                isOk = false;
            }

            if (isOk) {
                model.aggiornaCuochiDisponibili(data, oraInizio, oraFine);
                lblAlarmB.setVisible(false);
                lblAlarmA.setVisible(false);
            } else {
                lblAlarmA.setVisible(!isOk);
                model.resetCuochiDisponibili();
            }

        });
        lsvDisponibilita.setItems(model.getCuochiDisponibili());
        elencoPreparazioni.setItems(cateringManager.getPreparationsList());
        elencoPreparazioni.getSelectionModel().selectFirst();
        grid.add(new Label("Turno Lavorativo:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Ora inizio(hh:mm):"), 0, 1);
        grid.add(dataInizio, 1, 1);
        grid.add(new Label("Ora fine(hh:mm):"), 0, 2);
        grid.add(dataFine, 1, 2);
        grid.add(btnCheck, 0, 3);
        grid.add(lblAlarmB, 1, 3);
        grid.add(lsvDisponibilita, 0, 4);
        grid.add(lblAlarmA, 1, 4);

        dialog.getDialogPane().setContent(grid);

        //Request focus on the datePicker
        Platform.runLater(() -> datePicker.requestFocus());

        Button btnOk = (Button) dialog.getDialogPane().lookupButton(modifyButton);
        btnOk.addEventFilter(ActionEvent.ACTION, (ActionEvent event) -> {
            boolean continua = true;
            boolean isOk = true;
            String oraInizio = dataInizio.getText().trim();
            String oraFine = dataFine.getText().trim();
            String data = null;
            if (datePicker.getValue() != null) {
                data = datePicker.getValue().toString();
            }

            if (oraInizio == null || oraFine == null || data == null || oraInizio.isEmpty() || oraFine.isEmpty() || data.isEmpty()) {
                System.out.println("OOOOOOOOOOOOOOOOOO ehhhhhhhhhhhhh OOOOOOOOOOOOOOOOOOOOOOOOOOo GET WREKT!");

                isOk = false;
            }
            if (!checkOra(dataInizio.getText()) || !checkOra(dataFine.getText())) {
                isOk = false;
            }
            //if(lsvDisponibilita.getSelectionModel().getSelectedItem() == null)
            //    isOk = false;

            if (oraInizio != null && oraFine != null && oraInizio.compareTo(oraFine) >= 0) {
                isOk = false;
            }

            if (isOk) {
                WorkShift turno;
                if (lsvDisponibilita.getSelectionModel().getSelectedItem() == null) {
                    turno = new WorkShift(new Cook(0, ""), data, oraInizio, oraFine);
                } else {
                    turno = new WorkShift(((Cook) (lsvDisponibilita.getSelectionModel().getSelectedItem())), data, oraInizio, oraFine);
                }
                eventManager.modifyWorkShift(cateringManager.getSelectedTask().getIDTask(), cmbDettagliCompitoElencoTurni.getSelectionModel().getSelectedItem(), turno);
                cmbDettagliCompitoElencoTurni.getSelectionModel().select(oldTurno);
            } else {
                lblAlarmA.setVisible(!isOk);
                model.resetCuochiDisponibili();
                event.consume();
            }
        });
        dialog.showAndWait();
    }

    public void rimuoviTurboButton() {
        System.out.println("RIMUOVI TURNO BUTTON PRESSED - TODO");
        WorkShift turno = cmbDettagliCompitoElencoTurni.getSelectionModel().getSelectedItem();
        if (turno == null) {
            return;
        }
        eventManager.removeWorkShift(cateringManager.getSelectedTask().getIDTask(), turno); //rimuove sia da cateringManager che db
        cateringManager.getSelectedTask().getWorkShifts().remove(turno);
        if (cmbDettagliCompitoElencoTurni.getSelectionModel().getSelectedItem() == null) {
            cmbDettagliCompitoElencoTurni.getSelectionModel().selectFirst();
        }
    }

    public void aggiungiTurnoButton() {
        System.out.println("AGGIUNGI TURNO BUTTON PRESSED");
        Dialog dialog = new Dialog();
        dialog.setTitle("Inserisci Turno");
//dialog.setHeaderText("Look, a Custom Login Dialog");

// Set the button types.
        ButtonType insertButton = new ButtonType("Insert", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(insertButton, ButtonType.CANCEL);

// Create the username and password labels and fields.
        Label lblAlarmA = new Label("L'ora deve essere nel formato XY:ZW");
        lblAlarmA.setTextFill(Paint.valueOf("FF0000"));
        lblAlarmA.setVisible(false);
        Label lblAlarmB = new Label("Inserisci prima data, ora di inizio e fine turno");
        lblAlarmB.setTextFill(Paint.valueOf("FF0000"));
        lblAlarmB.setVisible(false);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker();
        TextField dataInizio = new TextField();
        TextField dataFine = new TextField();
        ListView lsvDisponibilita = new ListView();
        model.resetCuochiDisponibili();
        ComboBox<Preparation> elencoPreparazioni = new ComboBox();
        Button btnCheck = new Button("Visualizza Disponibilità");

        dataInizio.setPromptText("10:30");
        dataFine.setPromptText("11:30");
        btnCheck.setOnMouseClicked((Event event) -> {
            boolean isOk = true;
            String oraInizio = dataInizio.getText().trim();
            String oraFine = dataFine.getText().trim();
            String data = null;
            if (datePicker.getValue() != null) {
                data = datePicker.getValue().toString();
            }
            if (oraInizio == null || oraFine == null || data == null || oraInizio.isEmpty() || oraFine.isEmpty() || data.isEmpty()) {
                System.out.println("OOOOOOOOOOOOOOOOOO ehhhhhhhhhhhhh OOOOOOOOOOOOOOOOOOOOOOOOOOo GET WREKT!");
                lblAlarmB.setVisible(true);
                isOk = false;
            }
            if (!checkOra(dataInizio.getText()) || !checkOra(dataFine.getText())) {
                lblAlarmA.setVisible(true);
                isOk = false;
            }
            if (oraInizio != null && oraFine != null && oraInizio.compareTo(oraFine) >= 0) {
                isOk = false;
            }

            if (isOk) {
                model.aggiornaCuochiDisponibili(data, oraInizio, oraFine);
                lblAlarmB.setVisible(false);
                lblAlarmA.setVisible(false);
            } else {
                lblAlarmA.setVisible(!isOk);
                model.resetCuochiDisponibili();
            }

        });
        lsvDisponibilita.setItems(model.getCuochiDisponibili());
        elencoPreparazioni.setItems(cateringManager.getPreparationsList());
        elencoPreparazioni.getSelectionModel().selectFirst();
        grid.add(new Label("Turno Lavorativo:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Ora inizio(hh:mm):"), 0, 1);
        grid.add(dataInizio, 1, 1);
        grid.add(new Label("Ora fine(hh:mm):"), 0, 2);
        grid.add(dataFine, 1, 2);
        grid.add(btnCheck, 0, 3);
        grid.add(lblAlarmB, 1, 3);
        grid.add(lsvDisponibilita, 0, 4);
        grid.add(lblAlarmA, 1, 4);

        dialog.getDialogPane().setContent(grid);

        //Request focus on the datePicker
        Platform.runLater(() -> datePicker.requestFocus());

        Button btnOk = (Button) dialog.getDialogPane().lookupButton(insertButton);
        btnOk.addEventFilter(ActionEvent.ACTION, (ActionEvent event) -> {
            boolean continua = true;
            boolean isOk = true;
            String oraInizio = dataInizio.getText().trim();
            String oraFine = dataFine.getText().trim();
            String data = null;
            if (datePicker.getValue() != null) {
                data = datePicker.getValue().toString();
            }

            if (oraInizio == null || oraFine == null || data == null || oraInizio.isEmpty() || oraFine.isEmpty() || data.isEmpty()) {
                System.out.println("OOOOOOOOOOOOOOOOOO ehhhhhhhhhhhhh OOOOOOOOOOOOOOOOOOOOOOOOOOo GET WREKT!");

                isOk = false;
            }
            if (!checkOra(dataInizio.getText()) || !checkOra(dataFine.getText())) {
                isOk = false;
            }
            //if(lsvDisponibilita.getSelectionModel().getSelectedItem() == null)
            //    isOk = false;

            if (oraInizio != null && oraFine != null && oraInizio.compareTo(oraFine) >= 0) {
                isOk = false;
            }

            if (isOk) {
                WorkShift turno;
                if (lsvDisponibilita.getSelectionModel().getSelectedItem() == null) {
                    turno = new WorkShift(new Cook(0, ""), data, oraInizio, oraFine);
                } else {
                    turno = new WorkShift(((Cook) (lsvDisponibilita.getSelectionModel().getSelectedItem())), data, oraInizio, oraFine);
                }
                if (eventManager.addWorkShift(cateringManager.getSelectedTask().getIDTask(), turno)) {
                    cmbDettagliCompitoElencoTurni.getSelectionModel().selectLast();
                    cateringManager.getSelectedTask().getWorkShifts().add(turno);
                } else {//se entra qui l'utente ha modificato l'ora dopo aver cercato i turni disponibili
                    //e ha rotto un vincolo sugli orari.
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("ATTENZIONE!");
                    alert.setHeaderText(null);
                    alert.setContentText("Il cuoco selezionato non è disponibile in questo orario.");

                    alert.showAndWait();
                    event.consume();
                }
            } else {
                lblAlarmA.setVisible(!isOk);
                model.resetCuochiDisponibili();
                event.consume();
            }
        });
        dialog.showAndWait();
    }

    public void salvaChiudiCompitoButton() {
        System.out.println("SALVA CHIUDI COMPITO BUTTON PRESSED");
        Task compito = cateringManager.getSelectedTask(); //metti alert se lista vuota
        try {
            compito.setEstimatedTime(Integer.valueOf(txtDettagliCompitoStimaTempo.getText()));
            compito.setQuantity(Integer.valueOf(txtDettagliCompitoNumeroPorzioni.getText()));
            if (compito.getProcedure() instanceof Preparation) {
                //devi rileggere i valori base!
                ((Preparation) compito.getProcedure()).setAmount(Integer.valueOf(txtDettagliCompitoQuantita.getText()));

                //compito.setStimaTempo(compito.getStimaTempo()*((Preparazione) compito.getProcedura()).getQuantitativo());
                //compito.setnPorzioni(compito.getnPorzioni()*((Preparazione) compito.getProcedura()).getQuantitativo());
            }
            eventManager.updateTask(compito);
            cateringManager.getDriver().aggiornaTask(compito);
            if (lblAlert.isVisible())//se c'è la label di errore visibile la rendo invisibile siccome è tutto ok
            {
                lblAlert.setVisible(false);
            }
        } catch (NumberFormatException ex) {
            if (!lblAlert.isVisible()) {
                lblAlert.setVisible(true);
            }

        }
    }

    public void eliminaCompitoButton() {
        System.out.println("ELIMINA COMPITO BUTTON PRESSED");
        //salvo le cose da eliminare in un array perchè non posso eliminare i contenuti che ciclo in un foreach
        ArrayList<Task> toDel = new ArrayList<>();
        //prima elimino le sub procedure
        if (cateringManager.getSelectedTask().getFatherTask() == null) { //se è il compito padre procedo con la ricerca
            eventManager.getTasks().stream().filter((comp) -> (comp.getFatherTask() != null)).filter((comp) -> (cateringManager.getSelectedTask().getIDTask() == comp.getFatherTask().getIDTask())).forEachOrdered((comp) -> {
                toDel.add(comp);
            });
        }
        toDel.stream().map((comp) -> {
            cateringManager.getDriver().eliminaTask(comp);
            return comp;
        }).forEachOrdered((comp) -> {
            eventManager.deleteTask(comp);
        });
        cateringManager.getDriver().eliminaTask(cateringManager.getSelectedTask());
        eventManager.deleteTask(cateringManager.getSelectedTask());
    }

    public void aggiungiProceduraButton() {
        System.out.println("AGGIUNGI PROCEDURA BUTTON PRESSED");
        // Create the custom dialog.
        Dialog dialog = new Dialog();
        dialog.setTitle("Nuovo Compito");

// Set the button types.
        ButtonType insertButton = new ButtonType("Insert", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(insertButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField stimaTempo = new TextField();
        stimaTempo.setPromptText("Stima Tempo:");
        stimaTempo.setText("1");
        TextField nPorzioni = new TextField();
        nPorzioni.setPromptText("N.Porzioni:");
        nPorzioni.setText("1");
        TextField quantitativo = new TextField();
        quantitativo.setPromptText("Quantitativo(g):");
        quantitativo.setText("1");
        ComboBox<Preparation> elencoPreparazioni = new ComboBox();
        elencoPreparazioni.setItems(cateringManager.getPreparationsList());
        elencoPreparazioni.getSelectionModel().selectFirst();
        grid.add(new Label("Preparazione:"), 0, 0);
        grid.add(elencoPreparazioni, 1, 0);
        grid.add(new Label("Stima Tempo:"), 0, 1);
        grid.add(stimaTempo, 1, 1);
        grid.add(new Label("N.Porzioni:"), 0, 2);
        grid.add(nPorzioni, 1, 2);
        grid.add(new Label("Quantitativo(g):"), 0, 3);
        grid.add(quantitativo, 1, 3);

        //Label vuoto = new Label("");
        Label alarm = new Label("Inserire solo numeri interi!");
        alarm.setTextFill(Paint.valueOf("FF0000"));
        alarm.setVisible(false);
        //grid.add(vuoto,0,4);
        grid.add(alarm, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> nPorzioni.requestFocus());

        Button btnOk = (Button) dialog.getDialogPane().lookupButton(insertButton);
        btnOk.addEventFilter(ActionEvent.ACTION, event -> {
            boolean continua = true;
            Task nc = null;
            Preparation prep;

            try {
                prep = elencoPreparazioni.getSelectionModel().getSelectedItem();
                prep.setAmount(Integer.valueOf(quantitativo.getText()));
                if (eventManager.getTasks().isEmpty()) {
                    nc = new Task(-1, elencoPreparazioni.getSelectionModel().getSelectedItem(), Integer.valueOf(nPorzioni.getText()), Integer.valueOf(stimaTempo.getText()), null, 0);
                } else {
                    nc = new Task(-1, elencoPreparazioni.getSelectionModel().getSelectedItem(), Integer.valueOf(nPorzioni.getText()), Integer.valueOf(stimaTempo.getText()), null, eventManager.getTasks().get(eventManager.getTasks().size() - 1).getPriority() + 1);
                }

            } catch (NumberFormatException ex) {
                alarm.setVisible(true);
                continua = false;
            }
            if (nc != null) { //se è null non provo nemmeno ad aggiungere
                nc.setIDTask(cateringManager.getDriver().insertTask(nc, eventManager.getSelectedEvent().getIdEvento()));
                eventManager.addTask(nc);
            } else {
                continua = false;
            }

            if (!continua) {
                // The conditions are not fulfilled so we consume the event
                // to prevent the dialog to close
                event.consume();
            }
        });

        dialog.showAndWait();

    }

    private void gestisciEvento(bestutilsever.Event evento) {
        System.out.println("Button premuto da evento: " + evento.getName());
        updateGUI(DataModel.PANE_GESTISCI_EVENTO);
        lblGestisciEventoNomeEvento.setText(evento.getName());
        lblGestisciEventoDataEvento.setText("Data: " + evento.getData());
        lblGestisciEventoMenuEvento.setText("Menu: " + evento.getMenu().getTitle());
    }

    private void updateGUI(int newValue) {
        switch (model.getPanelIndex()) {
            case DataModel.PANE_GESTISCI_EVENTO:
                paneGestisciEvento.setVisible(false);
                paneGestisciEvento.setDisable(true);
                paneDettagliCompito.setVisible(false);
                paneDettagliCompito.setDisable(true);
                break;
            case DataModel.PANE_VISUALIZZA_EVENTI:
                paneListaEventi.setVisible(false);
                paneListaEventi.setDisable(true);
                break;
        }
        model.setPanelIndex(newValue);
        switch (newValue) {
            case DataModel.PANE_GESTISCI_EVENTO:
                paneGestisciEvento.setVisible(true);
                paneGestisciEvento.setDisable(false);
                break;
            case DataModel.PANE_VISUALIZZA_EVENTI:
                paneListaEventi.setVisible(true);
                paneListaEventi.setDisable(false);
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = DataModel.getInstance();
        cateringManager = CateringAppManager.getInstance();
        eventManager = cateringManager.getEventManager();
        eventManager.loadEvents(cateringManager.getDriver().selectEventi());
        lsvListaEventiElencoEventi.setItems(eventManager.getEvents());
        lsvListaEventiElencoEventi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePaneDettagliEvento((bestutilsever.Event) newValue);
        });
        lsvGestisciEventoFoglioRiepilogativo.setItems(eventManager.getTasks());
        lsvGestisciEventoFoglioRiepilogativo.setCellFactory(param -> new TaskCell());
        lsvGestisciEventoFoglioRiepilogativo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updatePaneDettagliCompito((Task) newValue);
        });
        lsvDettagliCompitoElencoPreparazioni.setItems(eventManager.getSubPreparations());
        cmbDettagliCompitoElencoTurni.setItems(eventManager.getWorkShifts());

    }

    private void updatePaneDettagliEvento(bestutilsever.Event evento) {
        if (evento == null) {
            paneDettagliEvento.setVisible(false);
            paneDettagliEvento.setDisable(true);
        } else {
            paneDettagliEvento.setVisible(true);
            paneDettagliEvento.setDisable(false);
            eventManager.setSelectedEvent(evento);
            eventManager.loadTasks(evento.getReviewPaper().getTasks());
            lblDettagliEventoName.setText(evento.getName());
            lblDettagliEventoData.setText("Data: " + evento.getData().toString());
            lblDettagliEventoMenuName.setText(evento.getMenu().getTitle());
            btnDettagliEventoGestisci.setOnMouseClicked((not_used) -> gestisciEvento(evento));
        }
    }

    private void updatePaneDettagliCompito(Task compito) {
        if (compito == null) {
            paneDettagliCompito.setVisible(false);
            paneDettagliCompito.setDisable(true);
        } else {
            paneDettagliCompito.setVisible(true);
            paneDettagliCompito.setDisable(false);
            cateringManager.setSelectedTask(compito);
            eventManager.setSubPreparations(compito);
            eventManager.setWorkShifts(compito);
            if (!eventManager.getWorkShifts().isEmpty()) {
                cmbDettagliCompitoElencoTurni.getSelectionModel().selectFirst();
            }
            if (compito.getProcedure() instanceof Recipe) {
                lblDettagliCompitoNomeRicetta.setText(((Recipe) compito.getProcedure()).getName());
                lblDettagliCompitoDescrizioneRicetta.setText(((Recipe) compito.getProcedure()).getDescription());
                lblDettagliCompitoQuantita.setDisable(true);
                lblDettagliCompitoQuantita.setVisible(false);
                txtDettagliCompitoQuantita.setDisable(true);
                txtDettagliCompitoQuantita.setVisible(false);
            } else {
                lblDettagliCompitoNomeRicetta.setText(((Preparation) compito.getProcedure()).getDescription());
                lblDettagliCompitoDescrizioneRicetta.setText("");
                lblDettagliCompitoQuantita.setDisable(false);
                lblDettagliCompitoQuantita.setVisible(true);
                txtDettagliCompitoQuantita.setDisable(false);
                txtDettagliCompitoQuantita.setVisible(true);
                txtDettagliCompitoQuantita.setText(String.valueOf(((Preparation) compito.getProcedure()).getAmount()));
            }
            txtDettagliCompitoNumeroPorzioni.setText(String.valueOf(compito.getquantity()));
            txtDettagliCompitoStimaTempo.setText(String.valueOf(compito.getEstimatedTime()));
        }
    }

    public void shutDown() {
        cateringManager.getDriver().closeConnection();
    }

    private boolean checkOra(String ora) {
        boolean result = false;
        String s = ora;
        char[] c = new char[5];
        int support = 0;
        try {
            for (int i = 0; i < s.length(); i++) {
                c[i] = s.charAt(i);
            }
            if (Character.isDigit(c[0]) && Character.isDigit(c[1]) && c[2] == ':' && Character.isDigit(c[3]) && Character.isDigit(c[3])) {
                support = Integer.parseInt(String.valueOf(c, 0, 2));
                //controllo ora
                if (support >= 0 && support <= 24) {
                    support = Integer.parseInt(String.valueOf(c, 3, 2));
                    if (support >= 0 && support <= 59) {
                        result = true;
                        System.out.println("DEBUG: Ora [" + ora + "] è OK");
                    }
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            result = false;
        }

        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbase.JDBaseDriver;

/**
 *
 * @author gianluca && christian && diego
 */
public class DataModel {

    private static DataModel dataModel;
    private final JDBaseDriver dbDriver;
    public static final int PANE_VISUALIZZA_EVENTI = 0;
    public static final int PANE_GESTISCI_EVENTO = 1;
    public static final ObservableList<String> cmbOrdinamentoValori = FXCollections.observableArrayList("Tempo Stimato", "N.Porzioni", "Priorità");

    private Task dragCompito;
    private final ObservableList<Cook> cuochiDisponibili;

    private int panelIndex;

    public static final LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    private DataModel() {
        panelIndex = PANE_VISUALIZZA_EVENTI;
        cuochiDisponibili = FXCollections.observableArrayList();
        dbDriver = JDBaseDriver.getInstance();
    }

    public static DataModel getInstance() {
        if (dataModel == null) {
            dataModel = new DataModel();
        }
        return dataModel;
    }

    public void setDragCompito(Task compito) {
        dragCompito = compito;
    }

    public Task getDragCompito() {
        return dragCompito;
    }

    public int getPanelIndex() {
        return panelIndex;
    }

    public void setPanelIndex(int value) {
        panelIndex = value;
    }

    public void aggiornaCuochiDisponibili(String data, String oraInizio, String oraFine) {
        String dataInizio = data + " " + oraInizio;
        String dataFine = data + " " + oraFine;
        synchronized (cuochiDisponibili) {
            cuochiDisponibili.clear();
            cuochiDisponibili.addAll(dbDriver.getCuochiDisponibili(dataInizio, dataFine));
        }
    }

    public ObservableList getCuochiDisponibili() {
        return cuochiDisponibili;
    }

    public void resetCuochiDisponibili() {
        synchronized (cuochiDisponibili) {
            cuochiDisponibili.clear();
        }
    }
}

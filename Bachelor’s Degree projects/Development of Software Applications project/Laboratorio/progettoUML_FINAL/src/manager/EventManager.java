/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import bestutilsever.Event;
import bestutilsever.Menu;
import bestutilsever.Procedure;
import bestutilsever.ReviewPaper;
import bestutilsever.Staff;
import bestutilsever.Task;
import bestutilsever.WorkShift;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbase.JDBaseDriver;

/**
 *
 * @author Christian && gianluca && diego
 */
public class EventManager {
    private static EventManager eventManager;
    
    private ReviewPaper reviewPaper;
    private final ObservableList<Event> events;
    private final ObservableList<Task> tasks;
    private final ObservableList<Procedure> subPreparations;
    private final ObservableList<WorkShift> workShifts;
    private Event selectedEvent;
    private int order;
    private final JDBaseDriver dbDriver;
    
    
    private EventManager(){
        events = FXCollections.observableArrayList();
        tasks = FXCollections.observableArrayList();
        subPreparations = FXCollections.observableArrayList();
        workShifts = FXCollections.observableArrayList();
        order = 2;
        dbDriver = JDBaseDriver.getInstance();
    }
    
    public static EventManager getInstance(){
        if(eventManager == null)
            eventManager = new EventManager();
        return eventManager;
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }
    public ObservableList<WorkShift> getWorkShifts(){
        return workShifts;
    }
    public ObservableList getSubPreparations() {
        return subPreparations;
    }
    public void loadEvents(ArrayList<Event> newEvents) {
        synchronized (events) {
            events.clear();
            events.addAll(newEvents);
            sortTasks();
            events.notifyAll();
        }
    }

    public void loadTasks(ArrayList<Task> newTasks) {
        synchronized (tasks) {
            tasks.clear();
            tasks.addAll(newTasks);
            sortTasks();
            tasks.notifyAll();
        }
    }

    public void updateTask(Task task) {
        synchronized (tasks) {
            tasks.get(tasks.indexOf(task)).updateData(task);
            synchronized (events) {
                events.get(events.indexOf(selectedEvent)).getReviewPaper().getTasks().get(events.get(events.indexOf(selectedEvent)).getReviewPaper().getTasks().indexOf(task)).updateData(task);
            }
            sortTasks();
            saveOrder();
        }
    }

    public void deleteTask(Task task) {
        synchronized (tasks) {
            tasks.remove(task);
            synchronized (events) {
                events.get(events.indexOf(selectedEvent)).getReviewPaper().getTasks().remove(task);
            }
        }
    }

    public void addTask(Task task) {
        synchronized (tasks) {
            tasks.add(task);
            synchronized (events) {
                events.get(events.indexOf(selectedEvent)).getReviewPaper().getTasks().add(task);
            }
            sortTasks();
            saveOrder();
        }
    }

    public void setSubPreparations(Task task) {
        synchronized(subPreparations){
            subPreparations.clear();
            subPreparations.addAll(task.getProcedure().getSubProcedure());
        }
    }
    public void setWorkShifts(Task task){
        synchronized(workShifts){
            workShifts.clear();
            workShifts.addAll(task.getWorkShifts());
        }
    }
    /**
     * Metodo che ordina l'ArrayList dove sono memorizzati i vari Compiti
     */
    public void sortTasks() {
        //[0] -> tempo stimato
        //[1] -> n.porzioni
        //[2] -> priorità: + è bassa e più è importante
        synchronized(tasks){
            switch(order){
                case 0:
                    tasks.sort((Task o1, Task o2) -> o1.getEstimatedTime() > o2.getEstimatedTime() ? 1 : -1);
                    break;
                case 1:
                    tasks.sort((Task o1, Task o2) -> o1.getquantity()> o2.getquantity()? 1 : -1);
                    break;
                case 2:
                    tasks.sort((Task o1, Task o2) -> o1.getPriority() > o2.getPriority()? 1 : -1);
                    break;
            }
        }
    }
    public void updateOrder(int newValue){
        order = newValue;
        //saveOrder();
        sortTasks();
    }
    /**
     * Imposta le priorità dei tasks nell'ordine in cui sono attualmente.
     * Non devo rieffettuare la sort perchè nell'ObservableList sono già aggiornate,
     * devo solo modificare il valore di priorità.
     */
    public void saveOrder() {
        for(int i = 0; i < this.tasks.size(); i++){
            this.tasks.get(i).setPriority(i);
            //System.out.println("saveOrder: Compito: " + this.tasks.get(i).toString() + "    priorità: " + this.tasks.get(i).getPriorita());
            dbDriver.aggiornaTask(this.tasks.get(i));
        }
    }
    
    public void createReviewPaper(Menu menu){
        reviewPaper = new ReviewPaper();
        //fare ciclo for e aggiungere tutti gli elementi del menu nel reviewpaper
    }
    
    //public Event createEventSheet(String title, Date date, String place, int n_part, String service_type, String note){
       // Event event;
      //  return event;
    //}
    
    public void modifyData(String title, Date date, String place, int n_part, String service_type, String note){
    
    }
    
    public void addStaff(Staff person, String role){
    
    }
    
    public void removeStaff(Staff person){
    
    }
    
    //public Event modifyEvent(Event event){
    
    //}
    
    public void saveTask(Task task){
    
    }
    
    public void removeTask(Task task){
    
    }
    
    public void deleteEvent(Event event){
    
    }
    
    //public Event showEvent(Event event){
    
    //}
    
    //public Task showTask(Task task){
        
    //}
    
    public void modifyRole(Staff person, String role){
    
    }
    
    /**
     * Restituisce un booleano che indica se l'aggiunta del turno ha avuto
     * esito positivo o no.
     * @param idTask
     * @param workShift
     * @return 
     */
    public boolean addWorkShift(int idTask, WorkShift workShift) {
        boolean result;
        synchronized(workShifts){
            result = dbDriver.aggiungiTurno(idTask, workShift);
            if(result)
                workShifts.add(workShift);
        }
        return result;
    }
    
    public boolean removeWorkShift(int idTask, WorkShift workShift) {
        boolean result;
        synchronized(workShifts){
            result = dbDriver.rimuoviTurno(idTask, workShift);
            if(result)
                workShifts.remove(workShift);
        }
        return result;
    }
    
    public void setCurrentTask(Task task){
    
    }
    
    //public Task getCurrentTask(){
        
    //}
    
    public void setAmount(int nAmount){
        
    }
    
    public void setSelectedEvent(Event event) {
        selectedEvent = event;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }
    
    //public void setEstimatedTime(Time time){
      
    //}
    
    //public void assignChef(Chef chef){
    
    //}
    
    //public void assignWorkShiftToTask(Date startDate, Date endDate, Cook cook, Task task){
    
    //}
    
    //public void deleteWorkShift(WorkShift workShift){
    
    //}
    
    public void sortList(int oldPosition, int newPosition){
    
    }

    public void modifyWorkShift(int idTask, WorkShift oldTurno, WorkShift turno) {
        synchronized(workShifts){
            dbDriver.aggiornaTurno(idTask, oldTurno, turno);
            int index = workShifts.indexOf(oldTurno);
            workShifts.remove(oldTurno);
            oldTurno.updateWorkShift(turno);
            workShifts.add(index, oldTurno);
        }
    }
    
}

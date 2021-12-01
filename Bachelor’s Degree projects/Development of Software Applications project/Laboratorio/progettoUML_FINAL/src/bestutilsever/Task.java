/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author gianluca && christian && diego
 */
public class Task extends Observable implements Observer{

    private int idTask;
    private int estimatedTime;
    private int quantity;
    private ArrayList<WorkShift> workShifts;
    private Procedure procedure;
    private Task taskFather; //se è null sei padre
    private int priority;
    
    
    public Task(int idTask, Procedure procedure, int quantity, int estimatedTime, Task padre, int priority) {
        this.idTask = idTask;
        this.procedure = procedure;
        this.quantity = quantity;
        this.estimatedTime = estimatedTime;
        this.taskFather = padre;
        this.priority = priority;
        workShifts = new ArrayList<>();
    } 
    
    public int getIDTask(){
        return idTask;
    }

    @Override
    public String toString() {
        return procedure.toString();
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public synchronized void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
        if(taskFather == null){
            setChanged();
            notifyObservers();
        }    
    }

    public int getquantity() {
        return quantity;
    }

    public synchronized void setQuantity(int quantity) {
        this.quantity = quantity;
        if(taskFather == null){
            setChanged();
            notifyObservers();
        }    
    }

    public ArrayList<WorkShift> getWorkShifts() {
        return workShifts;
    }

    public void setWorkShifts(ArrayList<WorkShift> workShifts) {
        this.workShifts = workShifts;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public synchronized void updateData(Task task) {
        estimatedTime = task.getEstimatedTime();
        quantity = task.getquantity();
        workShifts = task.getWorkShifts();
        procedure = task.getProcedure();
        if(taskFather == null){
            setChanged();
            notifyObservers();
        }    
    }

    public void setIDTask(int idTask) {
        this.idTask = idTask;
    }

    public Task getFatherTask() {
        return taskFather;
    }

    public void setFatherTask(Task taskFather) {
        this.taskFather = taskFather;
    }
    public void setPriority(int priority){
        this.priority = priority;
    }
    public int getPriority(){
        return priority;
    }
    

    @Override
    public void update(Observable o, Object arg) { 
        if(taskFather != null){//aggiorni solo se sei una sub procedura
            this.setEstimatedTime(((Task)o).getEstimatedTime());
            this.setQuantity(((Task)o).getquantity());
            System.out.println(((Task)o).getProcedure().getDescription() +": Dovrei modificarmi in teoria :/");
        }
    }

}
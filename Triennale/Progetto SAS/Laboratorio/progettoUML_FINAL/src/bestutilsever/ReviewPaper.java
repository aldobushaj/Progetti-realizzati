/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;

/**
 *
 * @author gianluca && christian && diego
 */
public class ReviewPaper {

    private final ArrayList<Task> tasks;
    private Task currentTask;

    public ReviewPaper() {
        tasks = new ArrayList<>();
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    
    public void setCurrentTask(Task task){
        this.currentTask = task;
    }
    
    public Task getCurrentTask(){
        return currentTask;
    }
    public void setAmount(int amount){
        currentTask.setQuantity(amount);
    }
    public void setEstimatedTime(int time){
        currentTask.setEstimatedTime(time);
    }
    public void deleteWorkShift(WorkShift ws){
        currentTask.getWorkShifts().remove(ws);
    }
}
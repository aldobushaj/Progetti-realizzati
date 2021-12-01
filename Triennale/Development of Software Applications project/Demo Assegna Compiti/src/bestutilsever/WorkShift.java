/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;


public class WorkShift {

    private String data;
    private String start;
    private String end;    
    private Cook cook;
    
    public WorkShift(Cook cook, String data, String startDate, String endDate) {
        this.cook = cook;
        this.data = data;
        this.start = startDate;
        this.end = endDate;
    }
    public WorkShift(Cook cook, String startDate, String endDate) {
        this.cook = cook;
        this.data = startDate.split(" ")[0];
        this.start = startDate.split(" ")[1];
        this.end = endDate.split(" ")[1];
    }

    public Cook getCook() {
        return cook;
    }

    public String getDate(){
        return data;
    }
    
    public String getStartDate() {
        return start;
    }

    public String getEndDate() {
        return end;
    }
    
    public String getStartDateDB(){
        return data + " " + start;
    }
    
    public String getEndDateDB(){
        return data + " " + end;
    }

    @Override
    public String toString() {
        return "Cuoco: " + cook + "\nInizio: " + data + " " + start + "\nFine: " + data + " " + end;
        //return "TODO";
    }

    public void updateWorkShift(WorkShift turno) {
        this.cook = turno.getCook();
        this.data = turno.getDate();
        this.start = turno.getStartDate();
        this.end = turno.getEndDate();
    }
    
}
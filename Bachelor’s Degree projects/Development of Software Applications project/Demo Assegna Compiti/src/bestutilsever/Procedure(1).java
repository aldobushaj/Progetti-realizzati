/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;


public abstract class Procedure {

    private int idProcedure;
    protected String description;
    private boolean isRecipe = false;
    private ArrayList<Procedure> subProcedures;
    
    public Procedure(boolean isRecipe, int idProcedure){
        init(isRecipe, idProcedure);
    }
    
    public Procedure(boolean isRecipe, int idProcedure, ArrayList<Procedure> subProcedures){
        init(isRecipe, idProcedure);
        this.subProcedures.addAll(subProcedures);
    }
    
    private void init(boolean isRecipe, int idProcedure){
        this.isRecipe = isRecipe;
        this.idProcedure = idProcedure;
        this.subProcedures = new ArrayList<>();
    }
    
    public int getIdProcedure(){
        return idProcedure;
    }
    
    public ArrayList<Procedure> getSubProcedure(){
        return subProcedures;
    }

    public boolean isRecipe() {
        return isRecipe;
    }

    public String getDescription() {
        return description;
    }
}
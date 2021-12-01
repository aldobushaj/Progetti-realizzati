/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import bestutilsever.Recipe;
import java.util.ArrayList;

/**
 *
 * @author Christian && Gianluca && Diego
 */
public class RecipeManager {
    private static RecipeManager recipeManager;
    private ArrayList<Recipe> recipes;
    private Recipe currentRecipe;
            
    private RecipeManager(){}
    
    public static RecipeManager getInstance(){
        if(recipeManager == null)
            recipeManager = new RecipeManager();
        return recipeManager;
    }
    
    //public void addInstruction(Instruction instruction){
    
    //}
    
    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    
    //public Recipe modifyRecipe(String notes, String classification, int amount,
            //int TT, int TACc, int TACv, int TUc, int TUv){
    
    //}
    
    //public void modifyNotes(Instruction instruction, String notes){
    
    //}
    
    
    
}

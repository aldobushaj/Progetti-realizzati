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
public class Recipe extends Procedure {
    private final String name;
    private final User owner;
    private String classification;
    private String note;
    private int people;
    private boolean in_use;
    private int TT;  //Time total
    private int TUc; //Time boh1 constant
    private int TUv; //Time boh2 variable
    private int TAc; //Time boh3 constant
    private int TAv; //Time boh4 variable

    public Recipe(int idRecipe, String name, String description, ArrayList<Procedure> subProcedures, User owner) {
        super(true, idRecipe, subProcedures);
        this.name = name;
        this.description = description;
        this.in_use = false;
        this.TT = 0; 
        this.TUc = 0;
        this.TUv = 0;
        this.TAc = 0;
        this.TAv = 0;
        this.owner = owner;
    }
    public Recipe(int idRecipe, String name, String description, ArrayList<Procedure> subProcedures) {
        super(true, idRecipe, subProcedures);
        this.name = name;
        this.description = description;
        this.in_use = false;
        this.TT = 0; 
        this.TUc = 0;
        this.TUv = 0;
        this.TAc = 0;
        this.TAv = 0;
        this.owner = null;
    }

    public String getName() {
        return name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public boolean isIn_use() {
        return in_use;
    }

    public void setIn_use(boolean in_use) {
        this.in_use = in_use;
    }

    public int getTT() {
        return TT;
    }

    public void setTT(int TT) {
        this.TT = TT;
    }

    public int getTUc() {
        return TUc;
    }

    public void setTUc(int TUc) {
        this.TUc = TUc;
    }

    public int getTUv() {
        return TUv;
    }

    public void setTUv(int TUv) {
        this.TUv = TUv;
    }

    public int getTAc() {
        return TAc;
    }

    public void setTAc(int TAc) {
        this.TAc = TAc;
    }

    public int getTAv() {
        return TAv;
    }

    public void setTAv(int TAv) {
        this.TAv = TAv;
    }

    
    
    @Override
    public String toString() {
        return name;
    }

    
}
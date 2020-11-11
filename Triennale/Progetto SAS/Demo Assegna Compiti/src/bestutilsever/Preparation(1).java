/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;


public class Preparation extends Procedure {

    private int amount;

    public Preparation(int idPreparation, int amount, String description) {
        super(false, idPreparation);
        this.amount = amount;
        this.description = description;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public int getAmount(){
        return amount;
    }

    @Override
    public String toString() {
        if(amount > 0)
            return description + ": " + amount + "g";
        else
            return description;
    }
    
    
}
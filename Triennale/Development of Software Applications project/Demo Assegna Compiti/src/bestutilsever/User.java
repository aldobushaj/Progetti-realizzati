/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;


public class User {
    private final int id;
    private final String nome;
    
    public User(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    public int getID(){
        return id;
    }
    public String getNome(){
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;


/**
 *
 * @author Christian && gianluca && diego
 */
public class Staff extends User{
    private String role;
    
    public Staff(int id, String name) {
        super(id, name);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;

/**
 *
 * @author gianluca
 */
public class Menu {

    private final int idMenu;
    private final String title;
    private final boolean published;
    private final boolean cookRequested;
    private final boolean hotDishes;
    private final boolean kitchenRequested;
    private final boolean buffet;
    private final boolean fingerFood;
    private final ArrayList<Procedure> composition;

    public Menu(int idMenu, String title, boolean published, boolean cookRequested, boolean hotDishes, boolean kitchenRequested, boolean buffet, boolean fingerFood, ArrayList<Procedure> composition) {
        this.idMenu = idMenu;
        this.title = title;
        this.published = published;
        this.cookRequested = cookRequested;
        this.hotDishes = hotDishes;
        this.kitchenRequested = kitchenRequested;
        this.buffet = buffet;
        this.fingerFood = fingerFood;
        this.composition = composition;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublished() {
        return published;
    }

    public boolean isCookRequested() {
        return cookRequested;
    }

    public boolean isHotDishes() {
        return hotDishes;
    }

    public boolean isKitchenRequested() {
        return kitchenRequested;
    }

    public boolean isBuffet() {
        return buffet;
    }

    public boolean isFingerfood() {
        return fingerFood;
    }

    public ArrayList<Procedure> getComposition() {
        return composition;
    }
}
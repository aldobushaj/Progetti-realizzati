/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.sql.Date;

/**
 *
 * @author gianluca && christian && diego
 */
public class Event {

    private final int idEvent;
    private final String name;
    private final Date data;
    private final Menu menu;
    private ReviewPaper reviewPaper;

    public Event(int idEvent, Menu menu, ReviewPaper reviewPaper, String name, String data) {
        this.idEvent = idEvent;
        this.menu = menu;
        this.name = name;
        this.data = Date.valueOf(data);
        this.reviewPaper = reviewPaper;
    }
    public void setFoglioRiepilogativo(ReviewPaper reviewPaper){
        this.reviewPaper = reviewPaper;
    }

    public int getIdEvento() {
        return idEvent;
    }

    public Date getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Menu getMenu() {
        return menu;
    }

    public ReviewPaper getReviewPaper() {
        return reviewPaper;
    }

    @Override
    public String toString() {
        if (name != null) {
            return "Evento -> " + name;
        } else {
            return "Evento -> NO-TITLE-YET";
        }
    }

}
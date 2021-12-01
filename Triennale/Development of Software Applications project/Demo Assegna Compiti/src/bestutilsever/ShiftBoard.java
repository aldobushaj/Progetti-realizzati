/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;


public class ShiftBoard {
    private static ShiftBoard shiftBoard;
    private ArrayList<WorkShift> workShiftList;
  
    private ShiftBoard(){
        workShiftList = new ArrayList<>();
    }
    
    public static ShiftBoard getInstance(){
        if(shiftBoard == null)
            shiftBoard = new ShiftBoard();
        return shiftBoard;
    }
    
}

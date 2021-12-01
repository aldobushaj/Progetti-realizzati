/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bestutilsever;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import manager.CateringAppManager;


public class TaskCell extends ListCell<Task> {

    private final Label text = new Label();

    public TaskCell() {

        ListCell thisCell = this;

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        setAlignment(Pos.CENTER);

        setOnDragDetected(event -> {

            if (getItem() == null) {

                return;

            }

            ObservableList<Task> items = getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            DataModel.getInstance().setDragCompito(getItem());
            content.putString(getItem().toString());

            dragboard.setContent(content);

            event.consume();

        });

        setOnDragOver(event -> {

            if (event.getGestureSource() != thisCell
                    && event.getDragboard().hasString()) {

                event.acceptTransferModes(TransferMode.MOVE);

            }

            event.consume();

        });

        setOnDragEntered(event -> {

            if (event.getGestureSource() != thisCell
                    && event.getDragboard().hasString()) {

                setOpacity(0.3);

            }

        });

        setOnDragExited(event -> {

            if (event.getGestureSource() != thisCell
                    && event.getDragboard().hasString()) {

                setOpacity(1);

            }

        });

        setOnDragDropped(event -> {

            if (getItem() == null) {

                return;

            }

            Dragboard db = event.getDragboard();

            boolean success = false;

            if (db.hasString()) {

                ObservableList<Task> items = getListView().getItems();
                Task draggedCompito = DataModel.getInstance().getDragCompito();
                int draggedIdx = items.indexOf(draggedCompito);

                int thisIdx = items.indexOf(getItem());

                /*Image temp = birdImages.get(draggedIdx);

                    birdImages.set(draggedIdx, birdImages.get(thisIdx));

                    birdImages.set(thisIdx, temp);*/
                items.set(draggedIdx, getItem());

                items.set(thisIdx, draggedCompito);
                
                List<Task> itemscopy = new ArrayList<>(getListView().getItems());

                getListView().getItems().setAll(itemscopy);
                CateringAppManager.getInstance().getEventManager().saveOrder();
                success = true;

            }

            event.setDropCompleted(success);

            event.consume();

        });

        setOnDragDone(DragEvent::consume);

    }

    @Override
    protected void updateItem(Task item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {

            setGraphic(null);

        } else {

            text.setText(item.toString());

            setGraphic(text);

        }

    }

}

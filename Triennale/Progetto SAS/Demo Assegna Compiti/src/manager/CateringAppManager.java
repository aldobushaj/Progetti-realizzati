package manager;


import bestutilsever.Preparation;
import bestutilsever.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbase.JDBaseDriver;

public class CateringAppManager {
    private static CateringAppManager singleInstance;
    //public static UserManager userManager;
    //public static MenuManager menuManager;
    public static EventManager eventManager;
    public static RecipeManager recipeManager;

    // il data manager non è presente nel DSD perché non fa parte della business logic
    //public static DataManager dataManager;
    private final JDBaseDriver dbDriver;
    
    
    private Task selectedTask;
    

    public static CateringAppManager getInstance() {
        if (CateringAppManager.singleInstance == null){
            CateringAppManager.singleInstance = new CateringAppManager();
        }
        return CateringAppManager.singleInstance;
    }
    private CateringAppManager() {
        
        // Inizializza i GRASP controller e i servizi da utilizzare
        eventManager = EventManager.getInstance();
        recipeManager = RecipeManager.getInstance();
        dbDriver = JDBaseDriver.getInstance();
    }

    public JDBaseDriver getDriver(){
        return dbDriver;
    }
    
    public ObservableList<Preparation> getPreparationsList(){
        return FXCollections.observableArrayList(dbDriver.getElencoPreparazioni());
    }

    public void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }
    public EventManager getEventManager(){
        return eventManager;
    }
}

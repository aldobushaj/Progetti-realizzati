import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Character;
import java.lang.String;
import java.util.Scanner;
import javax.swing.JFrame; //imports JFrame library
import javax.swing.JButton; //imports JButton library
import java.awt.GridLayout; //imports GridLayout library
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;





public class Visualizer{

	static int rows=10, cols=10;
    static int count = 0;
    static String[] lines = new String[10000];
    static String[] linesA = new String[10000];
    static char[][] data = new char[100][2];
    static char[][] data_map = new char[100][2];
    static char[][] map = new char[10][10];
    static char a;
    static char b;

	JFrame frame = new JFrame();
	JButton[][] grid; 


        
        static Cella [][] matrix;

        ImageIcon water = new ImageIcon("img/water.png");
        ImageIcon green = new ImageIcon("img/green.png");
        ImageIcon red = new ImageIcon("img/red.png");



	public Visualizer(){//constructor
	      buttonMatrix(rows, cols);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.pack(); //sets appropriate size for frame
	      frame.setVisible(true); //makes frame visible
       }

	public void buttonMatrix(int width, int length){ 
		frame.setLayout(new BorderLayout());
        setupMatrix();
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		JPanel panGrid = new JPanel();
		c.add(panGrid, BorderLayout.NORTH);
		panGrid.setLayout(new GridLayout(width,length)); //set layout
		grid=new JButton[width][length]; //allocate the size of grid
                int counter = 0;
        
        

        
		for(int x=0; x<width; x++){
		 	for(int y=0; y<length; y++){
				grid[x][y]=new JButton("B"+counter); //creates new button"("+x+","+y+")"
				grid[x][y].setHideActionText(true);
				grid[x][y].setFont(new Font("Arial", Font.PLAIN, 1));
				counter++;
                if (matrix[x][y].isWater())
                    grid[x][y].setIcon(water);
                a = convertI_C(x);
                b = convertI_C(y);
                if(this.findMember(a,b,data_map)){
                    if(findMember(a,b,data)){
                        grid[x][y].setIcon(green);
                    }else{
                       grid[x][y].setIcon(red); 
                    }
                }
				grid[x][y].setPreferredSize(new Dimension(50, 50));	
				panGrid.add(grid[x][y]); //adds button to grid
			}
		}

            count++;
            
	}
    
	private void setupMatrix (){
		matrix = new Cella[rows][cols];
		for (int i=0; i< rows; i++)
			for (int j=0; j<cols; j++)
				matrix[i][j]=new Cella();
	}


	public static void main(String[] args) { 
        int i = 0;
        Scanner myObj = new Scanner(System.in); 
        System.out.println("inseirsci il nome della mappa utilizzata");
        String pathname = "maps/"+myObj.nextLine();

        //nome del file contenente la mappa con cui confrontare il risultato 
        String fileName = pathname; 
        //nome del file contenente l output di agent
        String fileNameA = "file_di_log/outputAGENT.clp";
        try{
            // file contenente la mappa originale(mappa scelta dall'utente)
           lines = readUsingFileReader(fileName);
        }catch( IOException e){
            System.out.println("ioexe");
        }
        try{
            // file contenente risultato dopo aver eseguito la ricerca delle navi
           linesA = readUsingFileReader(fileNameA);
        }catch( IOException e){
            System.out.println("ioexe");
        }
        data = refineAgent(linesA);
        data_map = refineMap(lines);
       Visualizer window = new Visualizer();
	}

    private static String[] readUsingFileReader(String fileName) throws IOException {
        int i = 0;
        String[] l = new String[10000];
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        System.out.println("Reading text file using FileReader");
        while((line = br.readLine()) != null){
            l[i] = line;
            i++;   
        }
        br.close();
        fr.close(); 
        return l;
    }

    public static char[][] refineAgent(String[] a){
        int i = 0;
        int j = 0;
        char[][] b = new char[100][2];
        while(a[i] != null){
            if((a[i].indexOf("(exec"))>0){
                if((a[i].indexOf("guess)"))>0){
                    b[j][0] = a[i].charAt(a[i].length()-9);
                    b[j][1] = a[i].charAt(a[i].length()-3);
                    //System.out.println(i +" - " + b[j][0] + " " + b[j][1]);
                    j++;
                }
            }
            i++;
        }
        System.out.println("  - - - - -  " );
        return b;
    }
    
    public static char[][] refineMap(String[] a){
        int i = 0;
        int j = 0;
        char[][] b = new char[100][2];
        
        while(a[i] != null){
            if((a[i].indexOf("boat)"))>0){ 
                //System.out.println("Indice i : "+i+"\nIndice j: "+j);
                        b[j][0] = a[i].charAt(9);
                        b[j][1] = a[i].charAt(15);
                        //System.out.println(i +" - " + b[j][0] + " " + b[j][1]);
                        j++;
                }
            i++;
        }
        return b;
    }
    
    public boolean findMember(char a, char b, char[][] arr){
        int i = 0;
        while(Character.isDigit(arr[i][0])){
            if((arr[i][0] == a)&&(arr[i][1] == b)){
                return true;
            }
            i++;
        }
        return false;
    }
    
    
    public static char convertI_C(int i){
        int a = i + 48;
        char b = (char)a;
        return b;
    }
    
    

    class Cella {

 	String content;
	boolean initial;

        Cella(){
		content = "water";
		initial = false;
	}
       
        boolean isWater(){
		return content.equals("water");
	}

	void setContent(String s){
		content = s;
	}
	
	void setInitial(boolean val){
		initial = val;
	}

	boolean isInitial(){
		return initial;
	}
	
	String getContentSimple(){
		if (isWater()) return content;
		return "boat";
	}

	String getContent(){
		return content;
	}

	boolean isSub(){
		return content.equals("sub");
	}

	boolean isLeft(){
		return content.equals("left");
	}

	boolean isRight(){
		return content.equals("right");
	}

	boolean isTop(){
		return content.equals("top");
	}

	boolean isBot(){
		return content.equals("bot");
	}

     }
}

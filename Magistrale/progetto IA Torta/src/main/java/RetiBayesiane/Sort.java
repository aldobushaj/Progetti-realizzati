package RetiBayesiane;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ------------------------------------------------ESERCIZIO 4 -------------------------------------------
public class Sort {


    /*public static void main(String args[]) {

        String[]args1=new String[1];
        BayesNet Network= (BayesNet) BNParser.main(args1);
        System.out.println("variable in topological order: " + Network.getVariablesInTopologicalOrder());
        System.out.println("MIN FILL ORDER: "+MinFillOrder(Network).toString());
      //  System.out.println("MIN DEGREE ORDER: "+MinDegreeOrder(Network).toString());
        ((BayesNet)Network).printNetworkWithMarried();

    }*/
    // Prima il nodo con meno vicini
    public static List<RandomVariable> MinDegreeOrder(BayesianNetwork N) {
        BayesNet B = null;
        B = (BayesNet) deepCopy(N);
        //Lista contenete tutte le variabili
        List<RandomVariable> variables = N.getVariablesInTopologicalOrder();
        Set<Node> nodeList = new HashSet<>();
        List<RandomVariable> sortedVariables = new ArrayList<>();
        Utils.Marry(N);

        List<RandomVariable> vars = new ArrayList<>();
        vars.addAll(variables);
        for (RandomVariable variable : variables) {
            FullCPTNode minNeighbors = null; // nodo che ha mneno neighbours

            List<RandomVariable> IterList = new ArrayList<>();
            IterList.addAll(vars);
            for (RandomVariable var : IterList) {
                //la prima volta il primo nodo diventa minNeighbors
                if (minNeighbors == null)
                    minNeighbors = ((FullCPTNode) N.getNode(var));

                // dopodichè confronto tutti i nodi che trovo e se hanno meno neighbours diventano minNeihbours
                if (minNeighbors.getNeighbours().size() > ((FullCPTNode) N.getNode(var)).getNeighbours().size())
                    minNeighbors = (FullCPTNode) N.getNode(var);
            }


            assert minNeighbors != null;
            if (!variable.equals(variables.get(variables.size() - 1)))
                getFactor(minNeighbors);


            // Aggiungo minNeighbours alle varibili ordinate
            sortedVariables.add(N.getRandomVariable(minNeighbors));
            nodeList.addAll((Utils.randomVariableToNodeList(N, variables)));
            nodeList.remove(minNeighbors);
            vars.remove(N.getRandomVariable(minNeighbors));
            //Togli dai vicini i riferimenti al nodo eliminato
            for (Node node : ((FullCPTNode) minNeighbors).getNeighbours())
                ((FullCPTNode) node).removeMarriedNode(minNeighbors);

        }


        return sortedVariables;
    }
        // Prima il nodo che una volta eliminato aggiunge meno archi
    public static List<RandomVariable> MinFillOrder(BayesianNetwork B) {
        BayesNet N = null;
        //N = (BayesNet) deepCopy(B);*/

        try {
            N = (BayesNet) ((BayesNet)B).clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        List<RandomVariable> variables = N.getVariablesInTopologicalOrder();
        Set<Node> nodeList = new HashSet<>();
        List<RandomVariable> sortedVariables = new ArrayList<>();
        Utils.Marry(N);


        List<RandomVariable> vars = new ArrayList<>();
        vars.addAll(variables);
        for (RandomVariable variable : variables
        ) {
            FullCPTNode minEdgeToAdd = null; // nodo che ha mneno neighbours

            List<RandomVariable> IterList = new ArrayList<>();
            IterList.addAll(vars);

            for (RandomVariable var : IterList
            ) {

                //la prima volta il primo nodo diventa minNeighbors
                if (minEdgeToAdd == null) {
                    minEdgeToAdd = ((FullCPTNode) N.getNode(var));
                }
                // se getNumberOfEdgeToAdd(minEdgeToAdd)==0 non c'è bisogno di conferontarlo con gli altri nodi e possiamo uscire dal for
                if (getNumberOfEdgeToAdd(minEdgeToAdd) == 0)
                    break;
                // dopodichè confronto tutti i nodi che trovo e se hanno meno edge da aggiunmgere diventano minNeihbours
                if (getNumberOfEdgeToAdd(minEdgeToAdd) > getNumberOfEdgeToAdd((FullCPTNode) N.getNode(var)))
                    minEdgeToAdd = (FullCPTNode) N.getNode(var);
            }


            assert minEdgeToAdd != null;
            if (!variable.equals(variables.get(variables.size() - 1))) {
                //Togli dai vicini i riferimenti al nodo eliminato
                for (Node node : ((FullCPTNode) minEdgeToAdd).getNeighbours()
                ) {
                    ((FullCPTNode) node).removeMarriedNode((FullCPTNode) minEdgeToAdd);
                }
                getFactor(minEdgeToAdd);
            }

            // Aggiungo minEdgeToAdd alle varibili ordinate
            sortedVariables.add(N.getRandomVariable(minEdgeToAdd));
            nodeList.addAll((Utils.randomVariableToNodeList(N, variables)));
            nodeList.remove(minEdgeToAdd);
            vars.remove(N.getRandomVariable(minEdgeToAdd));

        }
        return sortedVariables;
    }

    public static void getFactor(FullCPTNode node) {
        // Set contenente il fattore del nodo corrente(tutti i nodi vicini)
        Set<Node> factor = new HashSet<>();
        factor.addAll(node.getParents());
        factor.addAll(node.getChildren());
        // Set dei nodi sposati
        Set<Node> marriedN = new HashSet<>();
        marriedN.addAll(node.getMarriedNodes());
        List<Node> marriedList = new ArrayList<>();
        marriedList.addAll(marriedN);
        // aggiungo ai married nodes tutti i nodi vicini del nodo corrente
        node.setMarriedNodes(marriedList);
        // aggiungo a factor tutti i nodi vicini a "node"
        factor.addAll(marriedN);

        List<Node> iterator = new ArrayList<>();
        iterator.addAll(factor);
        // Sleghiamo minNeighbours dalla rete (anzichè eliminarlo e ricalcolare la CPT..)
        node.setChildren(new HashSet<Node>());
        node.setParents(new HashSet<Node>());
        node.setMarriedNodes(new ArrayList<>());

        //Sposa i vicini del nodo eliminato
        for (Node first : factor
        ) {
            // tolgo il first dalla lista dei nodi con cui lo sposo altrimenti si sposa con sé stesso
            iterator.remove(first);
            for (Node second : iterator
            ) {
                ((FullCPTNode) first).addSingleMarriedNodeNode(second);
                ((FullCPTNode) second).addSingleMarriedNodeNode(first);

            }
        }

    }

    public static int getNumberOfEdgeToAdd(FullCPTNode node) {
        // numero di archi da aggiungere se viene eliminato il nodo passato come parametro
        int numberOfEdgeToAdd = 0;

        Set<Node> neighbours = new HashSet<>();
        // recupero i vicini di "node"
        neighbours.addAll(node.getNeighbours());

        for (Node neighbor : neighbours) {
            Set<Node> otherNeighbours = new HashSet<>();
            otherNeighbours.addAll(neighbours);
            // rimuovo da tutti i vicini del nodo corrente il nodo "neighbor"
            otherNeighbours.remove(neighbor);

            for (Node otherNeighbor : otherNeighbours) {
                if (!((FullCPTNode) neighbor).getNeighbours().contains(otherNeighbor)) {
                    //Sommo il numero di archi
                    numberOfEdgeToAdd++;
                }
            }

        }
        return numberOfEdgeToAdd;
    }

    private static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            Object Copy = objInputStream.readObject();
            objInputStream.close();
            return Copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

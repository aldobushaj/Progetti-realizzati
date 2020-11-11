package RetiBayesiane;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.domain.Domain;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;

import java.util.*;

public class Utils {

    //esercizio 1 Pruning
    public static void removeNode(BayesNet bn,List<Node> toRemove){
        // per tutti i nodi da rimuovere, aggiorno la Bayesian Network eliminando i nodi superflui
        for(  Node e : toRemove){
            bn.rootNodes.remove(e);
            bn.rootNodes.addAll(e.getChildren());
            bn.variables.remove(bn.getRandomVariable(e));
            bn.varToNodeMap.values().remove(e);
        }
    }
    public static List<Node> getAncestors(Node node,List<Node> ancestors){
        if(!node.getParents().isEmpty()) {
            for (Node p : node.getParents()){
                ancestors.add(p);
                getAncestors(p,ancestors);
            }
            return ancestors;
        }
        else
            return ancestors;


    }



    //esercizio 2 M separation
    public static <E> void removeNodeMSeparation(List<Node> toRemove, BayesNet bn) {
        List<Node> good = new ArrayList<>();
        for (Node toRemoveNode: toRemove
        ) {
            good.add(bn.getNode(bn.getRandomVariable(toRemoveNode)));
        }
        // memorizzo tutti i nodi radice
        List<Node> setParents = new ArrayList<>();
        setParents.addAll(bn.getRootNodes());
        // Soprt toRemove in Topological Order
        good.sort(Comparator.comparingInt( randomVariableToNodeList(bn,bn.getVariablesInTopologicalOrder()) ::indexOf));

        for(Node e : good) {
            //Se tutti i nodi radice sono da rimuovere
            if(toRemove.containsAll(bn.getRootNodes())){
                // allora cancello tutti i root nodes
                setParents.clear();
                setParents.addAll(bn.getRootNodes());
                bn.rootNodes.clear();
                for (Node temp: setParents) {
                    // e imposto i nuovi nodi radice con i figli di temp
                    bn.rootNodes.addAll(temp.getChildren());

                }
            }
            else {
                // se il nodo corrente è una rdice la rimuovo e aggiungo i suoi figli come nbuove radici
                bn.rootNodes.remove(e);
                for (Node childofRoot : e.getChildren()
                ) {
                    if (!toRemove.contains(childofRoot))
                        bn.rootNodes.add(childofRoot);
                }
            }
            bn.variables.remove(bn.getRandomVariable(e));
            bn.varToNodeMap.values().remove(e);

        }
    }
    // Marry dei nodi
    public static void Marry(BayesianNetwork bn) {
        //Vengono ottenute tutte le variabili della rete
        List<RandomVariable> networkVariables = bn.getVariablesInTopologicalOrder();
        List<FullCPTNode> networkNodes = new ArrayList<>();

        // Conversione della lista in FullCPTNode
        for (RandomVariable r : networkVariables) {
            FullCPTNode node = (FullCPTNode) bn.getNode(r);
            networkNodes.add(node);
        }
         //Per ogni variabile della rete
        for (FullCPTNode node : networkNodes) {
            Set<Node> parents = node.getParents();
            List<Node> parentsList = new ArrayList<>();
            //Lista dei parent della variabile corrente
            List<FullCPTNode> parentNodes = new ArrayList<>();
            parentsList.addAll(parents);

            // Cast Da List<Node> a List<FullCPTNode>
            for (Node n : parentsList)
                parentNodes.add((FullCPTNode) n);

            // verifico se il numero dei suoi parents è >1(in modo da fare il marry solo alle variabili con piu padri)
            if (parents.size() > 1) {
                for (Node parent : parentNodes) {
                    FullCPTNode parentNode = (FullCPTNode) parent;
                    //aggiungo alla lista dei nodi con cui si è fatta la marry tutte le variabili presenti in parentNode
                    parentNode.addMarriedNode(parentsList);
                    // togliendo la variabile corrente(in modo da non avere nella propria marriedList anche se stesso)
                    parentNode.removeMarriedNode(parent);
                }

            }

        }
    }
    //Metodo che restituisce tutte le variabili M separate, quindi da eliminare
    public static List<RandomVariable> IsMSeparated(BayesianNetwork Network, List<RandomVariable> queryList, List<RandomVariable> evidenceList) {

        /************************************   REGOLE   ***********************************
        1) nodi da visitare(bianchi)
        2) nodi esplorati(grigi)
        3) nodi visitati(neri), sono visitati tutti i nodi in cui i suoi padri e figli non hanno nodi bianchi
         */
        List<Node> white = new ArrayList<>();
        List<Node> grey = new ArrayList<>();
        List<Node> black = new ArrayList<>();

        // Inizialmente tutti i nodi sono bianchi(da visitare) quindi li aggiungo alla lista dei "white"
        for (RandomVariable node : Network.getVariablesInTopologicalOrder())
            white.add(Network.getNode(node));

        //le variabili "eliminabili" sono tutti tranne le variabili di evidenza e quelli di query quindi le escludo dalla lista "white"
        white.removeAll(randomVariableToNodeList(Network, queryList));
        white.removeAll(randomVariableToNodeList(Network, evidenceList));

        // variablesToCheck contiene tutte le variabili eccetto quelle di query ed evidenza
        List<RandomVariable> variablesToCheck = new ArrayList<>();
        // lista contenente i nodi da rimuovere che sarà restituito dal metodo
        List<RandomVariable> nodesToRemove = new ArrayList<>();

        variablesToCheck.addAll(Network.getVariablesInTopologicalOrder());
        variablesToCheck.removeAll(queryList);
        variablesToCheck.removeAll(evidenceList);

        // Lista contenete tutte le variabili rilevanti, quindi da NON eliminare
        List<RandomVariable> relevantVariabels = new ArrayList<>();
        // partendo da una delle variabili di query cerco un cammino, se lo trovo, le variabili appena analizzate non verranno eliminate in quanto non M-separate
        for (RandomVariable query : queryList) {
            List<FullCPTNode> Path = new ArrayList<>();
            //
            List<FullCPTNode> result = depthFirstSearch(Network, (FullCPTNode) Network.getNode(query), Path, white, grey, black, queryList, evidenceList);
            boolean irrelevant = true;
            // per ogni variabile da analizzare
            for (RandomVariable toCheck : variablesToCheck) {
                // se result contiene la variabile da analizzare
                if (result.contains(Network.getNode(toCheck))) {
                    // la avriabile è rilevante
                    irrelevant = false;
                    // quindi viene aggiunta alla lista delle variabili rilevanti
                    relevantVariabels.add(toCheck);
                }
                if (irrelevant)
                    // se irrilevant non è stata settata a "false" la variabile corrente non è rilevante, quindi la aggiungo alla lista dei nodi da rimuovere
                    nodesToRemove.add(toCheck);
                // setto "irrelevant" nuovamente a true in modo da non avere valori "sporchi" alla prossima iterazione
                irrelevant = true;
            }

        }

        Set<RandomVariable> set = new HashSet<>(nodesToRemove);
        variablesToCheck.removeAll(relevantVariabels);
        set.addAll(variablesToCheck);
        // Elimino dalla lista tutti i nodi che dovranno essere eliminnati(sono memorizzati in un set) per poi
        nodesToRemove.clear();
        // aggiungerli in questa riga(tramite il set opportunamente inizzializato) in modo da non avere ripetizione dei nodi da eliminare
        nodesToRemove.addAll(set);
        return nodesToRemove;
    }
    //Depth Fisrt Search
    public static List<FullCPTNode> depthFirstSearch(BayesianNetwork Network, FullCPTNode node, List<FullCPTNode> Path, List<Node> white, List<Node> grey, List<Node> black, List<RandomVariable> queryList, List<RandomVariable> evidenceList) {
        // Il nodo corrente è esplorato, quindi cambio il suo colore in grigio
        grey.add(node);
        // e lo tolgo dalla lista dei bianchi
        white.remove(node);
        // utile a rimuovere le ripetizioni
        Set<Node> set = new HashSet<Node>(grey);
        grey.clear();
        grey.addAll(set);

        //Lista contenente tutti i nodi raggiungibili dal nodo corrente
        List<Node> neighbors = new ArrayList<>();
        neighbors.addAll(node.getParents());
        neighbors.addAll(node.getChildren());
        neighbors.addAll(node.getMarriedNodes());
        // tranne le variabili di evidenza
        for (RandomVariable ev : evidenceList) {
            Node n = Network.getNode(ev);
            neighbors.remove(n);
        }

        // In questo caso non ci sono percorsi possibili da una query variable ad un altra variabile senza passare da una evidence variable
        if (neighbors.isEmpty())
            return Path;

        //lista contenente tutti i nodi di colore nero e grigio, in modo da settare opportunamente i nodi a nero(visitati)
        List<Node> greyBlack = new ArrayList<>();
        greyBlack.addAll(grey);
        greyBlack.addAll(black);
        // utile a rimuovere le ripetizioni
        Set<Node> set2 = new HashSet<Node>(greyBlack);
        greyBlack.clear();
        greyBlack.addAll(set2);

        if (greyBlack.containsAll(neighbors)) {
            // se tutti i nodi intorno sono girgi o neri allora il colore del nodo corrente sarà settato a nero
            black.add(node);
            grey.remove(node);
        }

        //lista contenete solo i nodi bianchi
        List<Node> onlyWhite = new ArrayList<>();
        onlyWhite.addAll(neighbors);
        onlyWhite.removeAll(greyBlack);

        // richiamo ricorsivamente la depth search su tutti i nodi
        for (Node neighbor : neighbors) {
            FullCPTNode temp = (FullCPTNode) neighbor;
            // Diamo la priorità ai nodi bianchi quindi la la visita nella depth first avverrà prima nei nodi bianchi(da visitare)
            for (Node currentNode : onlyWhite) {
                FullCPTNode CPTnode = (FullCPTNode) currentNode;
                Set<FullCPTNode> set3 = new HashSet<FullCPTNode>(Path);
                Path.clear();
                // aggiungo il set alla Path in modo da non avere duplicati
                Path.addAll(set3);
                // quindi aggiungo il nodo corrente alla Path
                Path.add(CPTnode);

                return depthFirstSearch(Network, CPTnode, Path, white, grey, black, queryList, evidenceList);
            }

            // Se la condizione di questo if viene soddisfatta allora non ho più nodi bianchi da esplorare, quindi posso passare al visita dei nodi grigi
            //devo prima però assicurarmi che il nodo non sia nero
            if (!black.contains(temp)) {
                Set<FullCPTNode> set3 = new HashSet<FullCPTNode>(Path);
                Path.clear();
                // aggiungo il set alla Path in modo da non avere duplicati
                Path.addAll(set3);
                // quindi aggiungo il nodo corrente alla Path
                Path.add(temp);
                // faccio la chiamata ricorsiva, passando come nodo da visitare il neighbor del nodo corrente(temp)
                return depthFirstSearch(Network, temp, Path, white, grey, black, queryList, evidenceList);
            } else {
                //altrimenti non faccio niente e passo al prossimo nodo
                continue;
            }

        }
        // se ho trovato una path significa che c'è un percorso dalla/e variabile di query a un nodo qualsiasi(tranne quelli di query)
        // evitando le variabili di evidenza
        return Path;

    }





    // esercizio 3 Archi irrilevanti
    // Rimuoviamo dalle evidenze gli archi ai nodi figli
    public static <E> void removeEdgePruning(List<RandomVariable> Evidence, BayesNet bn) {
        // Cancella tutti i figli delle evidenze
        for (RandomVariable currentEvidence: Evidence) {

            FullCPTNode node=(FullCPTNode) bn.getNode(currentEvidence);
            // per tutti i figli dell'evidenza corrente
            for (Node child: node.getChildren())
                ((FullCPTNode) child).removeParent(node);


            Set <Node> children = new LinkedHashSet<>();
            node.setChildren(children);

        }

    }
    // Recupera un nodo Dato il nome
    public static Set<Node> getNodeByName(BayesNet bn,Set<String> names){
        Set<Node> nodes = new HashSet<>();
        for (RandomVariable var: bn.getVariablesInTopologicalOrder()) {
            for (String name:names) {
                //se la variabile corrente è contenuta nel set "names" viene aggionta al set "nodes" che verrà succesivamente restituito dal metodo
                if (var.getName().equals(name))
                    nodes.add(bn.getNode(var));
            }

        }
        return nodes;
    }









    public static Node getNodeByName(BayesNet bn,String name){
        for (RandomVariable var: bn.getVariablesInTopologicalOrder()
        ) {
            if(var.getName().equals(name))
                return bn.getNode(var);
        }
        return null;
    }


    public static List<RandomVariable> nodeToRandomVariableList(BayesianNetwork Network, List<Node> listNode) {
        List<RandomVariable> randVar = new ArrayList<>();
        for (Node node : listNode) {
            randVar.add(Network.getRandomVariable(node));
        }
        return randVar;
    }
    public static List<Node> randomVariableToNodeList(BayesianNetwork Network, List<RandomVariable> listRand) {
        List<Node> listNode = new ArrayList<>();
        for (RandomVariable node : listRand) {
            listNode.add(Network.getNode(node));
        }
        return listNode;
    }

    public static void printNetwork(BayesNet bn){
        System.out.println("Variables in topological order: " +bn.getVariablesInTopologicalOrder());
        for (RandomVariable var:bn.getVariablesInTopologicalOrder()
        ) {
            FullCPTNode node= (FullCPTNode) bn.getNode(var);
            CPT cpt= (CPT) node.getCPT();

            System.out.println("Nodo: "+var.getName()+ " ID: "+System.identityHashCode( bn.getNode(var)) +" Parents: "+bn.getNode(var).getParents().toString()+ " Children: "+ bn.getNode(var).getChildren().toString()+ " CPT: "+cpt.getTable());
        }
    }
    public static void printNetworkWithMarried(BayesNet bn){
        System.out.println("Variables in topological order: " +bn.getVariablesInTopologicalOrder());
        for (RandomVariable var:bn.getVariablesInTopologicalOrder()
        ) {
            FullCPTNode node= (FullCPTNode) bn.getNode(var);
            CPT cpt= (CPT) node.getCPT();

            System.out.println("Nodo: "+var.getName()+ " ID: "+System.identityHashCode( bn.getNode(var)) +" Married: "+node.getMarriedNodes().toString() +" Parents: "+bn.getNode(var).getParents().toString()+ " Children: "+ bn.getNode(var).getChildren().toString()+ " CPT: "+cpt.getTable());
        }
    }
    public static List getDomain(BayesNet bn){
        List <Node> nodes = bn.getAllNodes();
        List <Domain> domains_list=new ArrayList<>();
        for (Node node:nodes) {
            RandVar rand = (RandVar)bn.getRandomVariable(node);
            domains_list.add(rand.getDomain());
            System.out.println("Name: "+rand.getName()+ " ; Domain: "+rand.getDomain()+";");
            FullCPTNode n= (FullCPTNode)node;
            CPT prova = (CPT)n.getCPT();
            System.out.println("CPT: "+prova.getTable());
        }
        return domains_list;
    }

    public static List<List> getQuery(String variables, List variablesList) {
        List<List> values = new ArrayList<>();

        boolean correct = true;
        do {
            correct = true;
            System.out.println("Insert query variable");
            Scanner scanner = new Scanner(System.in);
            String queryString = scanner.nextLine();

            String[] queryArray = queryString.split("\\s+"); // query variables array
            List<RandomVariable> queryList = new ArrayList<>();

            for (String queryVariable : queryArray) {

                queryList.add(new RandVar(queryVariable, new BooleanDomain()));
                if (!variables.contains(queryVariable)) {
                    correct = false;
                    System.out.println("Wrong input!");
                }
            }
            if (!correct) // se non è corretto l'input, ripeti l'inserimento delle   query
                continue;
            System.out.println("Insert evidence variable");
            String evidenceQuery = scanner.nextLine();
            String[] evidenceArray = evidenceQuery.split("\\s+");


            List<RandomVariable> evidenceVariable = new ArrayList<>();
            List<AssignmentProposition> ap = new ArrayList<>();

            for (String evidence : evidenceArray
            ) {
                String[] NameValues = evidence.split("=");
                //Check evidenze corrette
                if (!variables.contains(NameValues[0])) {
                    correct = false;
                    System.out.println("Wrong input!");
                    continue;
                }
                RandVar var = getVariableFromList(variablesList, NameValues[0]);
                assert var != null;
                if (var.getDomainType().equals(BooleanDomain.class)) {
                    evidenceVariable.add(new RandVar(NameValues[0], new BooleanDomain()));
                    ap.add(new AssignmentProposition(evidenceVariable.get(evidenceVariable.size() - 1), NameValues[1].equalsIgnoreCase("TRUE") ? Boolean.TRUE : Boolean.FALSE));
                } else if (var.getDomainType().equals(ArbitraryTokenDomain.class)) {
                    //Arbitrary token domain
                    //System.out.println(var.getName() + " HAS ARBITRARY TOKEN DOMAIN");
                    evidenceVariable.add(new RandVar(NameValues[0], new ArbitraryTokenDomain()));
                    ap.add(new AssignmentProposition(evidenceVariable.get(evidenceVariable.size() - 1), NameValues[1]));

                } else if (var.getDomainType().equals(FiniteIntegerDomain.class)) {
                    //Finite integer domain
                    //System.out.println(var.getName() + " HAS FINITE INTEGER DOMAIN");
                    evidenceVariable.add(new RandVar(NameValues[0], new FiniteIntegerDomain()));
                    ap.add(new AssignmentProposition(evidenceVariable.get(evidenceVariable.size() - 1),Integer.parseInt(NameValues[1]) ));
                }
            }
            if(!correct) // se non è corretto ripeti l'input
                continue;
            System.out.println("Insert desired ordering: 1 InvertedTopological; 2 MinFill; 3 MinDegree;");
            int ordering = scanner.nextInt();
            if(ordering<1 ||ordering >3){
                correct=false;
                System.out.println("Ordering is wrong, must be 1,2 or 3.");
                continue;
            }
            if (correct) {
                // Lista values che conterrà alla prima posizione le variabili di query inserite
                values.add(queryList);
                //alla seconda posizione le varibili di evidenza
                values.add(evidenceVariable);
                //alla terza posizione le assignment proposition
                values.add(ap);
                List<Integer> order = new ArrayList<Integer>();
                order.add(ordering);
                // alla quarta l'ordinamento scelto dall'utente
                values.add(order);

            }


        } while (!correct);


        return values;
    }
    public static RandVar getVariableFromList(List<RandVar> variableList, String name) {
        for (RandVar var : variableList)
            if (var.getName().equals(name))
                return var;

        return null;
    }

}

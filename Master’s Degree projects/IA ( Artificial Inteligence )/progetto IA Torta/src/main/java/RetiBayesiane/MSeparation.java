package RetiBayesiane;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.AbstractFiniteDomain;
import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;

import java.util.*;

/*
 * MODIFICHE AL REPO AIMA
 * classe CPT: +getTable()
 * BaysesNet: rimossi private
 * BayesNet: +getAllNodes(), getKeyByValue, getRandomVariable
 * bayesNet: +removeNode()
 * */
// ------------------------------------------------ESERCIZIO 2 -------------------------------------------
public class MSeparation<E> {

    public static <T> void main(String[] args) throws CloneNotSupportedException {

        //Rete bayseana di esempio
        BayesianNetwork Network = BayesNetExampleFactory.constructBurglaryAlarmNetworkTornado();


        //BayesianNetwork Network = BayesNetExampleFactory.constructBurglaryAlarmNetworkTornado();


        //PER LEGGERE RETI CON PARSER
        //String[]args1=new String[1];
        //BayesNet Network= (BayesNet) BNParser.main(args1);
        //System.out.println("variable in topological order: "+Network.getVariablesInTopologicalOrder());


        BayesNet NetworkBayes = (BayesNet) Network;
        Utils.getDomain(NetworkBayes);
        List<List> queryList = Utils.getQuery(Network.getVariablesInTopologicalOrder().toString(), Network.getVariablesInTopologicalOrder());
        //Lista delle variabili di query
        List<RandomVariable> queryVariable = queryList.get(0);
        //Lista delle variabili di evidenza
        List<RandomVariable> evidenceVariable = queryList.get(1);
        //Lista delle assignment proposition
        List<AssignmentProposition> ap = queryList.get(2);
        // Intero che rappresenta l'ordine(minDegree, minFill, invertedTopologicalOrder)
        int order = (int) queryList.get(3).get(0);

        CategoricalDistribution cd;
        BayesInference[] allbi = new BayesInference[]{new EliminationAsk()};

        BayesNet B = (BayesNet) Network;
        // Marry dei nodi opportuni in modo da ottenere un moral Graph
        Utils.Marry(B);
        // Lista contenente tutti i nodi da rimuovere, in quanto M separati
        List<RandomVariable> listToRemove = Utils.IsMSeparated(Network, queryVariable, evidenceVariable);

        // nodi da elimnare convertito in Lista di nodi
        List<Node> listNodeToRemove = new ArrayList<>();
        for (RandomVariable varToRemove : listToRemove)
            listNodeToRemove.add(B.getNode(varToRemove));

        Set<Node> copyOfCHildNodeToRemove = new HashSet<>();

        /*-------------------- Passi da eseguire pe modificare le CPT ---------------------
        1 -Svuotare child e parent dei nodi parent
        2- Aggiornare i parent del clone lasciando solo i sopravvissuti
        3- ggiornare i child dei parent sopravvissuti
        4- aggiornare i parent dei children con il clone e non  il padre originale (padre J=A')

        * */

        //creiamo la copia dei figli di tutti i nodi che vanno rimossi
        // Map che restituisce il nodo originale dato il colone
        Map<Node, Node> cloneOriginalmap = new HashMap<>(); //<Clone,Original>

        //----------------- FOR PER CRARE I CLONI E SISTEMARRE GLI ARCHI DEI CHILD E PARENT
        for (Node node : listNodeToRemove) {
            for (Node child : node.getChildren()) {
                FullCPTNode childCPTNode = (FullCPTNode) child;
                //creo i cloni di tutti i filgi dei nodi da rimuovere
                Node cloneChild = (Node) childCPTNode.clone();
                // lista contenete i cloni appena creati
                copyOfCHildNodeToRemove.add(cloneChild);
                cloneOriginalmap.put(cloneChild, child);

                //vengono cancellati i parent e i children del nodo originale
                childCPTNode.setParents(new HashSet<>());
                childCPTNode.setChildren(new HashSet<>());
            }

        }

        Set<Node> parentsOfCopyOfChild = new HashSet<>();
        for (Node node : copyOfCHildNodeToRemove) {
            FullCPTNode child = (FullCPTNode) node;

            // Aggiungo ai miei parent il riferimento al nodo clone
            parentsOfCopyOfChild.addAll(child.getParents());

            // Copia di parentsOfCopyOfChild per poter iterare nel for e modificare parentsOfCopyOfChild senza avere errori di ConcurrentAcces
            Set<Node> copyOfParents = new HashSet<>();
            copyOfParents.addAll(parentsOfCopyOfChild);

            //Aggiornare i parent
            for (Node parent : parentsOfCopyOfChild) {
                if (listNodeToRemove.contains(parent)) {
                    // 2- Aggiornare i parent del clone lasciando solo i sopravvissuti
                    child.removeParent(parent);
                    ((FullCPTNode) parent).addChildren(child);

                } else {
                    // togliemo il riferimento del parent al nodo originale
                    ((FullCPTNode) parent).removeChildren(cloneOriginalmap.get(child));
                    //aggiungo al parent sopravvissuto il riferimento al nodo clone
                    ((FullCPTNode) parent).addChildren(child);
                }
            }

            // Aggiornare i children
            for (Node childrenNode : child.getChildren()) {
                //rimuovo il parent originale
                ((FullCPTNode) childrenNode).removeParent(cloneOriginalmap.get(child));
                //aggiungo ai parent il nodo clone
                ((FullCPTNode) childrenNode).addParent(child);
            }

            // A questo punto basta sostituire l'originale con il nodo clone nella rete bayesiana
            //Recupero il nodo clone dalla rete
            RandomVariable originalNode = Network.getRandomVariable(child);
            //Recupero il nodo priginale dalla rete
            FullCPTNode originalFullCptNode = (FullCPTNode) Network.getNode(originalNode);
            //Togliamo dai children del clone i nodi da eliminare
            Set<Node> prunedChildren = child.getChildren();
            prunedChildren.removeAll(listNodeToRemove);
            // quindi aggiorno i figli del nodo clone, che conterrà tutti tranne i nodi da eliminare
            child.setChildren(prunedChildren);

            //svuoto i children e i parents del noto originale
            originalFullCptNode.setChildren(new HashSet<>());
            originalFullCptNode.setParents(new HashSet<>());
            // e svuoto la lista dei nodi clone che verrà inizializzata opportunamente alla prossima iterazione
            parentsOfCopyOfChild.clear();


            // Aggiorniamo la rete bayesiana con i cloni
            B.variables.set(B.variables.indexOf(B.getRandomVariable(cloneOriginalmap.get(child))), B.getRandomVariable(child));
            Set<RandomVariable> variableSet = new HashSet<>();
            B.varToNodeMap.replace(B.getRandomVariable(child), cloneOriginalmap.get(child), child);


        }

        // Lista contenente i valori dei parent "sopravvissuti"
        List<Object> parentsValues = new ArrayList<>();

        Utils.removeNodeMSeparation(listNodeToRemove, B);
        System.out.println("Bayesian Network after elimination of irrilevant nodes : " + B.getVariablesInTopologicalOrder());

        // elimino tutti i nodi da rimuovere
        copyOfCHildNodeToRemove.removeAll(listNodeToRemove);

        //---------- FOR CHE MODIFICA LA CPT DEI CLONI (CON LA RETE GIA' AGGIORNATA)
        for (Node child : copyOfCHildNodeToRemove) {
            FullCPTNode temp = (FullCPTNode) child;

            FiniteNode fn = (FiniteNode) child;
            Node[] parentArray = child.getParents().toArray(Node[]::new);
            // se il numero dei parents è uguale a zero
            if (child.getParents().size() == 0) {
                Set<RandomVariable> parentVariables = (((FullCPTNode) child)).getCPT().getParents();

                //per ogni padre del nodo corrente
                for (RandomVariable parentRandomVar : parentVariables) {
                    RandVar parentVar = (RandVar) parentRandomVar;
                    // recupero il suo dominio(int,boolean, ecc...)
                    AbstractFiniteDomain domain = (AbstractFiniteDomain) parentVar.getDomain();
                    if (parentVar.getDomainType().equals(BooleanDomain.class)) {
                        parentsValues.add(true); //default is true
                    }
                    if (parentVar.getDomainType().equals(FiniteIntegerDomain.class)) {
                        parentsValues.add(((Integer) domain.getPossibleValues().iterator().next()));
                    }
                    if (parentVar.getDomainType().equals(ArbitraryTokenDomain.class)) {
                        parentsValues.add(domain.getPossibleValues().iterator().next());
                    }
                }
                // lista contenente tutti i padri del nodo corrente(tutti i padre che non sono stati eliminati)
                List<RandomVariable> parentsVarList = new ArrayList<>();
                parentsVarList.addAll(parentVariables);
                Object[] arrayOfParentsValues = parentsValues.toArray(Object[]::new);
                // e chiamo la select domain che modificherà opportunamente le CPT
                temp.selectDomain(Utils.randomVariableToNodeList(Network, parentsVarList), arrayOfParentsValues);

            }
            // se invece il numero dei padri è > 0
            if (child.getParents().size() > 0) {
                // lista dei valori da assegnare ai padri del nodo corrente(child)
                List<AssignmentProposition> copyOfAssignment = new ArrayList<>();
                // itero su tutti i padri del nodo corrente
                for (RandomVariable parentRandomVar : ((FullCPTNode) child).getCPT().getParents()) {
                    RandVar parentVar = (RandVar) parentRandomVar;
                    AbstractFiniteDomain domain = (AbstractFiniteDomain) parentVar.getDomain();
                    // recupero il dominio del padre corrente
                    if (listToRemove.contains(parentRandomVar)) {
                        if (parentVar.getDomainType().equals(BooleanDomain.class)) {
                            copyOfAssignment.add(new AssignmentProposition(parentVar, true));
                            parentsValues.add(true); //default is true
                        }
                        if (parentVar.getDomainType().equals(FiniteIntegerDomain.class)) {
                            parentsValues.add(((Integer) domain.getPossibleValues().iterator().next()));
                            copyOfAssignment.add(new AssignmentProposition(parentVar, ((Integer) domain.getPossibleValues().iterator().next())));
                        }
                        if (parentVar.getDomainType().equals(ArbitraryTokenDomain.class)) {
                            parentsValues.add(domain.getPossibleValues().iterator().next()); //default is true
                            copyOfAssignment.add(new AssignmentProposition(parentVar, domain.getPossibleValues().iterator().next()));
                        }

                    }

                }

                AssignmentProposition[] assignmentPropositionsArray = copyOfAssignment.toArray(AssignmentProposition[]::new);
                // Chiamo la getFactor ottenendo il fattore passando come parametro il valore dei padri di "child"
                Factor f = fn.getCPT().getFactorFor(assignmentPropositionsArray);
                double[] values = f.getValues();
                // modifico opportunamente la CPT del nodo corrente(child)
                temp.setCPT(Network.getRandomVariable(child), Utils.randomVariableToNodeList(B, evidenceVariable), values, parentArray);
            }


        }


        //Elimination
        RandomVariable[] queryArray = queryVariable.toArray(RandomVariable[]::new);
        AssignmentProposition[] assignmentPropositions = ap.toArray(AssignmentProposition[]::new);
        cd = allbi[0].ask(queryArray, assignmentPropositions, B, order);
        System.out.println("Risultato della query inserita :\n" + Arrays.toString(cd.getValues()));
    }
}
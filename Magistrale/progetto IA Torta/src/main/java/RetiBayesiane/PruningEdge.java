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
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;

import java.util.*;

// ------------------------------------------------ESERCIZIO 3 -------------------------------------------
public class PruningEdge {
    public static <T> void main(String[] args) throws CloneNotSupportedException {
        //Rete bayseana di esempio
        BayesianNetwork Network = BayesNetExampleFactory.constructBurglaryAlarmNetworkTornado();
        BayesNet B = (BayesNet) Network;

        BayesNet NetworkBayes = (BayesNet) Network;
        Utils.getDomain(NetworkBayes);
        List<List> queryList = Utils.getQuery(Network.getVariablesInTopologicalOrder().toString(), Network.getVariablesInTopologicalOrder());
        //Lista delle variabili di query
        List<RandomVariable> queryVariable = queryList.get(0);
        //Lista delle variabili di evidenza
        List<RandomVariable> evidenceVariable = queryList.get(1);
        //Ordino le Variabili di evidenza in ordine Topologico in mdo da non dare errori di CPT
        evidenceVariable.sort(Comparator.comparingInt(B.getVariablesInTopologicalOrder()::indexOf));
        //Lista delle assignment proposition
        List<AssignmentProposition> ap = queryList.get(2);
        // Intero che rappresenta l'ordine(minDegree, minFill, invertedTopologicalOrder)
        int order= (int) queryList.get(3).get(0);

        CategoricalDistribution cd;
        BayesInference[] allbi = new BayesInference[]{new EliminationAsk()};

        //creiamo la copia dei figli di tutti i nodi che vanno rimossi
        // Map che restituisce il nodo originale dato il colone
        Map<Node, Node> cloneOriginalmap = new HashMap<>(); //<Clone,Original>
        // Mappa nodo originale --> Parent
        Map<Node, Set<String>> originalParents = new HashMap<>();
        // Set contenente tutti i nodi clonati
        Set<Node> cloneSet = new HashSet<>();
        // set contenente i nodi già processati
        Set<Node> processedNodes = new HashSet<>();
        //------------------FOR IL QUALE CREA TUTTI I CLONI NECESSARI -------------------------
        for (RandomVariable evidence : evidenceVariable) {
            FullCPTNode fullCPTEvidence = (FullCPTNode) B.getNode(evidence);
            // otteniamo tutti i figli delle evidenze
            for (Node childEvidence : ((FullCPTNode) B.getNode(evidence)).getChildren()) {
                // se il nodo corrente non è già stato processato (gestisco il caso in cui un padre abbia lo stesso figlio, es: Burglary, Earthquake hanno come figlio Alarm)
                if (!processedNodes.contains(childEvidence)) {
                    // aggiungo il nodo corrente ai nodi processati
                    processedNodes.add(childEvidence);
                    FullCPTNode childCPTNode = (FullCPTNode) childEvidence;
                    //CLONIAMO IL CHILD
                    Node cloneChild = (Node) childCPTNode.clone();
                    // settiamo a TRUE il booleano che indica se il nodo è un clone
                    ((FullCPTNode) cloneChild).isClone = true;
                    // aggiungo il clone alla lista dei nodi clone
                    cloneSet.add(cloneChild);
                    // aggiungo alla map il nodo originale e quello clonato
                    cloneOriginalmap.put(cloneChild, childCPTNode);
                    // set contenente i nomi dei padri del figlio corrente(childEvidence)
                    Set<String> parentsNames = new HashSet<>();
                    for (Node par : childCPTNode.getParents())
                        parentsNames.add(B.getRandomVariable(par).getName());

                    // aggiungo alla Map il padre originale e quello copia
                    originalParents.put(childCPTNode, parentsNames);
                }

            }

        }

        // "sleghiamo" il nodo originale dalla rete Bayesiana eliminando tutti i suoi parent e children
        for (RandomVariable evidence : evidenceVariable) {
            // otteniamo tutti i figli delle evidenze
            for (Node childEvidence : B.getNode(evidence).getChildren()) {
                FullCPTNode childCPTNode = (FullCPTNode) childEvidence;
                childCPTNode.setParents(new HashSet<>());
                childCPTNode.setChildren(new HashSet<>());

            }
        }

        // Aggiorno la rete bayesiana sostituendo i nodi originali con i cloni
        for (Node clone : cloneSet) {
            // Sistemo i parent del clone
            for (Node parent : clone.getParents()) {
                ((FullCPTNode) parent).removeChildren(cloneOriginalmap.get(clone)); // rimuovo l'originale dai figli dell'evidenza
                ((FullCPTNode) parent).addChildren(clone); // aggiungo il clone ai figli dell'evidenza
            }

            //Sistemiamo i fligli del clone
            for (Node childOfClone : clone.getChildren()) {
                // tolgiamo il nodo originale dai padri del child
                ((FullCPTNode) childOfClone).removeParent(cloneOriginalmap.get(clone));
                // Aggiungo il clone ai parent del child
                ((FullCPTNode) childOfClone).addParent(clone);
            }

            // Aggiorniamo la rete bayesiana con i cloni sostituiti alle variabili originali
            B.variables.set(B.variables.indexOf(B.getRandomVariable(cloneOriginalmap.get(clone))), B.getRandomVariable(clone));
            Set<RandomVariable> variableSet = new HashSet<>();
            B.varToNodeMap.replace(B.getRandomVariable(clone), cloneOriginalmap.get(clone), clone);
        }



        //----Rimuoviamo dalle evidenze gli archi ai nodi figli
        Utils.removeEdgePruning(evidenceVariable, B);
        // converto il set contenente i cloni in una Lista
        List<Node> cloneList = new ArrayList<>();
        cloneList.addAll(cloneSet);

        //ordino i cloni in ordine topologico
        cloneList.sort(Comparator.comparingInt(Utils.randomVariableToNodeList(B,B.getVariablesInTopologicalOrder())::indexOf));

        for (Node clone : cloneList) {
            // Lista contenente i padri del nodo corrente
            List<AssignmentProposition> copyOfAssignment = new ArrayList<>();
            // lista contenente i valori delle variabili parent
            List<Object> parentValues = new ArrayList<>();

            for (AssignmentProposition currentEvidence : ap)
                parentValues.add(currentEvidence.getValue());

            //Lista contenente i padri interessati
            List<Node> retainedParents = new ArrayList<>();
            retainedParents.addAll(Utils.getNodeByName(B,originalParents.get(cloneOriginalmap.get(clone))));
            retainedParents.retainAll(Utils.randomVariableToNodeList(B, evidenceVariable));

            // dai parent che sono evidenza otteniamo le assignment proposition
            for (Node parentAssignment : retainedParents)
                for (AssignmentProposition assignmentProp : ap)
                    if (assignmentProp.getTermVariable().equals(((RandVar) B.getRandomVariable(parentAssignment)))) {
                        copyOfAssignment.add(assignmentProp);
                    }



            AssignmentProposition[] assignmentPropositionsArray = copyOfAssignment.toArray(AssignmentProposition[]::new);
            Factor f = ((FiniteNode) clone).getCPT().getFactorFor(assignmentPropositionsArray);
            double[] values = f.getValues();
            Node[] interestedPArentsArray = retainedParents.toArray(Node[]::new);
            // Modifico la CPT del nodo corrente opportunamente chiamando il metodo setCPT
            ((FullCPTNode) clone).setCPT(Network.getRandomVariable(clone), Utils.randomVariableToNodeList(B, evidenceVariable), values, interestedPArentsArray);


        }

        RandomVariable[] queryArray = queryVariable.toArray(RandomVariable[]::new);
        AssignmentProposition[] assignmentPropositions = ap.toArray(AssignmentProposition[]::new);
        cd = allbi[0].ask(queryArray, assignmentPropositions, B,order);
        System.out.println("Result of Ask:\n" + cd.toString());
    }
}

package RetiBayesiane;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.proposition.AssignmentProposition;

import java.util.*;

/*
 * MODIFICHE AL REPO AIMA
 * classe CPT: +getTable()
 * BaysesNet: rimossi private
 * BayesNet: +getAllNodes(), getKeyByValue, getRandomVariable
 * bayesNet: +removeNode()
 *
 * */

// ------------------------------------------------ESERCIZIO 1 -------------------------------------------
public class Pruning<E> {

    public static <T> void main(String[] args) {
        //Rete bayseana di esempio
        BayesianNetwork Network = BayesNetExampleFactory.constructBurglaryAlarmNetworkTornado();

        //BayesianNetwork Network = BayesNetExampleFactory.constructBurglaryAlarmNetworkTornado();

        //
        //String[]args1=new String[1];
        //BayesNet Network= (BayesNet) BNParser.main(args1);
        //System.out.println("variable in topological order: "+Network.getVariablesInTopologicalOrder()());

        BayesNet NetworkBayes = (BayesNet) Network;
        Utils.getDomain(NetworkBayes);
        List<List> queryList = Utils.getQuery(NetworkBayes.getVariablesInTopologicalOrder().toString(), NetworkBayes.getVariablesInTopologicalOrder());
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
        // Lista che conterrà tutti gli antenati delle variabili opportune
        List<Node> Ancestors = new ArrayList<Node>();

        //Aggiungo gli antenati della/e variabili di evidenza
        for (RandomVariable var : evidenceVariable
        ) {
            // Aggiungo gli antenati della variabile di evidenza corrente(var)
            Ancestors.addAll(Utils.getAncestors(Network.getNode(var), Ancestors));
            // Aggiungo la varibaile corrente
            Ancestors.add(Network.getNode(var));
        }
        //Aggiungo gli antenati della/e variabili di query
        for (RandomVariable var : queryVariable
        ) {
            // Aggiungo gli antenati della variabile di query corrente(var)
            Ancestors.addAll(Utils.getAncestors(Network.getNode(var), Ancestors));
            // Aggiungo la varibaile corrente
            Ancestors.add(Network.getNode(var));
        }
        Set<Node> set = new LinkedHashSet<>(Ancestors);
        Ancestors.clear();
        // Lista degli antenati enza duplicati
        Ancestors.addAll(set);

        //Lista dei nodi da eliminare, inizialmente sono tutti i nodi della rete
        List<Node> total = B.getAllNodes();
        List<Node> finalAncestors = Ancestors;
        //da total rimuoviamo i nodi ancestor
        total.removeIf(i -> finalAncestors.contains((Node) i));
        System.out.println("Nodes to prune: " + total.toString());
        // Rimuovo dalla rete i nodi selezionati precedentemente
        Utils.removeNode(B, total);

        System.out.println("After pruning: " + B.getVariablesInTopologicalOrder());

        //Rimuoviamo dai children e dai parent variabili che non servono più
        for (Node node : B.getAllNodes()) {
            node.getChildren().removeAll(total);
            node.getParents().removeAll(total);
        }

        // Chiamo la Elimination Ask
        RandomVariable[] queryArray = queryVariable.toArray(RandomVariable[]::new);
        AssignmentProposition[] assignmentPropositions = ap.toArray(AssignmentProposition[]::new);
        // eseguo la ask sulla query inserita dall'utente, con l'ordine specificato
        cd = allbi[0].ask(queryArray, assignmentPropositions, B, order);
        System.out.println("Risultato della query inserita :\n" + Arrays.toString(cd.getValues()));


    }
}
package dynamic.probability

import aima.core.probability.proposition.AssignmentProposition


import dynamic.probability.bayes.CustomEliminationAsk
import dynamic.probability.bayes.Inferences.getCustomEliminationAsk
import dynamic.probability.bayes.Inferences.minNeighboursHeuristicFunction
import dynamic.probability.bayes.*
import dynamic.probability.bayes.BayesNetsFactory.getBigDynamicNetworkExample


fun main(args: Array<String>) {


    //Creazione inferenza
    val inference = getCustomEliminationAsk(
            inferenceMethod = CustomEliminationAsk.InferenceMethod.STANDARD,
            hMetrics = minNeighboursHeuristicFunction(),
            removeIrrelevantRVs = false,
            showMoralGraph = false,
            delay = 1000
    )
    //Creazione e scelta rete Complex, Simnple o Big
    val dynNet = CustomDynamicBayesianNet(getBigDynamicNetworkExample(), inference)

    // Avanzamento della rete a due instanti successivi
    dynNet.forward()
    dynNet.forward()


    // per brevità prendiamo come query la prima e come evidenza l'ultima = true
    val queryVars = dynNet.variablesInTopologicalOrder.first()
    val evidenceVar1 = dynNet.variablesInTopologicalOrder.last()
    val res = inference.ask(arrayOf(queryVars), arrayOf(AssignmentProposition(evidenceVar1, true)), dynNet) as CustomFactor

    //stampa dei risultati
    println(res.printTable())
}
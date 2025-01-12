//@file:Suppress("HasPlatformType", "FunctionName")

package dynamic.probability.bayes

import aima.core.probability.RandomVariable
import aima.core.probability.bayes.BayesInference
import aima.core.probability.bayes.BayesianNetwork
import aima.core.probability.bayes.DynamicBayesianNetwork
import aima.core.probability.bayes.Node
import aima.core.probability.bayes.impl.CPT
import aima.core.probability.bayes.impl.DynamicBayesNet
import aima.core.probability.bayes.impl.FullCPTNode
import aima.core.probability.proposition.AssignmentProposition
import dynamic.probability.utils.WrongDistributionException
import dynamic.probability.bayes.CustomEliminationAsk
import dynamic.probability.utils.*
import kotlin.collections.*
import dynamic.probability.bayes.CustomEliminationAsk.InferenceMethod.STANDARD
import dynamic.probability.bayes.Inferences.getCustomEliminationAsk
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Representation of a Dynamic Bayesian network unrolling using
 * rollup filtering.
 */
class CustomDynamicBayesianNet: DynamicBayesianNetwork {

    /**
     * The [BayesInference] technique to be used to propagate in time.
     */
    var inference: BayesInference
    private var currentSlice: DynamicBayesianNetwork

    /**
     * Constructor for a newly generated network.
     * @param priorNetwork The original beliefs eventually used to start from beginning.
     * @param X_0_to_X_1 A [Map] between [RandomVariable]s connecting the beliefs to the status variables.
     * @param E_1 A [Set] of [RandomVariable]s contained inside their nodes.
     * @param rootNodes The root [Node]s to be used to initialize the network.
     * @param inference The [BayesInference] technique to be used to propagate in time.
     */
    constructor(priorNetwork: BayesianNetwork,
                X_0_to_X_1: Map<RandomVariable, RandomVariable>,
                E_1: Set<RandomVariable>,
                rootNodes: Array<Node>,
                inference: BayesInference = getCustomEliminationAsk()) {
        if(inference is CustomEliminationAsk && inference.inferenceMethod != STANDARD)
            throw IllegalArgumentException("The network can only go forward in time with an exact inference.")
        checkNodes(rootNodes)
        currentSlice = DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, *rootNodes)
        this.inference = inference
    }

    /**
     * Constructor for a newly generated network.
     * @param net The [DynamicBayesianNetwork] which will be evolved over time.
     * @param inference The [BayesInference] technique to be used to propagate in time.
     */
    constructor(net: DynamicBayesianNetwork, inference: BayesInference = getCustomEliminationAsk()){
        if(inference is CustomEliminationAsk && inference.inferenceMethod != STANDARD)
            throw IllegalArgumentException("The network can only go forward in time with an exact inference.")
        checkNodes(ArrayList<Node>().apply {
            for(rv in net.x_0) add(net.getNode(rv))
        }.toTypedArray())
        currentSlice = net
        this.inference = inference
    }

    private fun checkNodes(nodes: Array<Node>){
        for(node in nodes){
            if(node !is FullCPTNode) throw WrongDistributionException("Node $node is not a CPT node. Only FullCPTNodes are supported.")
            else if (node.children.isNotEmpty()) checkNodes(node.children.toTypedArray())
        }
    }


    /**
     * Allows to transition the network one slice ahead.
     * @param propositions The [Array]<[AssignmentProposition]> used to represent the observation of the evidence [RandomVariable]s.
     * @param verbose Allows to print logs while calculating. Default is `false`.
     */
    fun forward(propositions: Array<AssignmentProposition> = emptyArray(), verbose: Boolean = false){
        val nextBeliefs = HashMap<RandomVariable, Node>()
        val nextRootNodes = ArrayList<Node>()
        val newStateVariables = HashMap<RandomVariable, Node>()
        val X_1_to_X_2 = HashMap<RandomVariable, RandomVariable>()
        val newEvidences = HashMap<RandomVariable, Node>()

        // Questo ciclo crea i nuovi beliefs nodes calcolando
        // con il transition model e una ask sulle evidenze
        if(verbose) log("\nGenerating new beliefs nodes...")
        currentSlice.x_0_to_X_1.forEach { x0, x1 ->
            val newCPT = inference.ask(
                    arrayOf(x1), propositions, currentSlice
            )
            val node = FullCPTNode(x1, newCPT.values)
            nextBeliefs[x1] = node
            if(currentSlice.getNode(x0).parents.isEmpty()) nextRootNodes.add(nextBeliefs[x1]!!)
            if(verbose){
                log("$x1 ${Arrays.toString(newCPT.values)}")
            }
        }
        if(verbose) log("\nGenerating new state variables nodes...")
        // Questo ciclo crea i nodi per le variabili del nuovo stato tramite il
        //  transition model dai nodi precedenti
        for(oldRV in currentSlice.x_1){
            val parents = currentSlice.getNode(oldRV).parents
            val newParents = ArrayList<Node>()
            val newRv = oldRV.getNext()
            for(parent in parents) {
                val parentRV = parent.randomVariable
                newParents.add(nextBeliefs[currentSlice.x_0_to_X_1[parentRV]]!!)
            }
            val distribution = (currentSlice.getNode(oldRV).cpd as CPT).generateVector()
            newStateVariables[newRv] = FullCPTNode(newRv, distribution, *newParents.toTypedArray())
            X_1_to_X_2[oldRV] = newRv
            if (verbose){
                log("Old RV name: $oldRV\n" +
                        "New RV name: $newRv\n" +
                        "cpt: ${Arrays.toString(distribution)}")
            }
        }

        if(verbose) log("Generating new evidences nodes...")
        //Questo ciclo crea le nuove variabili di evidenza  tramite
        // iil modello sensoriale a partire dai nodice precedenti
        for(oldEvRv in currentSlice.e_1){
            val parents = currentSlice.getNode(oldEvRv).parents
            val newParents = ArrayList<Node>()
            val newEvRv = oldEvRv.getNext()
            for(parent in parents){
                val parentRv = parent.randomVariable
                newParents.add(newStateVariables[X_1_to_X_2[parentRv]]!!)
            }
            val distribution = (currentSlice.getNode(oldEvRv).cpd as CPT).generateVector()
            newEvidences[newEvRv] = FullCPTNode(newEvRv, distribution, *newParents.toTypedArray())
            if (verbose){
                log("Old RV name: $oldEvRv\n" +
                        "New RV name: $newEvRv\n" +
                        "cpt: ${Arrays.toString(distribution)}")
            }
        }

        currentSlice = DynamicBayesNet(currentSlice.priorNetwork, X_1_to_X_2, newEvidences.keys, *nextRootNodes.toTypedArray())
    }

    override fun getE_1() = currentSlice.e_1
    override fun getRandomVariable(node: Node?): RandomVariable {
        TODO("Not yet implemented")
    }

    override fun getX_1_VariablesInTopologicalOrder() = currentSlice.x_1_VariablesInTopologicalOrder

    override fun getX_1_to_X_0() = currentSlice.x_0_to_X_1

    override fun getNode(rv: RandomVariable?) = currentSlice.getNode(rv)

    override fun getPriorNetwork() = currentSlice.priorNetwork

    override fun getX_0() = currentSlice.x_0

    override fun getX_1() = currentSlice.x_1

    override fun getVariablesInTopologicalOrder() = currentSlice.variablesInTopologicalOrder

    override fun getX_0_to_X_1() = currentSlice.x_0_to_X_1
}
@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_VARIABLE")

package dynamic.probability.bayes

import aima.core.probability.RandomVariable
import aima.core.probability.bayes.BayesianNetwork
import aima.core.probability.bayes.Node
import aima.core.probability.bayes.impl.BayesNet
import aima.core.probability.bayes.impl.DynamicBayesNet
import aima.core.probability.bayes.impl.FullCPTNode
import aima.core.probability.domain.BooleanDomain
import aima.core.probability.domain.DiscreteDomain
import aima.core.probability.domain.FiniteDomain
import aima.core.probability.domain.FiniteIntegerDomain
import aima.core.probability.util.RandVar

/**
 * Example of [DynamicBayesNet]
 */
@Suppress("LocalVariableName")
object BayesNetsFactory {

    fun getComplexDynamicNetworkExample(): DynamicBayesNet {
        val r_0 = RandVar("R_0", BooleanDomain())
        val s_0 = RandVar("S_0", BooleanDomain())

        val r_1 = RandVar("R_1", BooleanDomain())
        val s_1 = RandVar("S_1", BooleanDomain())

        val e_1 = RandVar("E_1", BooleanDomain())

        val priorNetwork = BayesNet(
                FullCPTNode(r_0, doubleArrayOf(0.5, 0.5)),
                FullCPTNode(s_0, doubleArrayOf(0.5, 0.5))
        )

        val r_0_node = FullCPTNode(r_0, doubleArrayOf(0.5, 0.5))
        val s_0_node = FullCPTNode(s_0, doubleArrayOf(0.5, 0.5))

        val r_1_node = FullCPTNode(r_1, doubleArrayOf(
                0.1, 0.9,
                0.15, 0.85,
                0.7, 0.3,
                0.8, 0.2
        ), r_0_node, s_0_node)
        val s_1_node = FullCPTNode(s_1, doubleArrayOf(
                0.23, 0.77,
                0.4, 0.6,
                0.57, 0.43,
                0.29, 0.71
        ), r_0_node, s_0_node)
        val e_1_node = FullCPTNode(e_1, doubleArrayOf(
                0.93, 0.07,
                0.49, 0.51,
                0.87, 0.13,
                0.01, 0.99
        ), r_1_node, s_1_node)

        val rvMap = HashMap<RandomVariable, RandomVariable>().apply {
            this[r_0] = r_1
            this[s_0] = s_1
        }
        val evidences = HashSet<RandomVariable>().apply { add(e_1) }
        return DynamicBayesNet(priorNetwork, rvMap, evidences, r_0_node, s_0_node)
    }
    fun getSimpleDynamicNetworkExample(): DynamicBayesNet {
        val mare = RandVar("Mare", BooleanDomain())
        val sole = RandVar("Sole", BooleanDomain())

        val mare_domani = RandVar("MareDomani", BooleanDomain())
        val sole_domani = RandVar("SoleDomani", BooleanDomain())

        val bagno = RandVar("Bagno", BooleanDomain())

        val priorNetwork = BayesNet(
                FullCPTNode(mare, doubleArrayOf(0.5, 0.5)),
                FullCPTNode(sole, doubleArrayOf(0.5, 0.5))
        )

        val mare_node = FullCPTNode(mare, doubleArrayOf(0.5, 0.5))
        val sole_node = FullCPTNode(sole, doubleArrayOf(0.5, 0.5))

        val mare_domani_node = FullCPTNode(mare_domani, doubleArrayOf(
                0.9, 0.1,
                0.6, 0.4,
                0.2, 0.8,
                0.1, 0.9
        ), mare_node, sole_node)
        val sole_domani_node = FullCPTNode(sole_domani, doubleArrayOf(
                0.9, 0.1,
                0.6, 0.4,
                0.8, 0.2,
                0.2, 0.8
        ), mare_node, sole_node)
        val bagno_node = FullCPTNode(bagno, doubleArrayOf(
                0.93, 0.07,
                0.7, 0.3,
                0.3, 0.7,
                0.05, 0.95
        ), mare_domani_node, sole_domani_node)

        val rvMap = HashMap<RandomVariable, RandomVariable>().apply {
            this[mare] = mare_domani
            this[sole] = sole_domani
        }
        val evidences = HashSet<RandomVariable>().apply { add(bagno) }
        return DynamicBayesNet(priorNetwork, rvMap, evidences, mare_node, sole_node)
    }
    fun getBigDynamicNetworkExample(): DynamicBayesNet {
        val mare = RandVar("Mare", BooleanDomain())
        val sole = RandVar("Sole", BooleanDomain())
        val vento= RandVar("Vento", BooleanDomain())

        val mare_domani = RandVar("MareDomani", BooleanDomain())
        val sole_domani = RandVar("SoleDomani", BooleanDomain())
        val vento_domani = RandVar("VentoDomani", BooleanDomain())

        val bagno = RandVar("Bagno", BooleanDomain())
        val onde= RandVar("Onde", BooleanDomain())

        val priorNetwork = BayesNet(
                FullCPTNode(mare, doubleArrayOf(0.8, 0.2)),
                FullCPTNode(sole, doubleArrayOf(0.9, 0.1)),
                FullCPTNode(vento, doubleArrayOf(0.4, 0.6))
        )

        val mare_node = FullCPTNode(mare, doubleArrayOf(0.8, 0.2))
        val sole_node = FullCPTNode(sole, doubleArrayOf(0.9, 0.1))
        val vento_node = FullCPTNode(vento, doubleArrayOf(0.4, 0.6))

        val mare_domani_node = FullCPTNode(mare_domani, doubleArrayOf(
                0.9, 0.1,
                0.6, 0.4,
                0.2, 0.8,
                0.1, 0.9,
                0.8, 0.2,
                0.3, 0.7,
                0.5, 0.5,
                0.1, 0.9
        ), mare_node, sole_node,vento_node)
        val sole_domani_node = FullCPTNode(sole_domani, doubleArrayOf(
                0.9, 0.1,
                0.7, 0.3,
                0.8, 0.2,
                0.2, 0.8,
                0.7, 0.3,
                0.3, 0.7,
                0.5, 0.5,
                0.05, 0.95
        ), mare_node, sole_node,vento_node)
        val vento_domani_node = FullCPTNode(vento_domani, doubleArrayOf(
                0.8, 0.2,
                0.75, 0.25,
                0.75, 0.25,
                0.26, 0.74,
                0.71, 0.29,
                0.21, 0.79,
                0.57, 0.43,
                0.05, 0.95
        ),mare_node,  sole_node,vento_node)

        val bagno_node = FullCPTNode(bagno, doubleArrayOf(
                0.93, 0.07,
                0.7, 0.3,
                0.3, 0.7,
                0.05, 0.95,
                0.78,0.22,
                0.66, 0.34,
                0.32, 0.68,
                0.12,0.88
        ), mare_domani_node, sole_domani_node,vento_domani_node)
        val onde_node = FullCPTNode(onde, doubleArrayOf(
                0.7, 0.3,
                0.7, 0.3,
                0.3, 0.7,
                0.05, 0.95,
                0.78,0.22,
                0.66, 0.34,
                0.32, 0.68,
                0.12,0.88
        ), mare_domani_node, sole_domani_node,vento_domani_node)

        val rvMap = HashMap<RandomVariable, RandomVariable>().apply {
            this[mare] = mare_domani
            this[sole] = sole_domani
            this[vento]= vento_domani
        }
        val evidences = HashSet<RandomVariable>().apply { add(bagno)  }
        evidences.add(onde)
        return DynamicBayesNet(priorNetwork, rvMap, evidences, mare_node, sole_node,vento_node)
    }

}




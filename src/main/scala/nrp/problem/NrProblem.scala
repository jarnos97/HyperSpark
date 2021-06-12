package nrp.problem

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import it.polimi.hyperh.solution.EvaluatedSolution
import nrp.solution.NrSolution
import nrp.solution.NrEvaluatedSolution
import nrp.util.NrProblemParser

@SerialVersionUID(100L)
class NrProblem(val numCustomers: Int,
                val numLevels: Int,
                val customerWeights: Array[Double],
                val customerRequirements: Array[Array[Int]],
                val nodeCosts: Array[Double],
                val nodeParents: Array[Array[Int]]
               ) extends Problem {

  val initialSolution: Array[Int] = Array.fill(numCustomers)(0)

  def calculateWeights(customerIndices: List[Int]): Double = {customerIndices.map(customerWeights).sum}

  def findParents(requirements: List[Int], numLevels: Int): List[Int] = {
    var allRequirements: List[Int] = requirements
    var requirementsArray = requirements.toArray
    for (_ <- 2 to numLevels){  // for each level extract the parents of the requirements.
      val parents = requirementsArray.map(nodeParents).flatten.distinct
      allRequirements = allRequirements:::parents.toList
      requirementsArray = parents
    }
    allRequirements.distinct  // return unique items
  }

  def calculateCosts(customerIndices: List[Int]): Double = {
    // Gather all customer requirements of solution. Flatten and take distinct elements for union of requirements.
    val requirements = customerIndices.map(customerRequirements).flatten.distinct
    // Find the all parents for the customer requirements
    val allRequirements = findParents(requirements, numLevels)
    allRequirements.toArray.map(nodeCosts).sum
  }

  def calculateFitness(s: NrSolution): Int = {
    val solution = s.toList
    val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)
    val weightSum: Double = calculateWeights(customerIndices)
    val costSum: Double = calculateCosts(customerIndices)
    val fitness = (weightSum - costSum).toInt
    fitness
  }

  def evaluate(s: Solution): EvaluatedSolution = {
    val solution = s.asInstanceOf[NrSolution]
    val fitness = calculateFitness(solution)
    val evaluatedSolution = new NrEvaluatedSolution(fitness, solution)
    evaluatedSolution
  }
}

//Problem Factory
object NrProblem {
  // arg name - name of a resource in src/main/resources and src/test/resources
  def fromResources(name: String): NrProblem =  {
    NrProblemParser(name)
  }
}

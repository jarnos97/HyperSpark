package kpp.problem

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import it.polimi.hyperh.solution.EvaluatedSolution
import kpp.solution.KpSolution
import kpp.solution.KpEvaluatedSolution
import kpp.util.KpProblemParser

@SerialVersionUID(100L)
class KpProblem(val capacity: Int, val profits: Array[Int], val weights: Array[Int]) extends Problem {
  val initialSolution: Array[Int] = Array.fill(profits.length)(0)

  def calculateWeights(itemIndices: List[Int]): Int = {itemIndices.map(weights).sum}
  def calculateProfits(itemIndices: List[Int]): Int = {itemIndices.map(profits).sum}

  def evaluate(s: Solution): EvaluatedSolution = {
    val solution = s.asInstanceOf[KpSolution]
    val itemIndices = solution.toList.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)
    val fitness = calculateProfits(itemIndices)
    val evaluatedSolution = new KpEvaluatedSolution(fitness, solution)
    evaluatedSolution
  }
}

// Problem Factory
object KpProblem{
  // arg name - name of a resource in src/main/resources and src/test/resources
  def fromResources(fileName: String): KpProblem = {
    KpProblemParser(fileName)
  }
}
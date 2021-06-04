package kp.problem

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import it.polimi.hyperh.solution.EvaluatedSolution
import kp.solution.KpSolution
import kp.solution.KpEvaluatedSolution
import kp.util.KpProblemParser

class KpProblem(val capacity: Int, val profits: Array[Int], val weights: Array[Int]) extends Problem {
  val initialSolution: Array[Int] = Array.fill(profits.length)(0)

  def calculateWeights(customerIndices: List[Int]): Int = {customerIndices.map(weights).sum}
  def calculateProfits(customerIndices: List[Int]): Int = {customerIndices.map(profits).sum}

  def calculateFitness(s: KpSolution): Int = {
    val solution = s.toList
    val itemIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)
    val weightSum: Int = calculateWeights(itemIndices)
    val profitSum: Int = calculateProfits(itemIndices)
    val fitness = profitSum - weightSum
    fitness
  }

  def evaluate(s: Solution): EvaluatedSolution = {
    val solution = s.asInstanceOf[KpSolution]
    val fitness = calculateFitness(solution)
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
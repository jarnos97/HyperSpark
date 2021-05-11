package nrp.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.algorithms.Algorithm
import it.polimi.hyperh.spark.StoppingCondition
import it.polimi.hyperh.spark.TimeExpired
import nrp.problem.NrProblem
import nrp.solution.NrSolution
import nrp.solution.NrEvaluatedSolution
import scala.util.Random
import scala.annotation.tailrec
import nrp.util.Moves


class SAAlgorithm(p: NrProblem, initialTemp: Double, timeLimit: Int, beta: Double,
                  minTemperature: Double, seed: NrSolution, bound: Double) extends Algorithm {
  // Set variables
  var t: Double = initialTemp  // set temperature t to initial temperature
  val initialSolution: List[Int] = seed.toList

  override def evaluate(problem: Problem): EvaluatedSolution = {
    val p = problem.asInstanceOf[NrProblem]
    val stopCond = new TimeExpired(timeLimit)
    evaluate(p, stopCond)
  }

//  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = {
  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = {
    val random = new Random
    val p = problem.asInstanceOf[NrProblem]

    def cost(solution: List[Int]): Double = {p.calculateCosts(solution)}
    def fitness(solution: List[Int]): NrEvaluatedSolution = {p.evaluate(NrSolution(solution)).asInstanceOf[NrEvaluatedSolution]}

    def checkConstraint(solution: List[Int]): Boolean = {
      if (cost(solution) <= bound)
        true
      else false
    }

    @tailrec
    def validMove(solution: List[Int]): List[Int] = {
      // create new solution randomly
      var newSolution = List[Int]()  // empty list
      val functionInt = random.nextInt(List(1, 2, 3).length)  // produces random number 0, 1, or 2
      if (functionInt == 0) newSolution = Moves(random).removeCustomer(solution)
      if (functionInt == 1) newSolution = Moves(random).addCustomer(solution)
      if (functionInt == 2) newSolution = Moves(random).swapCustomers(solution)
      // Check new solution
      if (checkConstraint(newSolution)) {
        newSolution // return the new solution
      } else validMove(solution)  // create new solution if constraint is not satisfied
    }

    def acceptanceProbability(benefit: Double, temperature: Double): Double = scala.math.exp(benefit/temperature)

    val stop = stopCond.asInstanceOf[TimeExpired].initialiseLimit()
    def loop(old: NrEvaluatedSolution, temp: Double, iter: Int): NrEvaluatedSolution = {
      if ((temp > minTemperature)  && stop.isNotSatisfied()) {
        println("Do the things")
      }
    println("Test")
    }

    // This is just to return the right type for now
    validMove(initialSolution)
    fitness(initialSolution)
  }
}
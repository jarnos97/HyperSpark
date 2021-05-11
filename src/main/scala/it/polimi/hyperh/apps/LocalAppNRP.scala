package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.spark.{Framework, FrameworkConf, TimeExpired}
import nrp.algorithms.SAAlgorithm
import nrp.problem.NrProblem
import nrp.solution.NrSolution
import pfsp.neighbourhood.NeighbourhoodOperator
import util.Random

import scala.io.Source

object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
    val initialSolution = NrSolution.fromFile(fileName = "initial_solution.txt")
//    println(initialSolution)
//    val evaluatedSolution = problem.evaluate(s = initialSolution)
//    println(evaluatedSolution)

    val algo = new SAAlgorithm(p = problem, initialTemp = 100.0, timeLimit = 10000, beta = 0.0005,
      minTemperature = 0.005, seed = initialSolution, bound = 370.0)
    val algoResult = algo.evaluate(problem)
    println(algoResult)
  }
}


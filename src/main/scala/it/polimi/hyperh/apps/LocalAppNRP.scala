package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.spark.{Framework, FrameworkConf, TimeExpired}
import nrp.problem.NrProblem
import nrp.solution.NrSolution

import scala.io.Source

object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
    val initialSolution = NrSolution.fromFile(fileName = "initial_solution.txt")
//    println(initialSolution)
    val evaluatedSolution = problem.evaluate(s = initialSolution)
    println(evaluatedSolution)
  }
}


package nrp.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.algorithms.Algorithm
import it.polimi.hyperh.spark.StoppingCondition
import it.polimi.hyperh.spark.TimeExpired
//import pfsp.neighbourhood.NeighbourhoodOperator
import pfsp.problem.PfsProblem
import pfsp.solution.PfsEvaluatedSolution
import pfsp.solution.PfsSolution
//import pfsp.solution.NaivePfsEvaluatedSolution

import nrp.problem.NrProblem
import nrp.solution.NrSolution
import nrp.solution.NrEvaluatedSolution

class SAAlgorithm(p: NrProblem, initialTemp: Double, beta: Double, seed: Option[NrSolution]) extends Algorithm {
  var t: Double = initialTemp  // set temperature t to initial temperature


  override def evaluate(problem: Problem): EvaluatedSolution = {
    val p = problem.asInstanceOf[NrProblem]
    val t
  }

  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = ???
}
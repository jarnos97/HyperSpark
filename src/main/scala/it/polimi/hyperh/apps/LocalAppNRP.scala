package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.spark.Framework
import it.polimi.hyperh.spark.FrameworkConf
import it.polimi.hyperh.spark.TimeExpired
import nrp.problem.NrProblem
import nrp.algorithms.SAAlgorithm
import pfsp.algorithms.IGAlgorithm
import nrp.solution.{NrEvaluatedSolution, NrSolution}

/**
 * @author Jarno
 */
object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
    val initialSolution = NrEvaluatedSolution.fromResources(name = "NRP1InitialSolutionEvaluated.txt")
    println(initialSolution)
    val algo = new SAAlgorithm()
//    val algo = new IGAlgorithm()  // does work
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(200000)
    val randomSeed = 1255846

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNInitialSeeds(NrEvaluatedSolution.fromResources("NRP1.txt"), numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)
      .setNumberOfIterations(1)
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)
    println(solution)
  }
}


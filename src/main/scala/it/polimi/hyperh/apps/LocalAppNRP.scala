package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.Framework
import it.polimi.hyperh.spark.FrameworkConf
import it.polimi.hyperh.spark.TimeExpired
import nrp.problem.NrProblem
import nrp.algorithms.SAAlgorithm

import scala.language.postfixOps


/**
 * @author Jarno
 */
object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
//    val initialSolution = NrEvaluatedSolution.fromResources(name = "NRP1InitialSolutionEvaluated.txt")
    val algo = new SAAlgorithm(initT = 100.0, minT = 0.001, b = 0.0000005, boundB = 0.3, timeL = 200000)
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(200000)
    val randomSeed = 1255846

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
      .setNDefaultInitialSeeds(numOfAlgorithms)
      .setNumberOfIterations(1)
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)
    println(solution)
  }
}


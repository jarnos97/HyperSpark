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
    val algo = () => new SAAlgorithm(initT = 100.0, minT = 0.001, b = 0.0000005, boundB = 370.0)
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(900000)  // 15 minutes
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
      .setNumberOfIterations(1)  // what does this do?
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)
    println(solution)
  }
}


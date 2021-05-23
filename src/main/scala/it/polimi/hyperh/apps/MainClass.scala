package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.{Framework, FrameworkConf, SameSeeds, TimeExpired}
import nrp.problem.NrProblem
import nrp.algorithms.SAAlgorithm
import scala.language.postfixOps
import java.io._


/**
 * @author Jarno
 */
object MainClass {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
    val algo = () => new SAAlgorithm(initT = 100.0, minT = 0.001, b = 0.0000005, totalCosts = 820,
      boundPercentage = 0.3)
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(600000)  //  300000 = 5 minutes
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setDeploymentYarnCluster()
      .setRandomSeed(randomSeed)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
      .setNumberOfIterations(1)
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)

    // Print solution to console
    println(solution)
  }
}


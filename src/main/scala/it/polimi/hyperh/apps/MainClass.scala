package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.{Framework, FrameworkConf, SameSeeds, TimeExpired}
import kp.problem.KpProblem
import it.polimi.hyperh.spark.MapReduceHandlerMaximization
import kp.algorithms.SAAlgorithm
import java.io._


/**
 * @author Jarno
 */
object MainClass {
  def main(args: Array[String]): Unit = {
    val problem = KpProblem.fromResources(fileName = "KP_500_100000")
    val algo = new SAAlgorithm(initT = 100.0, minT = 0.01, b = 0.0000005)
    val numOfAlgorithms = 64
    val stopCond = new TimeExpired(120000)  //  300000 = 5 minutes
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setProblem(problem)
      .setRandomSeed(randomSeed)
      .setNumberOfIterations(10)
      .setStoppingCondition(stopCond)
      .setSeedingStrategy(new SameSeeds())
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
      .setMapReduceHandler(new MapReduceHandlerMaximization())

    val solution = Framework.run(conf)
    println(solution)
  }
}


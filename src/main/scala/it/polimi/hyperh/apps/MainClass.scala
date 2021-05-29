package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.{Framework, FrameworkConf, SameSeeds, TimeExpired}
import nrp.problem.NrProblem
import nrp.algorithms.SAAlgorithm
import it.polimi.hyperh.spark.MapReduceHandlerMaximization

/**
 * @author Jarno
 */
object MainClass {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
    val algo = new SAAlgorithm(initT = 100.0, minT = 0.001, b = 0.0000001, totalCosts = 820, boundPercentage = 0.3)
    val numOfAlgorithms = 64
    val stopCond = new TimeExpired(30000)  //  300000 = 5 minutes
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
      .setMapReduceHandler(new MapReduceHandlerMaximization())
      .setNumberOfIterations(10)
      .setStoppingCondition(stopCond)
      .setSeedingStrategy(new SameSeeds())

    val solution = Framework.run(conf)
    println(solution)
  }
}


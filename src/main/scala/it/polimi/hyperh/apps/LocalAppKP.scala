package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.{Framework, FrameworkConf, SameSeeds, TimeExpired}
import kpp.problem.KpProblem
import it.polimi.hyperh.spark.MapReduceHandlerMaximization
import kpp.algorithms.SAAlgorithm
import java.io._

/**
 * @author Jarno
 */
object LocalAppKP{
  def main(args: Array[String]): Unit = {
    // start timer
    val t1 = System.nanoTime

    val problem = KpProblem.fromResources(name = "KP_500_100000.txt")
    println(problem)
    val algo = new SAAlgorithm(initT= 100.0, minT = 0.01, b = 0.0000005)
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(60000)
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)
      .setSeedingStrategy(new SameSeeds())
      .setNumberOfIterations(5)
      .setMapReduceHandler(new MapReduceHandlerMaximization())
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)

    // Write solutions to file
    val fw = new FileWriter("src/main/resources/bestFoundSolutionsKP.txt.", true)
    try {
      fw.write("KP_500_100000  b=0.0000005 ")
      fw.write(solution.toString + "\n")
    }
    finally fw.close()

    // stop timer
    val duration = (System.nanoTime - t1) / 1e9d

    // Print solution to console
    println(solution)
    println("Total execution time:" + duration)
  }
}

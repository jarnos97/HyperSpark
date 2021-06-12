package it.polimi.hyperh.apps

import it.polimi.hyperh.spark.{Framework, FrameworkConf, SameSeeds, TimeExpired}
import nrp.problem.NrProblem
import nrp.algorithms.SAAlgorithm
import it.polimi.hyperh.spark.MapReduceHandlerMaximization
import java.io._

/**
 * @author Jarno
 */
object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    // start timer
    val t1 = System.nanoTime

    val problem =  NrProblem.fromResources(name = "NRP1")
    val algo = new SAAlgorithm(initT = 100.0, minT = 0.01, b = 0.0000005, totalCosts = 820, boundPercentage = 0.3)
    val numOfAlgorithms = 4
    val stopCond = new TimeExpired(60000)  //  300000 5 minutes
    val randomSeed = 118337975

    val conf = new FrameworkConf()
      .setRandomSeed(randomSeed)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(algo, numOfAlgorithms)
      .setNDefaultInitialSeeds(numOfAlgorithms)  // no initial seed
//      .setSeedingStrategy(new SameSeeds())
      .setNumberOfIterations(1)  // what does this do? --> Cooperation
      .setMapReduceHandler(new MapReduceHandlerMaximization())  // for maximization!
      .setStoppingCondition(stopCond)

    val solution = Framework.run(conf)

    // Write solutions to file
    val fw = new FileWriter("src/main/resources/bestFoundSolutionsNRP.txt.", true)
    try {
      fw.write("NRP1 bound=0.3  b=0.0000005 ")
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


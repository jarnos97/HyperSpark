package it.polimi.hyperh.apps

import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.spark.Framework
import it.polimi.hyperh.spark.FrameworkConf
import java.io._

import it.polimi.hyperh.spark.TimeExpired
import pfsp.problem.PfsProblem
import pfsp.algorithms.GAAlgorithm
import pfsp.algorithms.SAAlgorithm

object LocalApp2{
  def main(args: Array[String]) {

    val problem = PfsProblem.fromResources(name="inst_ta054.txt")
    val algGAAlgorithm =  new GAAlgorithm()
    val algSAAlgorithm =  new SAAlgorithm(problem)
    val conf = new FrameworkConf()
      .setProblem(problem)
      .setAlgorithms(Array(algGAAlgorithm, algSAAlgorithm))
      .setStoppingCondition(new TimeExpired(1000))
      .setDeploymentLocalNumExecutors(2)
      .setNumberOfIterations(1)
      .setNDefaultInitialSeeds(2)
      .setRandomSeed(118337975)

    val solution: EvaluatedSolution = Framework.run(conf)

    // Write solutions to file
    val fw = new FileWriter("solution.txt.")
    try {
      fw.write(solution.toString)
    }
    finally fw.close()

    println(solution)
  }
}

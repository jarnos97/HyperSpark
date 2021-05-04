package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.spark.Framework
import it.polimi.hyperh.spark.FrameworkConf
import it.polimi.hyperh.spark.TimeExpired
import pfsp.problem.PfsProblem
import pfsp.algorithms.IGAlgorithm
import pfsp.algorithms.GAAlgorithm

object LocalApp2 {
  def main(args: Array[String]) {
    val problem = PfsProblem.fromResources(name="inst_ta054.txt")
    val makeAlgo = () => new GAAlgorithm()
    val numOfAlgorithms = 4

    val conf = new FrameworkConf()
      .setRandomSeed(118337975)
      .setDeploymentLocalNumExecutors(numOfAlgorithms)
      .setProblem(problem)
      .setNAlgorithms(makeAlgo, numOfAlgorithms)
      .setNumberOfIterations(1)
      .setStoppingCondition(new TimeExpired(problem.getExecutionTime() / numOfAlgorithms))

    val solution: EvaluatedSolution = Framework.run(conf)
    println(solution)
  }
}

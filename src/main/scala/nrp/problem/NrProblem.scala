package nrp.problem

import Array._
import scala.io.Source
import java.io.InputStream
import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.{EvaluatedSolution, Solution}
//import it.polimi.hyperh.solution.{EvaluatedSolution, Solution}
import it.polimi.hyperh.solution.EvaluatedSolution
//import nrp.solution.{NrEvaluatedSolution, NrSolution}
import nrp.util.NrProblemParser

@SerialVersionUID(100L)
class NrProblem(val numCustomers: Int,
                val customerWeights: Array[Double]){  //Array[Array[Int]]
  // val nCustomers: Int
  //  val customerWeights: Array[Float],
  //  val customerRequirements: Array[Int],  //  Array[Array[Int]]
  //  val nodeCosts: Array[Int],
  //  val nodeParents: Array[Int]
  // def evaluatePartialSolution(solution: NrSolution): EvaluatedSolution = {
  //   val results = new NrEvaluatedSolution()
  // }

//  def evaluate(s: Solution): EvaluatedSolution = {
//    val solution = s.asInstanceOf[NrSolution]
//    evaluatePartialSolution(solution)
//  }
  println("Hello, World!")
}

//Problem Factory
object NrProblem {
  /**
   * @arg path - path to a file
   */
//  def fromFile(path: String): NrProblem = NrProblemParser(Source.fromFile(path).getLines().mkString(" x ") + " x ").getOrElse(throw new RuntimeException("ParserError"))
  /**
   * @arg name - name of a resource in src/main/resources and src/test/resources
   */
  def fromResources(fileName: String): Array[Array[Int]] =  {  //return type not necessary later (?)
    NrProblemParser(fileName)
  }

}

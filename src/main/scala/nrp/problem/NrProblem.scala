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
                val customerWeights: Array[Double],
                val customerRequirements: Array[Array[Int]],
                val nodeCosts: Array[Double],
                val nodeParents: Array[Array[Int]]
               ){
  // def evaluatePartialSolution(solution: NrSolution): EvaluatedSolution = {
  //   val results = new NrEvaluatedSolution()
  // }

//  def evaluate(s: Solution): EvaluatedSolution = {
//    val solution = s.asInstanceOf[NrSolution]
//    evaluatePartialSolution(solution)
//  }
  println("Hello, World!")
  println(numCustomers)
  println(customerWeights.mkString("Array(", ", ", ")"))
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
  def fromResources(fileName: String): NrProblem =  {  //return type not necessary later (?)
    NrProblemParser(fileName)
  }

}

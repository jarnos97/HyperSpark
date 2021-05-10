package nrp.solution

import it.polimi.hyperh.solution.EvaluatedSolution
import nrp.problem.NrProblem
import nrp.solution.NrSolution

import scala.io.Source
import java.io.InputStream


class NrEvaluatedSolution(override val value: Int, override val solution: NrSolution) extends EvaluatedSolution(value, solution){
  //Alternative constructor
  def this(value: Int, s: Array[Int]) = this(value, NrSolution(s))
  override def toString = {
    val solutionString = solution.asString()
    val str = "NrEvaluatedSolution(value:" + value + ", solution:" + solutionString + ")"
    str
  }
  def compare(that: EvaluatedSolution) = this.value - that.asInstanceOf[NrEvaluatedSolution].value
  def compare(that: NrEvaluatedSolution) = this.value - that.value
  def requirements = solution.solution
}

object NrEvaluatedSolution{
  //  def fromFile(path:String) = NrSolutionParser.apply(Source.fromFile(path).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
  def apply(value: Int, solution: Array[Int]) = new NrEvaluatedSolution(value, solution)
  def apply(value: Int, solution: List[Int]) = new NrEvaluatedSolution(value, solution.toArray)  // why this second part?
}

//object PfsEvaluatedSolution {
//  /**
//   * @arg path - name of a file
//   */
////  def fromFile(path: String) = PfsEvaluatedSolutionParser.apply(Source.fromFile(path).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
//  /**
//   * @arg name - name of a resource in src/main/resources and src/test/resources
//   */
////  def fromResources(name: String) = {
////    val stream: InputStream = getClass.getResourceAsStream("/" + name)
////    PfsEvaluatedSolutionParser(Source.fromInputStream(stream).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
////  }
//}
//object NaivePfsEvaluatedSolution {
//  def apply(problem: NrProblem) = new NrEvaluatedSolution(999999999, problem.jobs)
//}
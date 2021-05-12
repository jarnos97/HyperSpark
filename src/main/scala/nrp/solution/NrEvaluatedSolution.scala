package nrp.solution

import it.polimi.hyperh.solution.EvaluatedSolution
import nrp.problem.NrProblem
import nrp.util.NrEvaluatedSolutionParser
import scala.io.Source
import java.io.InputStream

class NrEvaluatedSolution(override val value: Int, override val solution: NrSolution) extends EvaluatedSolution(value, solution){
  //Alternative constructor
  def this(value: Int, s: Array[Int]) = this(value, NrSolution(s))
  override def toString: String = {
    val solutionString = solution.asString()
    val str = "NrEvaluatedSolution(value:" + value + ", solution:" + solutionString + ")"
    str
  }
  def compare(that: EvaluatedSolution): Int = this.value - that.asInstanceOf[NrEvaluatedSolution].value
  def compare(that: NrEvaluatedSolution): Int = this.value - that.value
  def requirements: Array[Int] = solution.solution
}

object NrEvaluatedSolution{
  def fromResources(name: String): NrEvaluatedSolution = {
    val stream: InputStream = getClass.getResourceAsStream("/" + name)
    NrEvaluatedSolutionParser(Source.fromInputStream(stream).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
  }
  def apply(value: Int, solution: Array[Int]) = new NrEvaluatedSolution(value, solution)
  def apply(value: Int, solution: List[Int]) = new NrEvaluatedSolution(value, solution.toArray)  // why this second part?
}

object NaiveNrEvaluatedSolution {
  def apply(problem: NrProblem) = new NrEvaluatedSolution(value = 1, problem.initialSolution)
}

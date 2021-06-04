package kp.solution

import it.polimi.hyperh.solution.EvaluatedSolution
import kp.problem.KpProblem
import kp.util.KpEvaluatedSolutionParser
import scala.io.Source
import java.io.InputStream

class KpEvaluatedSolution(override val value: Int, override val solution: KpSolution)
  extends EvaluatedSolution(value, solution){
  // Alternative constructor
  def this(value: Int, s: Array[Int]) = this(value, KpSolution(s))
  override def toString: String = {
    val solutionString = solution.asString()
    val str = "KpEvaluatedSolution(value:" + value + ", solution:" + solutionString + ")"
    str
  }
  def compare(that: EvaluatedSolution): Int = this.value - that.asInstanceOf[KpEvaluatedSolution].value
  def compare(that: KpEvaluatedSolution): Int = this.value - that.value
  def requirements: Array[Int] = solution.solution
}

object KpEvaluatedSolution{
  def fromResources(name: String): KpEvaluatedSolution = {
    val stream: InputStream = getClass.getResourceAsStream("/" + name)
    KpEvaluatedSolutionParser(Source.fromInputStream(stream).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
  }
  def apply(value: Int, solution: Array[Int]) = new KpEvaluatedSolution(value, solution)
  def apply(value: Int, solution: List[Int]) = new KpEvaluatedSolution(value, solution.toArray)
}

object NaiveKpEvaluatedSolution {
  def apply(problem: KpProblem) = new KpEvaluatedSolution(value = 1, problem.initialSolution)
}
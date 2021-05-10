package nrp.solution

import it.polimi.hyperh.solution.Solution
import scala.io.Source
import nrp.util.NrSolutionParser


/**
 * A solution is represented as a set of customers, whose requirements are fulfilled.
 * s = [0, 1, 0, 0, 1, 0, 1, 0, 1]
 */
class NrSolution (val solution: Array[Int]) extends Solution {
  // representation should be an array of booleans. For each customer.
  // 1 if their requirements are met, 0 otherwise

  def asString() = "Array(" + solution.mkString(", ")+")"
  override def toString = {
    val requirementsString = asString()
    val str = "NrSolution(Customer requirements fulfilled:" + requirementsString+")"
    str
  }
  def toList = solution.toList
}


object NrSolution{
  def fromFile(fileName: String): NrSolution =  {  //return type not necessary later (?)
    NrSolutionParser(fileName)}
//  def fromFile(path:String) = NrSolutionParser.apply(Source.fromFile(path).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
  def apply(solution: Array[Int]) = new NrSolution(solution)
  def apply(solution: List[Int]) = new NrSolution(solution.toArray)  // why this second part?
}





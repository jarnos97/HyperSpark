package nrp.solution

import it.polimi.hyperh.solution.Solution
import nrp.util.NrSolutionParser


/**
 * A solution is represented as a set of customers, whose requirements are fulfilled.
 * s = [0, 1, 0, 0, 1, 0, 1, 0, 1]
 */
class NrSolution(val solution: Array[Int]) extends Solution {
  def asString(): String = "Array(" + solution.mkString(", ")+")"
  override def toString: String = {
    val requirementsString = asString()
    val str = "NrSolution(Customer requirements fulfilled:" + requirementsString+")"
    str
  }
  def toList: List[Int] = solution.toList
}

object NrSolution{
  def fromResources(fileName: String): NrSolution =  NrSolutionParser(fileName)
  def apply(solution: Array[Int]) = new NrSolution(solution)
  def apply(solution: List[Int]) = new NrSolution(solution.toArray)
}





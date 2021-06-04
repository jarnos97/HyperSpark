package kp.solution

import it.polimi.hyperh.solution.Solution
import kp.util.KpSolutionParser

/**
 * A solution is represented as a set of items that are included in the knapsack.
 * s = [0, 1, 0, 0, 1, 0, 1, 0, 1]
 */

class KpSolution(val solution: Array[Int]) extends Solution {
  def asString(): String = "Array(" + solution.mkString(", ")+")"
  override def toString: String = {
    val requirementsString = asString()
    val str = "KpSolution(Items included in knapsack:" + requirementsString+")"
    str
  }
  def toList: List[Int] = solution.toList
}

object KpSolution{
  def fromResources(fileName: String): KpSolution =  KpSolutionParser(fileName)
  def apply(solution: Array[Int]) = new KpSolution(solution)
  def apply(solution: List[Int]) = new KpSolution(solution.toArray)
}
//package nrp.solution
//
//import it.polimi.hyperh.solution.Solution
//import scala.io.Source
//import nrp.util.NrSolutionParser
//
//class NrSolution (val customerRequirements: Array[Boolean]) extends Solution {
//  // representation should be an array of booleans. For each customer.
//  // 1 if their requirements are met, 0 otherwise
//
//  def asString() = "Array(" + customerRequirements.mkString(", ")+")"
//  override def toString = {
//    val requirementsString = asString()
//    val str = "NrSolution(Customer requirements:" + requirementsString+")"
//    str
//  }
//  def toList = customerRequirements.toList
//}
//
//
//object NrSolution{
//  def fromFile(path:String) = NrSolutionParser.apply(Source.fromFile(path).getLines().mkString).getOrElse(throw new RuntimeException("ParserError"))
//  def apply(customerRequirements: Array[Boolean]) = new NrSolution(customerRequirements)
////  def apply(customerRequirements: Array[Boolean]) = new NrSolution(customerRequirements.toArray)
//}
//
//

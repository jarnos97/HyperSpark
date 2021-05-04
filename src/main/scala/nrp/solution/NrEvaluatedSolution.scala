//package nrp.solution
//
//import it.polimi.hyperh.solution.EvaluatedSolution
//import nrp.problem.NrProblem
////import nrp.solution.NrSolution
//
//import scala.io.Source
//import java.io.InputStream
//
//
//class NrEvaluatedSolution(override val value: Float, override val solution: NrSolution) extends EvaluatedSolution(value, solution){
//  //Alternative constructor
//  def this(value: Float, customerRequirements: Array[Boolean]) = this(value, NrSolution(customerRequirements))
//  override def toString = {
//    val requirementsString = solution.asString()
//    val str = "NrEvaluatedSolution(value:" + value + ", solution:" + requirementsString + ")"
//    str
//  }
//  def compare(that: EvaluatedSolution) = this.value - that.asInstanceOf[NrEvaluatedSolution].value
//  def compare(that: NrEvaluatedSolution) = this.value - that.value
//  def requirements = solution.customerRequirements
//}

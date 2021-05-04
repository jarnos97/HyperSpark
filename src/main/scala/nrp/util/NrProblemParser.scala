package nrp.util

import scala.util.parsing.combinator.RegexParsers
import nrp.problem.NrProblem
import scala.io.Source

object NrProblemParser{
  def apply(fileName: String): Array[Array[Int]] = {
    val path = "src/main/resources/"
    // Number of customers
    val numCustomersFile = path + fileName + "numCustomers.txt"
    val numCustomersString = Source.fromFile(numCustomersFile).mkString
    val numCustomers = numCustomersString.toInt
    // Customer weights
    val customerWeightsFile = path + fileName + "CustomerWeights.txt"
    val customerWeightsCharacters = Source.fromFile(customerWeightsFile).getLines.toArray
    val customerWeights = customerWeightsCharacters.map(x => x.toDouble)
    // Customer requirements
    val customerRequirementsFile = path + fileName + "CustomerRequirements.txt"
    println(customerRequirementsFile)
    val customerRequirements = Source.fromFile(customerRequirementsFile).getLines().map(_.split(",").map(_.trim.toInt).toArray).toArray
//    val customerRequirementsCharacters = Source.fromFile(customerRequirementsFile).getLines.toArray
//    val customerRequirements = customerRequirementsCharacters.map(x => x.toDouble)
    // Costs per node
    // Parents per node
    return customerRequirements
  }
}
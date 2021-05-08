package nrp.util

import scala.io.Source
import nrp.problem.NrProblem


object NrProblemParser{
  def apply(fileName: String): NrProblem = {
    val path = "src/main/resources/"
    // Number of customers
    val numCustFile = path + fileName + "NumCustomers.txt"
    val numCustomersString = Source.fromFile(numCustFile).mkString
    val numCustomers = numCustomersString.toInt
    // Customer weights
    val custWFile = path + fileName + "CustomerWeights.txt"
    val customerWeightsCharacters = Source.fromFile(custWFile).getLines.toArray
    val customerWeights = customerWeightsCharacters.map(x => x.toDouble)
    // Customer requirements
    val custRFile = path + fileName + "CustomerRequirements.txt"
    val customerRequirements = Source.fromFile(custRFile).getLines().map(_.split(",").map(_.trim.toInt).toArray).toArray
    // Costs per node
    val nodeCFile = path + fileName + "NodeCosts.txt"
    val nodeCostsCharacter = Source.fromFile(nodeCFile).getLines().toArray
    val nodeCosts = nodeCostsCharacter.map(x => x.toDouble)
    // Parents per node
    val nodePFile = path + fileName + "NodeParents.txt"
    val nodeParents = Source.fromFile(nodePFile).getLines().map(_.split(",").map(_.trim.toInt).toArray).toArray

    new NrProblem(numCustomers, customerWeights, customerRequirements, nodeCosts, nodeParents)
  }
}
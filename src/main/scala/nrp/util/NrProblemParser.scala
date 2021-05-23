package nrp.util

import scala.io.Source
import nrp.problem.NrProblem


object NrProblemParser{
  def apply(fileName: String): NrProblem = {
    val path = "src/main/resources/"
    // Number of customers
    val parametersFile = path + fileName + "Parameters.txt"
    val parametersString = Source.fromFile(parametersFile).getLines.toArray
    val parameters = parametersString.map(x => x.toInt)
    val numCustomers = parameters(0)
    val numLevels = parameters(1)
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

    // New problem instance
    new NrProblem(numCustomers, numLevels, customerWeights, customerRequirements, nodeCosts, nodeParents)
  }
}

object AlternativeNrProblemParser{  // todo: remove
  def apply(fileName: String): NrProblem = {
    // Number of customers
//    val parametersFile = fileName + "Parameters.txt"
    val parametersFile = "NRP1Parameters.txt"
    val parametersString = Source.fromFile(parametersFile).getLines.toArray
    val parameters = parametersString.map(x => x.toInt)
    val numCustomers = parameters(0)
    val numLevels = parameters(1)
    // Customer weights
    val custWFile = fileName + "CustomerWeights.txt"
    val customerWeightsCharacters = Source.fromFile(custWFile).getLines.toArray
    val customerWeights = customerWeightsCharacters.map(x => x.toDouble)
    // Customer requirements
    val custRFile = fileName + "CustomerRequirements.txt"
    val customerRequirements = Source.fromFile(custRFile).getLines().map(_.split(",").map(_.trim.toInt).toArray).toArray
    // Costs per node
    val nodeCFile = fileName + "NodeCosts.txt"
    val nodeCostsCharacter = Source.fromFile(nodeCFile).getLines().toArray
    val nodeCosts = nodeCostsCharacter.map(x => x.toDouble)
    // Parents per node
    val nodePFile = fileName + "NodeParents.txt"
    val nodeParents = Source.fromFile(nodePFile).getLines().map(_.split(",").map(_.trim.toInt).toArray).toArray

    // New problem instance
    new NrProblem(numCustomers, numLevels, customerWeights, customerRequirements, nodeCosts, nodeParents)
  }
}
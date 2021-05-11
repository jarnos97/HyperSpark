package nrp.util

import pfsp.neighbourhood.NeighbourhoodOperator

import scala.util.Random

class Moves(random: Random){
  def removeCustomer(solution: List[Int]): List[Int] = {
    val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)  // indices with value = 1
    var customer = random.nextInt(customerIndices.length)  // pick random customer to remove
    customer = customerIndices(customer)
    solution.updated(customer, 0)  // return updated solution
  }

  def addCustomer(solution: List[Int]): List[Int] = {
    val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 0).map(pair => pair._2)  // indices with value = 0
    var customer = random.nextInt(customerIndices.length)  // pick random customer to remove
    customer = customerIndices(customer)
    solution.updated(customer, 1)  // return updated solution
  }

  def swapCustomers(solution: List[Int]): List[Int] =  {
    var newSol = removeCustomer(solution)
    addCustomer(newSol)
  }
}

object Moves {
  def apply(random: Random): Moves = {
    new Moves(random)
  }
}
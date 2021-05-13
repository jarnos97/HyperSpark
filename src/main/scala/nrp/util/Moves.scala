package nrp.util

import scala.util.Random

class Moves(random: Random){
  def removeCustomer(solution: List[Int]): List[Int] = {
    val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)  // indices with value = 1
    if (customerIndices.nonEmpty) {
      var customer = random.nextInt(customerIndices.length) // pick random customer to remove
      customer = customerIndices(customer)
      solution.updated(customer, 0) // return updated solution
    } else throw new RuntimeException("@removeCustomer: the list of customers with value 1 is empty.")
  }

  def addCustomer(solution: List[Int]): List[Int] = {
    val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 0).map(pair => pair._2)  // indices with value = 0
    if (customerIndices.nonEmpty){
      var customer = random.nextInt(customerIndices.length)
      customer = customerIndices(customer)
      solution.updated(customer, 1)  // return updated solution
    } else throw new RuntimeException("@addCustomer: the list of customers with value 0 is empty.")
  }

  def swapCustomers(solution: List[Int]): List[Int] =  {
    val newSol = removeCustomer(solution)
    addCustomer(newSol)
  }
}

object Moves {
  def apply(random: Random): Moves = {
    new Moves(random)
  }
}
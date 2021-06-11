package kpp.util

import scala.util.Random

class Moves(random: Random){
  def removeItem(solution: List[Int]): List[Int] = {
    val itemIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)  // indices with value = 1
    if (itemIndices.nonEmpty) {
      var item = random.nextInt(itemIndices.length) // pick random item to remove
      item = itemIndices(item)
      solution.updated(item, 0)  // returned updated solution
    } else throw new RuntimeException("@removeItem: the list of items with value 1 is empty.")
  }

  def addItem(solution: List[Int]): List[Int] = {
    val itemIndices = solution.zipWithIndex.filter(pair => pair._1 == 0).map(pair => pair._2)  // indices with value = 0
    if (itemIndices.nonEmpty) {
      var item = random.nextInt(itemIndices.length)  // pick random item to add
      item = itemIndices(item)
      solution.updated(item, 1)  // return updated solution
    } else throw new RuntimeException("@addItem: the list of items with value 0 is empty.")
  }

  def swapItems(solution: List[Int]): List[Int] = {
    val newSol = removeItem(solution)
    addItem(newSol)
  }
}

object Moves {
  def apply(random: Random): Moves = {
    new Moves(random)
  }
}
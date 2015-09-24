package it.polimi.hyperh.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.Solution
import scala.util.Random
import it.polimi.hyperh.search.NeighbourhoodSearch
import it.polimi.hyperh.solution.EvaluatedSolution
import akka.actor._
import util.Timeout
import util.RNG
import it.polimi.hyperh.solution.DummyEvaluatedSolution
/**
 * @author Nemanja
 */
class TSAlgorithm(
    var seed: Option[Solution],
    rng: RNG
    ) extends Algorithm {
  
  private var maxTabooListSize: Int = 7
  private var numOfRandomMoves: Int = 20
  private var neighbourhoodSearch: (List[Int], Int, Int) => List[Int] = NeighbourhoodSearch(rng).INSdefineMove
  /**
   * A secondary constructor.
   */
  def this(maxTabooListSize: Int, numOfRandomMoves: Int) {
    this(None, RNG())
    this.maxTabooListSize = maxTabooListSize
    this.numOfRandomMoves = numOfRandomMoves
  }
  def this(maxTabooListSize: Int) {
    this(None, RNG())
  }
  def this(seed: Option[Solution]) {
    this(seed, RNG())
  }
  def this(rng: RNG) {
    this(None, rng)
  }
  def this() {
    this(None, RNG())
  }
  def getNumOfRandomMoves() = { 
    val copy = numOfRandomMoves
    copy
  }
  def initNEHSolution(p: Problem) = {
    val nehAlgorithm = new NEHAlgorithm()
    nehAlgorithm.evaluate(p)
  }
  def initialSolution(p: Problem): EvaluatedSolution = {
    seed match {
      case Some(seed) => seed.evaluate(p)
      case None => initNEHSolution(p)
    }
  }
  //with default time limit
  def evaluateSmallProblem(p: Problem): EvaluatedSolution = {
    //algorithm time limit
    val timeLimit = p.numOfMachines * (p.numOfJobs / 2.0) * 60 //termination is n*(m/2)*60 milliseconds
    evaluateSmallProblem(p, timeLimit)
  }
  def evaluateSmallProblem(p: Problem, timeLimit: Double): EvaluatedSolution = {
    var evBestSolution = DummyEvaluatedSolution(p)
    var allMoves: List[(Int,Int)] = List()//dummy initalization
    //algorithm time limit
    val expireTimeMillis = Timeout.setTimeout(timeLimit)
    def loop(bestSolution: EvaluatedSolution, taboo: List[Int], iter: Int): EvaluatedSolution = {
      if(Timeout.notTimeout(expireTimeMillis)) {
        if(iter == 1) {
          evBestSolution = initialSolution(p)
          allMoves = NeighbourhoodSearch(rng).generateAllNeighbourhoodMoves(p.numOfJobs)
        } else {
          evBestSolution = bestSolution
        }
        val pair = firstImprovement(p, evBestSolution, allMoves, taboo, expireTimeMillis)
        val evNewSolution = pair._1
        evBestSolution = List(evNewSolution, evBestSolution).min
        val tabooList = updateTabooList(taboo, evBestSolution)
        loop(evBestSolution, tabooList, iter + 1)
      }
      evBestSolution
    }
    loop(evBestSolution, List(), 1)
  }
  //with default time limit
  def evaluateBigProblem(p: Problem): EvaluatedSolution = {
    //algorithm time limit
    val timeLimit = p.numOfMachines * (p.numOfJobs / 2.0) * 60 //termination is n*(m/2)*60 milliseconds
    evaluateBigProblem(p, timeLimit)
  }
  def evaluateBigProblem(p: Problem, timeLimit: Double): EvaluatedSolution = {
    var evBestSolution = DummyEvaluatedSolution(p)
    //algorithm time limit
    val expireTimeMillis = Timeout.setTimeout(timeLimit)
    def loop(bestSolution: EvaluatedSolution, taboo: List[Int], iter: Int): EvaluatedSolution = {
      if(Timeout.notTimeout(expireTimeMillis)) {
        if(iter == 1) {
          evBestSolution = initialSolution(p)
        } else {
          evBestSolution = bestSolution
        }
        //Examine a fixed number of moves that are not taboo, randomly generated. Good method for huge instances
        val allMoves = NeighbourhoodSearch(rng).generateNRandomNeighbourhoodMoves(p.numOfJobs, numOfRandomMoves)
        val pair1 = firstImprovement(p, evBestSolution, allMoves, taboo, expireTimeMillis)
        val evNewSolution = pair1._1
        evBestSolution = List(evNewSolution, evBestSolution).min
        val tabooList = updateTabooList(taboo, evBestSolution)
        loop(evBestSolution, tabooList, iter + 1)
      }
      else evBestSolution
    }
    loop(evBestSolution, List(), 1)
  }
  override def evaluate(p: Problem) = {
    val timeLimit = p.numOfMachines * (p.numOfJobs / 2.0) * 60 //termination is n*(m/2)*60 milliseconds
    if(p.numOfJobs <= 11)
      evaluateSmallProblem(p)
    else
      evaluateBigProblem(p)
  }
  override def evaluate(p:Problem, timeLimit: Double): EvaluatedSolution = {
    if(p.numOfJobs <= 11)
      evaluateSmallProblem(p, timeLimit)
    else
      evaluateBigProblem(p, timeLimit)
  }
  override def evaluate(p:Problem, seedSol: Option[Solution], timeLimit: Double):EvaluatedSolution = {
    seed = seedSol
    evaluate(p, timeLimit)
  }
  def updateTabooList(tabooList: List[Int], solution: EvaluatedSolution): List[Int] = {
    if (tabooList.size == maxTabooListSize) {
        //remove the oldest forbidden move, and add new move at the end
        tabooList.drop(1) ::: List(solution.value)
      } else
        tabooList ::: List(solution.value)
  }
  
  def isForbidden(tabooList: List[Int], makespan: Int) = {
    tabooList.contains(makespan)//forbidden makespan if it is in taboo list
  }

  //Examine all provided moves and take the first which improves the current solution
  def firstImprovement(p: Problem, evOldSolution: EvaluatedSolution, allMoves: List[(Int, Int)], expireTimeMillis: Double) = {
    var bestSolution = evOldSolution
    var candidateMoves = allMoves
    var move = (0, 1) //dummy initialization
    var betterNotFound = true
    while (betterNotFound && candidateMoves.size != 0 && Timeout.notTimeout(expireTimeMillis)) {
      val perturbed = neighbourhoodSearch(evOldSolution.solution.toList, candidateMoves.head._1, candidateMoves.head._2) 
      val evNewSolution = Problem.evaluate(p, new Solution(perturbed))
      if (evNewSolution.value < bestSolution.value) {
        bestSolution = evNewSolution
        move = candidateMoves.head
        betterNotFound = false
      }
      candidateMoves = candidateMoves.tail
    }
    (bestSolution, move)
  }
  //Examine the moves (that are not taboo) and take the first which improves the current solution
  def firstImprovement(p: Problem, evOldSolution: EvaluatedSolution, allMoves: List[(Int, Int)], tabooList: List[Int], expireTimeMillis: Double) = {
    var bestSolution = evOldSolution
    var candidateMoves = allMoves
    var move = (0, 1) //dummy initialization
    var betterNotFound = true
    while (betterNotFound && candidateMoves.size != 0 && Timeout.notTimeout(expireTimeMillis)) {
      val perturbed = neighbourhoodSearch(evOldSolution.solution.toList, candidateMoves.head._1, candidateMoves.head._2)
      val evNewSolution = Problem.evaluate(p, new Solution(perturbed))
      if (evNewSolution.value < bestSolution.value && (! isForbidden(tabooList, evNewSolution.value))) {
        bestSolution = evNewSolution
        move = candidateMoves.head
        betterNotFound = false
      }
      candidateMoves = candidateMoves.tail
    }
    (bestSolution, move)
  }
  //Examine all the moves and take the best
  //the neighbourhood must be examined in parallel for big instances
  def bestImprovement(p: Problem, evOldSolution: EvaluatedSolution, allMoves: List[(Int, Int)], expireTimeMillis: Double) = {
    var bestSolution = evOldSolution
    var candidateMoves = allMoves
    var move = (0, 1) //dummy initialization
    while (candidateMoves.size != 0 && Timeout.notTimeout(expireTimeMillis)) {
      val perturbed = neighbourhoodSearch(evOldSolution.solution.toList, candidateMoves.head._1, candidateMoves.head._2)
      val evNewSolution = Problem.evaluate(p, new Solution(perturbed))
      if (evNewSolution.value < bestSolution.value) {
        bestSolution = evNewSolution
        move = candidateMoves.head
      }
      candidateMoves = candidateMoves.tail
    }
    (bestSolution, move)
  }
  //Examine all the moves (that are not taboo) and take the best
  //the neighbourhood must be examined in parallel for big instances
  def bestImprovement(p: Problem, evOldSolution: EvaluatedSolution, allMoves: List[(Int, Int)], tabooList: List[Int], expireTimeMillis: Double) = {
    var bestSolution = evOldSolution
    var candidateMoves = allMoves
    var move = (0, 1) //dummy initialization
    while (candidateMoves.size != 0 && Timeout.notTimeout(expireTimeMillis)) {
      val perturbed = neighbourhoodSearch(evOldSolution.solution.toList, candidateMoves.head._1, candidateMoves.head._2)
      val evNewSolution = Problem.evaluate(p, new Solution(perturbed))
      if (evNewSolution.value < bestSolution.value && (! isForbidden(tabooList, evNewSolution.value))) {
        bestSolution = evNewSolution
        move = candidateMoves.head
      }
      candidateMoves = candidateMoves.tail
    }
    (bestSolution, move)
  }
  
}

package kp.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.algorithms.Algorithm
import it.polimi.hyperh.spark.{StoppingCondition, TimeExpired}
import kp.problem.KpProblem
import kp.solution.{KpEvaluatedSolution, KpSolution, NaiveKpEvaluatedSolution}

import scala.util.Random
import scala.annotation.tailrec
import kp.util.Moves

class SAAlgorithm(p: KpProblem,
                  initialTemperature: Double,
                  minTemperature: Double,
                  beta: Double,
                  seedOption: Option[KpSolution]
                 ) extends Algorithm {
  // Define default values
  val defaultTimeLimit: Int = 300000
  // Secondary constructors
  def this(p: KpProblem, seedOption: Option[KpSolution]) {
    this(p, 100.0, 0.001, 0.00000005, seedOption)
  }
  def this(p: KpProblem) {
    this(p, 100.0, 0.001, 0.00000005, None)
  }
  seed = seedOption

  def randomSolution: List[Int] = {
    val numItems = p.weights.length
    val solution = Array.fill(numItems)(0)  // solution with only zeros
    val numInitialItems: Int = (0.1 * numItems).round.toInt  // TODO: explain choice
    val randomIndices = Seq.fill(numInitialItems)(Random.nextInt(numItems))
    randomIndices.foreach(solution(_) = 1)
    solution.toList
  }

  def initialSolution: KpEvaluatedSolution = {
    seed match {  // if a seed is set, evaluate it. Otherwise create random initial solution.
      case Some(seedValue) => seedValue.evaluate(p).asInstanceOf[KpEvaluatedSolution]
      case None => p.evaluate(KpSolution(randomSolution)).asInstanceOf[KpEvaluatedSolution]
    }
  }

  override def evaluate(problem: Problem): EvaluatedSolution = {
    val p = problem.asInstanceOf[KpProblem]
    val stopCond = new TimeExpired(defaultTimeLimit)
    evaluate(p, stopCond)
  }

  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = {
    val random = new Random()
    val p = problem.asInstanceOf[KpProblem]

    def weights(solution: List[Int]): Int = {
      val itemIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)
      p.calculateWeights(itemIndices)
    }

    def fitness(solution: List[Int]): KpEvaluatedSolution = p.evaluate(KpSolution(solution)).asInstanceOf[KpEvaluatedSolution]

    def checkConstraint(solution: List[Int]): Boolean = {
      if (weights(solution) <= p.capacity) {
        true
      } else false
    }

    @tailrec
    def validMove(solution: List[Int]): List[Int] = {
      // create new solution randomly
      var newSolution = List[Int]()  // empty list
      var functionInt = 9999999
      val itemIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)  // how many items are selected?
      if (itemIndices.length < 1){  // if there are no item in the current solution, always add an item
        newSolution = Moves(random).addItem(solution)
      } else {
        functionInt = Random.nextInt(List(1, 2, 3, 4, 5).length)  // produces random number 0, 1, 2, 3, or 4
        if ((functionInt == 0) || (functionInt == 1)) newSolution = Moves(random).addItem(solution) // add and swap are more likely than remove
        if ((functionInt == 2) || (functionInt == 3)) newSolution = Moves(random).swapItems(solution)
        if (functionInt == 4) newSolution = Moves(random).removeItem(solution)
      }
      // Check new solution
      if (checkConstraint(newSolution)) {
        newSolution // return the new solution
      } else validMove(solution)  // create new solution if constraint is not satisfied
    }

    def acceptanceProbability(benefit: Double, temperature: Double): Double = scala.math.exp(benefit/temperature)

    var evOldSolution = NaiveKpEvaluatedSolution(p)  // purely because it has to be an instance of NrEvaluatedSolution
    val stop = stopCond.asInstanceOf[TimeExpired].initialiseLimit()

    @tailrec
    def loop(old: KpEvaluatedSolution, temp: Double, iter: Int): KpEvaluatedSolution = {
      if ((temp > minTemperature)  && stop.isNotSatisfied()) {
        if (iter == 1){
          // initialize solution
          evOldSolution = initialSolution
        } else evOldSolution = old
        var temperature = temp
        // generate random new solution
        val newSolution = validMove(evOldSolution.solution.toList)
        // calculate fitness for new solution
        val evNewSolution = fitness(newSolution)
        // calculate benefit from move
        val benefit = evNewSolution.value - evOldSolution.value
        // calculate acceptance probability
        val ap = acceptanceProbability(benefit, temperature)
        val randNo = random.nextDouble()
        if ((benefit > 0) || (randNo <= ap)) {
          evOldSolution = evNewSolution
        }
        temperature = temperature / (1 + beta*temperature)  // update temperature
        loop(evOldSolution, temperature, iter+1)  //  start new iteration
      } else evOldSolution
    }
    loop(evOldSolution, initialTemperature, iter = 1)
  }

}
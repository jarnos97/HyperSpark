package nrp.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.algorithms.Algorithm
import it.polimi.hyperh.spark.{StoppingCondition, TimeExpired}
import nrp.problem.NrProblem
import nrp.solution.{NrSolution, NrEvaluatedSolution, NaiveNrEvaluatedSolution}
import scala.util.Random
import scala.annotation.tailrec
import nrp.util.Moves


class SAAlgorithm() extends Algorithm {
  // Define default values
  var initialTemperature: Double = 100.0
  var minTemperature: Double = 0.001
  var beta: Double = 0.00000005
  var totalCosts = 1000
  var boundP: Double = 0.3
  var bound: Double = (totalCosts * boundP).round
  val defaultTimeLimit: Int = 300000 // 5 minutes
  // Secondary constructors
  def this(initT: Double, minT: Double, b: Double, totalCosts: Double, boundPercentage: Double)  {
    this()
    initialTemperature = initT
    minTemperature = minT
    beta = b
    boundP = boundPercentage
    bound = (totalCosts * boundPercentage).round
  }
  def this(initT: Double, minT: Double, b: Double, totalCosts: Double, boundPercentage: Double,
           seedOption: Option[NrSolution])  {
    this()
    initialTemperature = initT
    minTemperature = minT
    beta = b
    boundP = boundPercentage
    bound = (totalCosts * boundPercentage).round
    seed = seedOption
  }
  def this(seedOption: Option[NrSolution]) {
    this()
    seed = seedOption
  }

  def randomSolution(numCustomers: Int): List[Int]= {
    val solution = Array.fill(numCustomers)(0)  // solution with only zeros
    val numInitialCustomers: Int = (0.1 * numCustomers * boundP).round.toInt // TODO: this the best percentage?
    val randomIndices = Seq.fill(numInitialCustomers)(Random.nextInt(numCustomers))  // TODO: Changed this! from static 100.
    randomIndices.foreach(solution(_) = 1)
    solution.toList
  }

  def initialSolution(p: NrProblem): NrEvaluatedSolution = {
    seed match {  // if a seed is set, evaluate it. Otherwise create random initial solution.
      case Some(seedValue) => seedValue.evaluate(p).asInstanceOf[NrEvaluatedSolution]
      case None => p.evaluate(NrSolution(randomSolution(p.numCustomers))).asInstanceOf[NrEvaluatedSolution]
    }
  }

  override def evaluate(problem: Problem): EvaluatedSolution = {
    val p = problem.asInstanceOf[NrProblem]
    val stopCond = new TimeExpired(defaultTimeLimit)
    evaluate(p, stopCond)
  }

  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = {
    val random = new Random
    val p = problem.asInstanceOf[NrProblem]

    def cost(solution: List[Int]): Double = {
      val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)
      p.calculateCosts(customerIndices)
    }

    def fitness(solution: List[Int]): NrEvaluatedSolution = p.evaluate(NrSolution(solution)).asInstanceOf[NrEvaluatedSolution]

    def checkConstraint(solution: List[Int]): Boolean = {
      if (cost(solution) <= bound) {
        true
      } else false
    }

    @tailrec
    def validMove(solution: List[Int]): List[Int] = {
      // create new solution randomly
      var newSolution = List[Int]()  // empty list
      var functionInt = 9999999
      val customerIndices = solution.zipWithIndex.filter(pair => pair._1 == 1).map(pair => pair._2)  // how many customers are selected?
      if (customerIndices.length < 1){  // if there are no customers in the current solution, always add a customer
        newSolution = Moves(random).addCustomer(solution)
      } else {
        functionInt = Random.nextInt(List(1, 2, 3, 4, 5).length)  // produces random number 0, 1, 2, 3, or 4
        if ((functionInt == 0) || (functionInt == 1)) newSolution = Moves(random).addCustomer(solution) // add and swap are more likely than remove
        if ((functionInt == 2) || (functionInt == 3)) newSolution = Moves(random).swapCustomers(solution)
        if (functionInt == 4) newSolution = Moves(random).removeCustomer(solution)
      }
      // Check new solution
      if (checkConstraint(newSolution)) {
        newSolution // return the new solution
      } else validMove(solution)  // create new solution if constraint is not satisfied
    }

    def acceptanceProbability(benefit: Double, temperature: Double): Double = scala.math.exp(benefit/temperature)

    var evOldSolution = NaiveNrEvaluatedSolution(p)  // purely because it has to be an instance of NrEvaluatedSolution
    val stop = stopCond.asInstanceOf[TimeExpired].initialiseLimit()

    @tailrec
    def loop(old: NrEvaluatedSolution, temp: Double, iter: Int): NrEvaluatedSolution = {
      if ((temp > minTemperature)  && stop.isNotSatisfied()) {
        if (iter == 1){
          // initialize solution
          evOldSolution = initialSolution(p)
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
        val randomNo = random.nextDouble()
        if ((benefit > 0) || (randomNo <= ap)) {
          evOldSolution = evNewSolution
        }
        temperature = temperature / (1 + beta*temperature)  // update temperature
        loop(evOldSolution, temperature, iter+1)  //  start new iteration
      } else evOldSolution
    }
    loop(evOldSolution, initialTemperature, iter = 1)
  }

}
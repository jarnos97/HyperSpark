package nrp.algorithms

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.solution.EvaluatedSolution
import it.polimi.hyperh.algorithms.Algorithm
import it.polimi.hyperh.spark.StoppingCondition
import it.polimi.hyperh.spark.TimeExpired
import nrp.problem.NrProblem
import nrp.solution.NrSolution
import nrp.solution.NrEvaluatedSolution
import scala.util.Random
import scala.annotation.tailrec
import nrp.util.Moves
import nrp.solution.NaiveNrEvaluatedSolution

class SAAlgorithm() extends Algorithm {
  // Define default values
  var initialTemperature: Double = 200.00  // TODO: pick better default
  var minTemperature: Double = 0.001  // TODO: pick better default
  var beta: Double = 0.00000005  // TODO: pick better default
  var bound: Double = 0.7 // TODO: pick better default
  var timeLimit: Int = 100000 // TODO: pick better default
  /**
   * Secondary constructors
   */
  def this(initT: Double, minT: Double, b: Double, boundB: Double, timeL: Int)  {
    this()
    initialTemperature = initT
    minTemperature = minT
    beta = b
    bound = boundB
    timeLimit = timeL
  }
  def this(initT: Double, minT: Double, b: Double, boundB: Double, timeL: Int, seedOption: Option[NrSolution])  {
    this()
    initialTemperature = initT
    minTemperature = minT
    beta = b
    bound = boundB
    timeLimit = timeL
    seed = seedOption
  }

  def randomSolution(numCustomers: Int): List[Int]= {
    val solution = Array.fill(numCustomers)(0)  // solution with only zeros
    val randomIndices = Seq.fill(4)(Random.nextInt(100))  // can change random to Random
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
    val stopCond = new TimeExpired(timeLimit)
    evaluate(p, stopCond)
  }

  override def evaluate(problem: Problem, stopCond: StoppingCondition): EvaluatedSolution = {
    val random = new Random
    val p = problem.asInstanceOf[NrProblem]

    def cost(solution: List[Int]): Double = {p.calculateCosts(solution)}
    def fitness(solution: List[Int]): NrEvaluatedSolution = {p.evaluate(NrSolution(solution)).asInstanceOf[NrEvaluatedSolution]}

    def checkConstraint(solution: List[Int]): Boolean = {
      if (cost(solution) <= bound)
        true
      else false
    }

    @tailrec
    def validMove(solution: List[Int]): List[Int] = {
      // create new solution randomly
      var newSolution = List[Int]()  // empty list
      val functionInt = random.nextInt(List(1, 2, 3).length)  // produces random number 0, 1, or 2
      if (functionInt == 0) newSolution = Moves(random).removeCustomer(solution)
      if (functionInt == 1) newSolution = Moves(random).addCustomer(solution)
      if (functionInt == 2) newSolution = Moves(random).swapCustomers(solution)
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
        } else {
          evOldSolution = old
        }
        var temperature = temp
//        println("Current temperature:" + temperature)
        // generate random new solution
        val newSolution = validMove(evOldSolution.solution.toList)
        // calculate fitness for new solution
        val evNewSolution = fitness(newSolution)

        // calculate benefit from move
        val benefit = evNewSolution.value - evOldSolution.value
//        println("Benefit of new solution:" + benefit)
        // calculate acceptance probability
        val ap = acceptanceProbability(benefit, temperature)
//        println("Acceptance probability new solution:" + ap)
        val randomNo = random.nextDouble()
        if ((benefit > 0) || (randomNo <= ap)) {
          evOldSolution = evNewSolution
//          println("Fitness of new accepted solution:" + evOldSolution.value)
        }
        temperature = temperature / (1 + beta*temperature)  // update temperature
        loop(evOldSolution, temperature, iter+1)  //  start new iteration
      }
      else evOldSolution
    }
    loop(evOldSolution, initialTemperature, iter = 1)
  }

}
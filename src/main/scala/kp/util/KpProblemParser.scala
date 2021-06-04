package kp.util

import scala.io.Source
import kp.problem.KpProblem

object KpProblemParser{
  def apply(fileName: String): KpProblem = {
    val path = "src/main/resources/" + fileName + ".txt"
    // Read capacity parameter
    val capacityString = Source.fromFile(path).getLines.find(_ => true).toArray
    val capacity = capacityString.map(x => x.toInt)
    // Read profits and weights
    val values = Source.fromFile(path).getLines().drop(1).map(_.split(" ").map(x => x.toInt)).toArray
    val profits = values.map(x => x(0))
    val weights = values.map(x => x(1))
    // New problem instance
    new KpProblem(capacity(0), profits, weights)
  }
}
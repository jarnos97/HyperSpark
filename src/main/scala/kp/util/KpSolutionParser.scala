package kp.util

import scala.io.Source
import kp.solution.KpSolution
import kp.solution.KpEvaluatedSolution
import scala.util.parsing.combinator.RegexParsers

object KpSolutionParser{
  def apply(fileName: String): KpSolution = {
    val path = "src/main/resources/"
    val solutionFile = path + fileName
    val solutionCharacter = Source.fromFile(solutionFile).getLines().toArray
    val solution = solutionCharacter.map(x => x.toInt)
    // Initialize new solution
    new KpSolution(solution)
  }
}

object KpEvaluatedSolutionParser extends RegexParsers {
  def number: Parser[Int] = """\d+""".r ^^ { _.toInt }
  def identifier  = """[_\p{L}][_\p{L}\p{Nd}]*""".r
  def row: Parser[Array[Int]] = number.+ ^^ {_.toArray}
  def solution: Parser[KpEvaluatedSolution] = identifier ~> number ~ row ^^ {
    case ms ~ r => new KpEvaluatedSolution(ms,r)
  }
  def apply(input: String): Option[KpEvaluatedSolution] = parseAll(solution, input) match {
    case Success(result, _) => Some(result)
    case NoSuccess(_, _) => None
  }
}
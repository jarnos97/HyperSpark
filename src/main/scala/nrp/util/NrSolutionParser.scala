package nrp.util

import scala.io.Source
import nrp.solution.NrSolution

//import scala.util.parsing.combinator.RegexParsers
//import nrp.solution.NrEvaluatedSolution

object NrSolutionParser{
  def apply(fileName: String): NrSolution = {
    val path = "src/main/resources/"
    // Load Initial solution
    val solutionFile = path + fileName
    val solutionCharacter = Source.fromFile(solutionFile).getLines().toArray
    val solution = solutionCharacter.map(x => x.toInt)

    new NrSolution(solution)
  }
}

//
//object NrEvaluatedSolutionParser extends RegexParsers {
//
//  def number: Parser[Int] = """\d+""".r ^^ { _.toInt }
//  def identifier  = """[_\p{L}][_\p{L}\p{Nd}]*""".r
//  def row: Parser[Array[Int]] = number.+ ^^ {_.toArray}
//  def solution: Parser[NrEvaluatedSolution] = identifier ~> number ~ row ^^ {
//    case ms ~ r => new NrEvaluatedSolution(ms,r)
//  }
//  def apply(input: String): Option[NrEvaluatedSolution] = parseAll(solution, input) match {
//    case Success(result, _) => Some(result)
//    case NoSuccess(_, _) => None
//  }
//
//}



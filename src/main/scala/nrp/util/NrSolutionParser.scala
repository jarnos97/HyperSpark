package nrp.util

import scala.io.Source
import nrp.solution.NrSolution
import nrp.solution.NrEvaluatedSolution
//import it.polimi.hyperh.solution.EvaluatedSolution
//import it.polimi.hyperh.solution.Solution
import scala.util.parsing.combinator.RegexParsers


object NrSolutionParser{
  def apply(fileName: String): NrSolution = {
    val path = "src/main/resources/"
    // Load Initial solution
    val solutionFile = path + fileName
    val solutionCharacter = Source.fromFile(solutionFile).getLines().toArray
    val solution = solutionCharacter.map(x => x.toInt)
    // Initialize new solution
    new NrSolution(solution)
  }
}

//object NrEvaluatedSolutionParser{
//  def parseFunction(valueFileName: String, solutionFileName: String): Option[NrEvaluatedSolution] = {
//    val path = "src/main/resources/"
//    // Load solution value
//    val valueFile = path + valueFileName
//    val valueCharacter = Source.fromFile(valueFile).getLines().toArray
//    val value = valueCharacter(0).toInt
//    // Load Initial solution
//    val solutionFile = path + solutionFileName
//    val solutionCharacter = Source.fromFile(solutionFile).getLines().toArray
//    val solution = solutionCharacter.map(x => x.toInt)
//    // Initialize new evaluated solution
//    val b:Option[NrEvaluatedSolution] = None
//    new NrEvaluatedSolution(value, solution)
//
//  }
//
//  def apply(value: String, solution: String): Option[NrEvaluatedSolution] = {
//    value match {
//      case Some(valueTemp) => parseFunction(valueTemp, solution)
//    }
//  }
//}


object NrEvaluatedSolutionParser extends RegexParsers {

  def number: Parser[Int] = """\d+""".r ^^ { _.toInt }
  def identifier  = """[_\p{L}][_\p{L}\p{Nd}]*""".r
  def row: Parser[Array[Int]] = number.+ ^^ {_.toArray}
  def solution: Parser[NrEvaluatedSolution] = identifier ~> number ~ row ^^ {
    case ms ~ r => new NrEvaluatedSolution(ms,r)
  }
  def apply(input: String): Option[NrEvaluatedSolution] = parseAll(solution, input) match {
    case Success(result, _) => Some(result)
    case NoSuccess(_, _) => None
  }

}
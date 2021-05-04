//package nrp.util
//
//import scala.util.parsing.combinator.RegexParsers
//import nrp.solution.NrSolution
//import nrp.solution.NrEvaluatedSolution
//
////import pfsp.util.PfsEvaluatedSolutionParser.parseAll
////import pfsp.util.PfsSolutionParser.parseAll
//
//object NrSolutionParser extends RegexParsers {
//
//  def number: Parser[Int] = """\d+""".r ^^ { _.toInt }
//  def identifier  = """[_\p{L}][_\p{L}\p{Nd}]*""".r
//  def row: Parser[Array[Int]] = number.+ ^^ {_.toArray}
//  def boolNumber: Parser[Int] = """\d+""".r ^^ { _.toInt}
//  def solution: Parser[NrSolution] = identifier ~> boolNumber ~ row ^^ {
//    case ms ~ r => new NrSolution(r)
//  }
//  def apply(input: String): Option[NrSolution] = parseAll(solution, input) match {
//    case Success(result, _) => Some(result)
//    case NoSuccess(_, _) => None
//  }
//
//}
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



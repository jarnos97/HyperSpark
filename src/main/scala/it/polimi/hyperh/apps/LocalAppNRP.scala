package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.spark.{Framework, FrameworkConf, TimeExpired}

import nrp.problem.NrProblem
import scala.io.Source

object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    //    val problem = NrProblem.fromResources(name = "NRP1.txt")
    val customerWeights =  NrProblem.fromResources(fileName = "NRP1")
    println(customerWeights.mkString("Array(", ", ", ")"))  // does not work because it is an array of arrays
    println(customerWeights(0).mkString("Array(", ", ", ")"))
  }
}


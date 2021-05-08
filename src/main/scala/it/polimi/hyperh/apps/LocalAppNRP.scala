package it.polimi.hyperh.apps

import it.polimi.hyperh.problem.Problem
import it.polimi.hyperh.spark.{Framework, FrameworkConf, TimeExpired}

import nrp.problem.NrProblem
import scala.io.Source

object LocalAppNRP {
  def main(args: Array[String]): Unit = {
    val problem =  NrProblem.fromResources(fileName = "NRP1")
  }
}


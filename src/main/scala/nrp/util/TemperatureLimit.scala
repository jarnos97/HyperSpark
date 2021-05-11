package nrp.util

import it.polimi.hyperh.spark.StoppingCondition


class TemperatureLimit(minTemperature: Double) extends StoppingCondition {
  // We override this method because it is mandatory
  override def isSatisfied(): Boolean = {false}

  // This method we will be using, we need a parameter to known the current temperature
  override def isSatisfiedParam(currentTemperature: Double): Boolean = {
    if (currentTemperature <= minTemperature)
      true
    else false
  }
}
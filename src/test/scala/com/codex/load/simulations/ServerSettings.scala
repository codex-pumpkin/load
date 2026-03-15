package com.codex.load.simulations

import com.codex.load.Endpoints.{getAnalytics, getQuota, loginAsAdmin, updateQuota}
import io.gatling.commons.validation.SuccessWrapper
import io.gatling.core.Predef.{exec, scenario}

import scala.util.Random

class ServerSettings extends BaseSimulation {

  private val updateQuotaScenario = scenario("Update quota")
    .exec(loginAsAdmin)
    .repeat(_ => Random.between(2, 5).success) {
      exec(updateQuota)
        .exec(getQuota)
    }
    .exec(getQuota)

  private val retrieveAnalyticsScenario = scenario("Get analytics")
    .exec(loginAsAdmin)
    .exec(getAnalytics)

  addScenario(updateQuotaScenario)
  addScenario(retrieveAnalyticsScenario)

  setup()

}

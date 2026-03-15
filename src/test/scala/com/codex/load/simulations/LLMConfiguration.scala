package com.codex.load.simulations

import com.codex.load.Endpoints.{createLLMConfig, getLlmConfigs, loginAsAdmin, updateLLMConfig}
import io.gatling.commons.validation.SuccessWrapper
import io.gatling.core.Predef.{exec, scenario}

import scala.util.Random

class LLMConfiguration extends BaseSimulation {

  private val llmManipulationScenario = scenario("Manipulating the LLM configurations")
    .exec(loginAsAdmin)
    .exec { session =>
      session.set("llmCount", Random.between(3, 7)).success
    }
    .repeat(_ => Random.between(2, 7).success) {
      exec(createLLMConfig)
      .exec(updateLLMConfig)
    }
    .exec(getLlmConfigs)

  addScenario(llmManipulationScenario)

  setup()

}

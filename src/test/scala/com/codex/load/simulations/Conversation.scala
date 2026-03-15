package com.codex.load.simulations

import com.codex.load.Endpoints._
import io.gatling.core.Predef._

import scala.util.Random

class Conversation extends BaseSimulation {

  private val manipulatingConversation = scenario("Manipulating a single conversation")
    .exec(signup)
    .exec(login)
    .exec(createConversation)
    .exec(updateConversation)
    .exec(deleteConversation)

  private val bigConversation = scenario("Multiple follow up messages")
    .exec(signup)
    .exec(login)
    .exec(createConversation)
    .repeat(_ => Random.between(1, 5)) {
      exec(addMessage)
    }

  addScenario(manipulatingConversation)
  addScenario(bigConversation)

  setup()

}

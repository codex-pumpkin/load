package com.codex.load.simulations

import com.codex.load.Endpoints._
import io.gatling.core.Predef._

class Authentication extends BaseSimulation {

  /**
   * Scenario for creating new accounts.
   */
  private val accountCreateScenario = scenario("Create account and logout")
    .exec(signup)
    .exec(login)
    .exec(getOwnData)
    .exec(getProfilePicture)
    .exec(logout)

  /**
   * Scenario for refreshing after logging out.
   */
  private val refreshScenario = scenario("Use refresh token to reauthenticate")
    .exec(signup)
    .exec(login)
    .exec(refresh)

  /**
   * Scenario for refreshing after logging out.
   */
  private val changeAccountDataScenario = scenario("Change account data")
    .exec(signup)
    .exec(login)
    .exec(updateAccountData)
    .exec(updateProfilePicture)
    .exec(getProfilePicture)
    .exec(getOwnData)

  //addScenario(accountCreateScenario)
  //addScenario(refreshScenario)
  //addScenario(changeAccountDataScenario)

  setup()

}

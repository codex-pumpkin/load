package com.codex.load

import io.gatling.commons.validation.SuccessWrapper
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

/**
 * Collection of the endpoints used in scenarios.
 */
object Endpoints {

  /**
   * Request to POST /auth/login/.
   */
  val login: HttpRequestBuilder = http("Login with credentials")
    .post("/auth/login")
    .body(ElFileBody("bodies/login.json"))
    .check(status is 200)
    .check(jsonPath("$.access_token").saveAs("accessToken"))
    .check(jsonPath("$.refresh_token").saveAs("refreshToken"))

  /**
   * Request to POST /auth/login with admin credentials
   */
  val loginAsAdmin: ChainBuilder = exec { session =>
    session.set("email", System.getProperty("adminEmail"))
      .set("password", System.getProperty("adminPassword"))
      .success
  }.exec(http("Login as Admin")
    .post("/auth/login")
    .body(StringBody(
      """
        |{
        |"email": "#{email}",
        |"username": null,
        |"password": "#{password}"
        |}
        |""".stripMargin))
    .check(status is 200)
    .check(jsonPath("$.access_token").saveAs("accessToken"))
    .check(jsonPath("$.refresh_token").saveAs("refreshToken")))

  /**
   * Request to POST /auth/signup/.
   */
  val signup: ChainBuilder = exec { session =>
    val email =
      s"${randomAlphanumeric(10)}@" +
        s"${randomAlphanumeric(5)}.com"

    val username = randomAlphanumeric(10)
    val password = randomAlphanumeric(10)

    session
      .set("email", email)
      .set("username", username)
      .set("password", password)
  }.exec(
    http("Signup with credentials")
      .post("/auth/signup")
      .body(ElFileBody("bodies/signup.json"))
      .check(status is 201)
  )

  val createLLMConfig: HttpRequestBuilder = withBearerAuth(http("Create LLM config")
    .post("/llm-configurations")
    .body(ElFileBody("bodies/llm_config.json"))
    .check(status is 200))
    .check(jsonPath("$.id").saveAs("configId"))

  val updateLLMConfig: HttpRequestBuilder = withBearerAuth(http("Update LLM config")
    .put("/llm-configurations/#{configId}")
    .body(ElFileBody("bodies/llm_config.json"))
    .check(status is 200))

  val deleteLLMConfig: HttpRequestBuilder = withBearerAuth(http("Delete LLM config")
    .delete("/llm-configurations/#{configId}")
    .check(status is 200))

  val getLlmConfigs: HttpRequestBuilder = withBearerAuth(http("Get all LLM configs")
    .get("/llm-configurations")
    .check(status is 200))

  /**
   * Request to POST /auth/logout/.
   */
  val logout: HttpRequestBuilder = withBearerAuth(http("Logout from all devices")
    .post("/auth/logout")
    .queryParam("refresh_token", "#{refreshToken}")
    .check(status is 200))

  /**
   * Adds the saved access token to the headers
   */
  private def withBearerAuth(http: HttpRequestBuilder): HttpRequestBuilder = {
    http.header("Authorization", "Bearer #{accessToken}")
  }

  /**
   * Request to GET /me/.
   */
  val getOwnData: HttpRequestBuilder = withBearerAuth(http("Get account data")
    .get("/account")
    .check(status is 200))

  val updateAccountData: HttpRequestBuilder = withBearerAuth(http("Update account data")
    .put("/account")
    .body(ElFileBody("bodies/update_account.json"))
    .check(status is 200))

  val updateProfilePicture: HttpRequestBuilder = withBearerAuth(http("Update profile picture")
    .put("/account/profile-picture")
    .bodyPart(RawFileBodyPart("file", "data/profile-pic.jpg"))
    .asMultipartForm
    .check(status is 200)
  )

  val getProfilePicture: HttpRequestBuilder = withBearerAuth(http("Get profile picture")
    .get("/account/profile-picture")
    .check(status is 200))

  /**
   * Request to POST /auth/refresh/.
   */
  val refresh: HttpRequestBuilder = withBearerAuth(http("Authenticate with refresh token")
    .post("/auth/refresh/")
    .body(ElFileBody("bodies/refresh.json"))
    .check(status is 200)
    .check(jsonPath("$.access_token").saveAs("accessToken"))
    .check(jsonPath("$.refresh_token").saveAs("refreshToken")))

  /**
   * Request to GET /quota
   */
  val getQuota: HttpRequestBuilder = withBearerAuth(http("Get server quota")
  .get("/quota")
  .check(status is 200))

  /**
   * Request to PUT /quota
   */
  val updateQuota: HttpRequestBuilder = withBearerAuth(http("Update server quota")
  .put("/quota")
    .body(ElFileBody("bodies/quota.json"))
  .check(status is 200))

  val getAnalytics: HttpRequestBuilder = withBearerAuth(http("Get analytics")
  .get("/analytics"))
    .queryParam("period_of_days", 4)
    .queryParam("time_interval_minutes", 69)
    .check(status is 200)

  def randomAlphanumeric(length: Int): String = {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val random = new scala.util.Random
    (1 to length).map(_ => chars(random.nextInt(chars.length))).mkString
  }

}

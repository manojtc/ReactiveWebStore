import org.scalatestplus.play._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.ws.WSClient
import play.api.inject.guice.GuiceApplicationBuilder

class RndDoubleGeneratorControllerTestSpec extends PlaySpec
    with OneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
    val injector = new GuiceApplicationBuilder().injector
    val ws:WSClient = injector.instanceOf(classOf[WSClient])
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    "Assume ng-microservice is down rx number should be" must {
        "work" in {
            val future = ws.url(s"http://localhost:${port}/rnd/rxbat").get().map { res => res.body }
            val response = Await.result(future, 15.seconds)
            response mustBe "2.3000000000000007"
        }
    }
}

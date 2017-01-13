package controllers

import javax.inject.Inject
import javax.inject.Singleton
import services._
import models._
import play.api.mvc._

@Singleton
class RndDoubleGeneratorController @Inject() (service:IRndService) extends Controller {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    def rndDouble = Action { implicit request =>
        Ok(service.next().toString())
    }
    def rndCall = Action.async { implicit request =>
        service.call().map { res => Ok(res) }
    }
    def rxCall = Action { implicit request =>
        Ok(service.rxScalaCall().toBlocking.first.toString())
    }
    def rxScalaCallBatch = Action { implicit request =>
        Ok(service.rxScalaCallBatch().toBlocking.first.toString())
    }
}

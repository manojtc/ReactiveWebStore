package controllers

import javax.inject.Singleton
import javax.inject.Inject
import play.api.mvc._
import models._
import controllers._

@Singleton
class RxController @Inject()(price: IPriceService) extends Controller {
    def prices = Action { implicit request =>
        Logger.info("RX called.")
        import ExecutionContext.Implicits.global
        val sourceObservable = priceService.generatePrices
        val rxResult = Observable.create {
            sourceObservable.subscribe
        }.subscribeOn(IOScheduler())
         .take(1)
         .flatMap { x => println(x) ; Observable.just(x) }
         .toBlocking
         .first
        Ok("RxScala Price suggested is = " + rxResult)
    }

    def pricesAsync = Action.async { implicit request =>
        Logger.info("RX Async called.")
        import play.api.libs.concurrent.Execution.Implicits.defaultContext
        val sourceObservable = priceService.generatePrices
        val rsResult = Observable.create {
            sourceObservable.subscribe
        }.subscribeOn(IOScheduler())
         .take(1)
         .flatMap { x => println(x) ; Observable.just(x) }
         .toBlocking
         .first
        Future { Ok("RxScala Price suggested is = " + rxResult)}
    }
}

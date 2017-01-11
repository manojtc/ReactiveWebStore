package services

import javax.inject.Singleton
import play.Logger
import rx.lang.scala.Observable
import rx.lang.scala.schedulers.IOScheduler
import rx.lang.scala.subjects.PublishSubject
import scala.concurrent.Future
import scala.util.Random.nextDouble

trait IPriceService {
    def generatePrices: Observable[Double]
}

@Singleton
class PriceService extends IPriceService {
    var doubleInfiniteStreamSubject = PublishSubject.apply[Double]()
    import scala.concurrent.ExecutionContext.Implicits.global
    Future {
        Stream.continually(nextDouble * 1000.0).foreach {
            x => Thread.sleep(1000)
            doubleInfiniteStreamSubject.onNext(x)
        }
    }

    override def generatePrices: Observable[Double] = {
        var observableEven = Observable.create {
            doubleInfiniteStreamSubject.subscribe
        }.subscribeOn(IOScheduler())
         .flatMap { x => Observable.from(Iterable.fill(1)(x + 10))}
         .filter { x => x.toInt % 2 != 0 }
        var observableOdd = Observable.create {
            doubleInfiniteStreamSubject.subscribe
        }.subscribeOn(IOScheduler())
         .flatMap { x => Observable.from(Iterable.fill(1)(x + 10))}
         .filter { x => x.toInt % 2 == 0}
        var mergeObservable = Observable
            .empty
            .subscribeOn(IOScheduler())
            .merge(observableEven)
            .merge(observableOdd)
            .take(10)
            .foldLeft(0.0)(_+_)
            .flatMap { x => Observable.just( x - (x * 0.9)) }
        return mergeObservable
    }
}

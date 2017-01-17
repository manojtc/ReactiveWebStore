package services

import javax.inject.Inject
import javax.inject.Singleton
import models.Review
import dao.ReviewDao
import dao.IReviewDao
import scala.concurrent.Future
import utils.Awaits

trait IReviewService extends BaseService[Review] {
    def insert(review:Review): Future[Unit]
    def update(id:Long, review:Review): Future[Unit]
    def remove(id:Long): Future[Int]
    def findById(id:Long): Future[Option[Review]]
    def findAll(): Future[Option[Seq[Review]]]
}

@Singleton
class ReviewService @Inject() (dao:IReviewDao) extends IReviewService {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    def insert(review:Review): Future[Unit] = { dao.insert(review) }
    def update(id:Long, review:Review): Future[Unit] = {
        review.id = Option(id.toInt)
        dao.update(review)
    }
    def remove(id:Long): Future[Int] = {
        dao.remove(id)
    }
    def findById(id:Long): Future[Option[Review]] = {
        dao.findById(id)
    }
    def findAll(): Future[Option[Seq[Review]]] = {
        dao.findAll().map { x => Option(x) }
    }
    private def validateId(id:Long): Unit = {
        val future = findById(id)
        val entry = Awaits.get(5, future)
        if(entry == null || entry.equals(None)) throw new RuntimeException("Could not find Review: " + id)
    }
}

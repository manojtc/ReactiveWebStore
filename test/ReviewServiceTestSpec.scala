import org.scalatestplus.play._
import services._
import models._

class ReviewServiceTestSpec extends PlaySpec {
    "ReviewService" must {
        val service:IReviewService = new ReviewService
        "insert a review properly" in {
            val review = new Review(Some(1), Some(1), "Manoj Chandrasekar", "Testing is good")
            service.insert(review)
        }
        "update a review" in {
            val review = new Review(Some(1), Some(1), "Manoj Chandrasekar", "Testing is good")
            service.update(1, review)
        }
        "not update because does not exist" in {
            intercept[RuntimeException] {
                service.update(333, null)
            }
        }
        "find the review 1" in {
            val review = service.findById(1)
            review.get.id mustBe Some(1)
            review.get.author mustBe "Manoj Chandrasekar"
            review.get.comment mustBe "Testing is good"
            review.get.productId mustBe Some(1)
        }
        "find all" in {
            val reviews = service.findAll()
            reviews.get.length mustBe 1
            reviews.get(0).id mustBe Some(1)
            reviews.get(0).author mustBe "Manoj Chandrasekar"
            reviews.get(0).comment mustBe "Testing is good"
            reviews.get(0).productId mustBe Some(1)
        }
        "remove 1 review" in {
            val review = service.remove(1)
            review mustBe true
            val oldReview = service.findById(1)
            oldReview mustBe None
        }
        "not remove because does not exist" in {
            intercept[RuntimeException] {
                service.remove(-1)
            }
        }
    }
}

import org.scalatestplus.play._
import utils.DBCleaner

class ReviewControllerTestSpec extends PlaySpec with OneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
    DBCleaner.cleanUp()
    "ReviewController" should {
        "insert a new review should be ok" in {
            goTo(s"http://localhost:${port}/product/add")
            click on id("name")
            enter("Blue Ball")
            click on id("details")
            enter("Blue Ball is an awesome and simple product")
            click on id("price")
            enter("17.55")
            submit()
            goTo(s"http://localhost:${port}/review/add")
            singleSel("productId").value = "1"
            click on id("author")
            enter("Manoj Chandrasekar")
            click on id("comment")
            enter("Tests are amazing!")
            submit()
        }
        "details from the review 1 should be ok" in {
            goTo(s"http://localhost:${port}/review/details/1")
            textField("author").value mustBe "Manoj Chandrasekar"
            textField("comment").value mustBe "Tests are amazing!"
        }
        "update review 1 should be ok" in {
            goTo(s"http://localhost:${port}/review/details/1")
            textField("author").value = "Manoj Chandrasekar2"
            textField("comment").value = "Tests are amazing 2!"
            submit()
            goTo(s"http://localhost:${port}/review/details/1")
            textField("author").value mustBe "Manoj Chandrasekar2"
            textField("comment").value mustBe "Tests are amazing 2!"
        }
        "delete a review should be ok" in {
            goTo(s"http://localhost:${port}/review/add")
            singleSel("productId").value = "1"
            click on id("author")
            enter("Manoj Chandrasekar")
            click on id("comment")
            enter("Tests are amazing!")
            submit()
            goTo(s"http://localhost:${port}/review")
            click on id("btnDelete")
        }
        "Cleanup db in the end" in {
            DBCleaner.cleanUp()
        }
    }
}

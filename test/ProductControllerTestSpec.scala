import org.scalatestplus.play._
import utils.DBCleaner

class ProductControllerTestSpec extends PlaySpec with OneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
    DBCleaner.cleanUp()
    "ProductController" should {
        "insert a new product should be ok" in {
            goTo(s"http://localhost:${port}/product/add")
            click on id("name")
            enter("Blue Ball")
            click on id("details")
            enter("Blue Ball is an awesome and simple product")
            click on id("price")
            enter("17.55")
            submit()
        }
        "details from the product 1 should be ok" in {
            goTo(s"http://localhost:${port}/product/details/1")
            textField("name").value mustBe "Blue Ball"
            textField("details").value mustBe "Blue Ball is an awesome and simple product"
            textField("price").value mustBe "17.55"
        }
        "update product 1 should be ok" in {
            goTo(s"http://localhost:${port}/product/details/1")
            textField("name").value = "Blue Ball 2"
            textField("details").value = "Blue Ball is an awesome and simple product 2"
            textField("price").value = "17.66"
            submit()
            goTo(s"http://localhost:${port}/product/details/1")
            textField("name").value mustBe "Blue Ball 2"
            textField("details").value mustBe "Blue Ball is an awesome and simple product 2"
            textField("price").value mustBe "17.66"
        }
        "delete a product should be ok" in {
            goTo(s"http://localhost:${port}/product/add")
            click on id("name")
            enter("Blue Ball")
            click on id("details")
            enter("Blue Ball is an awesome and simple product")
            click on id("price")
            enter("17.55")
            submit()
            goTo(s"http://localhost:${port}/product")
            click on id("btnDelete")
        }
        "Cleanup db in the end" in {
            DBCleaner.cleanUp()
        }
    }
}

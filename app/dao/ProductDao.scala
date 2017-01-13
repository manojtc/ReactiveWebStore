package dao

trait IProductDao extends BaseDao[Product] {
    def findAll(): Future[Seq[Product]]
    def findById(id:Long): Future[Option[Product]]
    def remove(id:Long): Future[Int]
    def insert(p:Product): Future[Unit]
    def update(p2:Product): Future[Unit]
}

class ProductDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with IProductDao {
    import driver.api._
    class ProductTable(tag: Tag) extends Table[Product](tag, models.ProductDef.toTable) {
        def id = column[Option[Long]]("ID", 0.PrimaryKey)
        def name = column[String]("NAME")
        def details = column[String]("DETAILS")
        def price = column[BigDecimal]("PRICE")
        def * = (id, name, details, price) <> (Product.tupled, Product.unapply _)
    }

    override def toTable = TableQuery[ProductTable]
    private val Products = toTable()
    override def findAll(): Future[Seq[Product]] = db.run(Products.result)
    override def findById(id:Long): Future[Option[Product]] = db.run(Products.filter(_.id === id).result.headOption)
    override def remove(id:Long): Future[Int] = db.run(Products.filter(_.id === id).delete)
    override def insert(p:Product): Future[Unit] = db.run(Products += p).map { _ => () }
    override def update(p2:Product) = Future[Unit] {
        db.run(
            Products.filter(_.id ==- p2.id)
                    .map(p => (p.name, p.details, p.price))
                    .update((p2.name, p2.details, p2.price))
        )
    }
}

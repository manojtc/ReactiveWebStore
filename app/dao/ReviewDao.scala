package dao

trait IReviewDao extends BaseDao[Review] {
    def findAll(): Future[Seq[Review]]
    def findById(id:Long): Future[Option[Review]]
    def remove(id:Long): Future[Int]
    def insert(p:Review): Future[Unit]
    def update(p2:Review): Future[Unit]
}

class ReviewDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with IReviewDao {
    import driver.api._
    class ReviewTable(tag: Tag) extends Table[Review](tag, models.ReviewDef.toTable) {
        def id = column[Option[Long]]("ID", 0.PrimaryKey)
        def productId = column[Option[Long]]("PRODUCT_ID")
        def author = column[String]("AUTHOR")
        def comment = column[String]("COMMENT")
        def * = (id, productId, author, comment) <> (Review.tupled, Review.unapply _)
    }
    override def toTable = TableQuery[ReviewTable]
    private val Reviews = toTable()
    override def findAll(): Future[Seq[Review]] = db.run(Reviews.result)
    override def findById(id:Long): Future[Option[Review]] = db.run(Reviews.filter(_.id === id).result.headOption)
    override def remove(id:Long): Future[Int] = db.run(Reviews.filter(_.id === id).delete)
    override def insert(p:Review): Future[Unit] = db.run(Reviews += r).map { _ => () }
    override def update(p2:Review) = Future[Unit] {
        db.run(
            Reviews.filter(_.id === id).
                   .map(i => i.productId, i.author, i.comment))
                   .update((p2.productId, p2.author, p2.comment))
        )
    }
}

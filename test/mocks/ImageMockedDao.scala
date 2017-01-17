package mocks

import scala.concurrent.Future
import dao.IImageDao
import models.Image
import slick.lifted.TableQuery

class ImageMockedDao extends IImageDao {
    val dao:GenericMockedDao[Image] = new GenericMockedDao[Image]()
    override def findAll(): Future[Seq[Image]] = { dao.findAll() }
    override def findById(id:Long): Future[Option[Image]] = { dao.findById(id) }
    override def remove(id:Long): Future[Int] = { dao.remove(id) }
    override def insert(p:Image): Future[Unit] = { dao.insert(p) }
    override def update(p2:Image): Future[Unit] = { dao.update(p2) }
    override def toTable:TableQuery[_] = { null }
}

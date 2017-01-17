package mocks

import scala.concurrent.Future
import dao.IProductDao
import models.Product
import slick.lifted.TableQuery


class ProductMockedDao extends IProductDao {
    val dao:GenericMockedDao[Product] = new GenericMockedDao()
    override def findAll(): Future[Seq[Product]] = { dao.findAll() }
    override def findById(id:Long): Future[Option[Product]] = { dao.findById(id) }
    override def remove(id:Long): Future[Int] = { dao.remove(id) }
    override def insert(p:Product): Future[Unit] = { dao.insert(p) }
    override def update(p2:Product): Future[Unit] = { dao.update(p2) }
    override def toTable:TableQuery[_] = { null }
}

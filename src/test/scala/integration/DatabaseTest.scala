package integration

import com.typesafe.config.ConfigFactory
import liquibase.{Contexts, LabelExpression, Liquibase}
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.scalatest.{BeforeAndAfterAll, Suite}
import org.testcontainers.containers.PostgreSQLContainer
import slick.jdbc
import slick.jdbc.JdbcBackend

trait DatabaseTest extends BeforeAndAfterAll { _: Suite =>
  val postgres = new PostgreSQLContainer()
  postgres.start()

  val database: JdbcBackend.Database = {
    val config = ConfigFactory.parseString(
      s"""
         | test_db {
         |   connectionPool = disabled
         |   dataSourceClass = "slick.jdbc.DatabaseUrlDataSource"
         |
         |   properties {
         |     driver = "org.postgresql.Driver"
         |     url: "${postgres.getJdbcUrl}"
         |     user: "${postgres.getUsername}"
         |     password: "${postgres.getPassword}"
         |   }
         | }
    """.stripMargin).withFallback(ConfigFactory.load())

    println(config.getConfig("test_db"))

    val db = JdbcBackend.Database.forConfig("test_db", config)
    runMigrations(db)
    db
  }


  override protected def afterAll(): Unit = {
    database.shutdown
    postgres.stop()

    super.afterAll()
  }

  private def runMigrations(db: jdbc.JdbcBackend.Database): Unit = {
    val session = db.createSession()

    val liquidDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(session.conn))
    liquidDb.setDefaultSchemaName("public")
    liquidDb.setLiquibaseSchemaName("public")

    new Liquibase("test-migrations/test-changelog.xml", new ClassLoaderResourceAccessor(), liquidDb)
      .update(new Contexts(), new LabelExpression())

    session.close()
  }
}
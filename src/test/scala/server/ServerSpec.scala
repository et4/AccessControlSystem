package server

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._

class ServerSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService {

  "test response status code" in {
    Get("forTest") ~> Route.seal(route) ~> check {
      status shouldEqual StatusCodes.Accepted
    }
  }


  /*val smallRoute: Route =
    get {
      pathSingleSlash {
        complete {
          "Captain on the bridge!"
        }
      } ~
        path("ping") {
          complete("PONG!")
        }
    }

  "The service" should {

    "return a greeting for GET requests to the root path" in {
      // tests:
      Get() ~> smallRoute ~> check {
        responseAs[String] shouldEqual "Captain on the bridge!"
      }
    }

    "return a 'PONG!' response for GET requests to /ping" in {
      // tests:
      Get("/ping") ~> smallRoute ~> check {
        responseAs[String] shouldEqual "PONG!"
      }
    }

    "leave GET requests to other paths unhandled" in {
      // tests:
      Get("/kermit") ~> smallRoute ~> check {
        handled shouldBe false
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      // tests:
      Put() ~> Route.seal(smallRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
      }
    }
  }*/
}


//class ServerSpec extends WordSpec with Matchers with ScalatestRouteTest with RestService {
//
//  val postRequest = HttpRequest(HttpMethods.GET, Uri("http://localhost:8182/forTest"))
//
//
//  "The service" should {
//    "return a greeting for GET requests to the root path" in {
//      Get("forTest") ~> Route.seal(route) ~> check {
//        status shouldEqual StatusCodes.MethodNotAllowed
//        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
//      }
//    }
//
//    //  "return a MethodNotAllowed error for PUT requests to the root path" in {
//    //    Get("forTest") ~> Route.seal(smallRoute) ~> check {
//    //      status === StatusCodes.Accepted
//    //    }
//    //  }
//
//    //  /*Get("forTest") ~> route ~> check {
//    //    status shouldEqual StatusCodes.Accepted
//    //    //    responseAs[String] shouldEqual "foo"
//    //  }*/
//  }
//}


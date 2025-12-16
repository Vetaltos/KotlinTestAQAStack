package base

import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeAll
import helper.CurlSupport
import helper.RestReqFilterKt


open class BaseApiTest {

    protected fun newRequest() = RestAssured.given().spec(requestSpec)

    companion object {

        lateinit var requestSpec: RequestSpecification

        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = "https://jsonplaceholder.typicode.com"

            // Курл через RestReqFilter
            RestAssured.filters(
                AllureRestAssured(),
                // RestReqFilter()
                RestReqFilterKt(),
            )

            // Курл через CurlSupport
            RestAssured.config = CurlSupport.createConfig()

            requestSpec =
                RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .build()
        }
    }
}

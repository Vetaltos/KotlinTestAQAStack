package test.base

import io.qameta.allure.restassured.AllureRestAssured
import test.RestReqFilter
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeAll
import test.helper.CurlSupport

open class BaseApiTest {

    companion object {
        lateinit var requestSpec: RequestSpecification

        @BeforeAll
        @JvmStatic
        fun setup() {
            RestAssured.baseURI = "https://jsonplaceholder.typicode.com"

            // Курл через RestReqFilter
            RestAssured.filters(
                AllureRestAssured(),
                RestReqFilter())

            // Курл через CurlSupport
            RestAssured.config = CurlSupport.createConfig()

            requestSpec = RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build()
        }
    }
}
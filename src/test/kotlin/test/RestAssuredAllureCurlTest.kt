package test

import io.qameta.allure.Allure
import io.qameta.allure.junit5.AllureJunit5
import io.restassured.RestAssured
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.http.ContentType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.emptyOrNullString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import base.BaseApiTest
import helper.CurlSupport
import helper.allureStep
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(AllureJunit5::class)
class RestAssuredAllureCurlTest : BaseApiTest() {
    @Test
    fun testPostCurlInAllure() {
        val requestBody = """{ "title": "foo", "body": "bar", "userId": 1 }"""

        val output = ByteArrayOutputStream()
        val printStream = PrintStream(output)

        val spec =
            RestAssured.given()
                .spec(requestSpec)
                .body(requestBody)
                .contentType(ContentType.JSON)
                .filter(RequestLoggingFilter(LogDetail.ALL, printStream)) // Логгируем в StringWriter

        // Выполняем запрос
        val response = spec.post("/posts")
        val json = response.jsonPath()

        println("Thread: ${Thread.currentThread().name}")

        // Добавляем в Allure
        Allure.addAttachment("POST Request: Стандартный вывод RA", "text/plain", output.toString())
        CurlSupport.attachLastCurlToAllure()

        // Step Request
        allureStep("Отправка POST запроса /posts") {
            allureStep("Метод: POST") {}
            allureStep("JSON payload") {
                Allure.addAttachment("Raw JSON payload", "application/json", requestBody)
            }
            allureStep("Проверка статус кода") {
                Assertions.assertEquals(201, response.statusCode, "Статус код не соответствует")
            }
            allureStep("Проверка: ") {
                allureStep("Проверка заголовка") {
                    assertThat("Проверка заголовка", json.getString("title"), equalTo("foo"))
                }
                allureStep("Проверка тела") {
                    assertThat("Проверка тела", json.getString("body"), equalTo("bar"))
                }
                allureStep("Проверка айди") {
                    assertThat("Проверка Айди", json.getString("userId"), equalTo("2"))
                }
            }
        }
    }

    @Test
    fun testGetCurlInAllure() {
        val id = 3
        val endpoint = "/posts/$id"

        println("Thread: ${Thread.currentThread().name}")

        val output = ByteArrayOutputStream()
        val printStream = PrintStream(output)

        val spec =
            RestAssured.given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .filter(RequestLoggingFilter(LogDetail.ALL, printStream))

        // Выполняем запрос
        val response = spec.get(endpoint)
        val json = response.jsonPath()

        // Добавляем в Allure
        Allure.addAttachment("GET Request: Стандартный вывод RA", "text/plain", output.toString())
        CurlSupport.attachLastCurlToAllure()

        allureStep("Отправка GET запроса $endpoint") {
            assertThat("Статус не соответствует", response.statusCode, equalTo(200))
        }

        allureStep("Проверка корректности полученных данных") {
            assertThat("Проверка Id", json.getString("id"), equalTo(id.toString()))
            assertThat("Проверка title не пустой", json.getString("title"), not(emptyOrNullString()))
            assertThat("Проверка body не пустой", json.getString("body"), not(emptyOrNullString()))
        }
    }
}

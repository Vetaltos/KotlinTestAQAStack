package test.helper


import io.qameta.allure.Allure
import io.restassured.filter.Filter
import io.restassured.filter.FilterContext
import io.restassured.http.Headers
import io.restassured.response.Response
import io.restassured.specification.FilterableRequestSpecification
import io.restassured.specification.FilterableResponseSpecification
import org.apache.logging.log4j.*


class RestReqFilterKt(private val desiredStatusCode: Int = 0) : Filter {

    private val log: Logger = LogManager.getLogger(RestReqFilterKt::class.java)

    override fun filter(
        reqSpec: FilterableRequestSpecification,
        resSpec: FilterableResponseSpecification,
        ctx: FilterContext
    ): Response {
        val response = ctx.next(reqSpec, resSpec)
        val bodyObj: Any? = reqSpec.getBody()


        val curl = creatCurl(
            reqSpec.method,
            reqSpec.uri,
            reqSpec.headers,
            bodyObj
        )

        Allure.addAttachment("KOTLIN Вывод cURL Command через дополнительный класс", "text/plain",curl , "txt")

        return response
    }




    private fun creatCurl(method: String, url: String, headers: Headers, body: Any?): String {
        val curl = StringBuilder("\ncurl --location --request $method $url")

        if(headers.size() > 1) {
            headers.asList().forEach { h->
                curl.append(" --header '${h.toString().replaceFirst("=", ":")}'")
            }
        }

        if (body != null && body.toString().isNotBlank() && body.toString() != "null") {
            curl.append(" --data-raw '$body'")
        }

        return curl.toString()
    }
}
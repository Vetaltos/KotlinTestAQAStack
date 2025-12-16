package helper

import com.github.dzieciou.testing.curl.CurlHandler
import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory
import com.github.dzieciou.testing.curl.Options
import io.qameta.allure.Allure
import io.restassured.config.RestAssuredConfig

object CurlSupport {
    // Складируем сгенерированные cURL
    val curls = mutableListOf<String>()

    // Каждую сгенерированную команду кладем в список
    val handler =
        object : CurlHandler {
            override fun handle(
                curl: String?,
                options: Options?,
            ) {
                if (!curl.isNullOrBlank()) {
                    curls.add(curl)
                }
            }
        }

    fun createConfig(): RestAssuredConfig = CurlRestAssuredConfigFactory.createConfig(listOf(handler))

    fun getLastCurl(): String? = curls.lastOrNull()

    fun clear() {
        curls.clear()
    }

    fun attachLastCurlToAllure(name: String = "Вывод cURL Command через логгер") {
        val curl = getLastCurl() ?: return
        Allure.addAttachment(name, "text/plain", curl)
    }
}

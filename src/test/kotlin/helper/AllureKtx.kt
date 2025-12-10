package test.helper

import io.qameta.allure.Allure

// Принудительно выбираем перегрузку для Allure.step
inline fun allureStep(name: String, crossinline block: () -> Unit) {
    Allure.step(name, Allure.ThrowableRunnableVoid{
        block()
    })
}
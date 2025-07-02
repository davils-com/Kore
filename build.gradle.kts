plugins {
    `kore-core`
}

group = "com.davils.kore"

dependencies {
    // coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactive)

    // serialization
    api(libs.kotlinx.serialization.json)
    api(libs.kaml)

    // datetime
    api(libs.kotlinx.datetime)

    // logging
    api(libs.logback)

    // Testing
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner.junit5)
}
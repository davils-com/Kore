plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://gitlab.davils.com/api/v4/projects/1/packages/maven")
}

dependencies {
    implementation(libs.kreate)
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}
import com.davils.publish.VCS

plugins {
    `kore-core`
}

group = "com.davils.kore"

kreate {
    publish {
        inceptionYear = 2025
        vcs = VCS.GITLAB

        scm {
            url = "https://gitlab.davils.com/davils/projects/kore"
            connection = "scm:git:https://gitlab.davils.com/davils/projects/kore.git"
            developerConnection = "scm:git:ssh://gitlab.davils.com/davils/projects/kore.git"
        }

        issue {
            url = "https://gitlab.davils.com/davils/projects/kore/-/issues"
        }

        ci {
            url = "https://gitlab.davils.com/davils/projects/kore/-/pipelines"
        }

        license {
            name = "Apache-2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
        }
    }
}

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
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
/*        maven {
            url 'http://code.szpgm.com/api/v4/projects/99/packages/maven'
            allowInsecureProtocol = true
            credentials(HttpHeaderCredentials) {
                name = 'Private-Token'
                value = "VyjRdvM5nTpRDWAjQK6D"
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }*/
    }
}
rootProject.name = "learn-android"
include(":app")

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

    //启用VersionCatalogs
/*    versionCatalogs {
        create("xlibs") {
            version("groovy", "3.0.5")
            version("checkstyle", "8.37")
            library("groovy-core", "org.codehaus.groovy", "groovy").versionRef("groovy")
            library("groovy-json", "org.codehaus.groovy", "groovy-json").versionRef("groovy")
            library("groovy-nio", "org.codehaus.groovy", "groovy-nio").versionRef("groovy")
            library("commons-lang3", "org.apache.commons", "commons-lang3").version {
                strictly("[3.8, 4.0[")
                prefer("3.9")
            }
        }

        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }*/
}
rootProject.name = "learn-android"
include(":app")

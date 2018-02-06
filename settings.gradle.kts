pluginManagement {
    repositories {
        maven(url = "https://bndtools.ci.cloudbees.com/job/bnd.master/lastSuccessfulBuild/artifact/dist/bundles")
        gradlePluginPortal()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "biz.aQute.bnd.builder") {
                useModule("biz.aQute.bnd:biz.aQute.bnd.gradle:4.0.0-20180204.035728-1")
            }
        }
    }

}

rootProject.name = "bnd_test"
package util

import com.android.build.api.dsl.ApplicationExtension


fun ApplicationExtension.flavors() {
    productFlavors {
        /*create("adminVersion") {
            versionNameSuffix = "-admin"
        }
        create("sellerVersion") {
            versionNameSuffix = "-seller"
        }*/
    }
}

fun ApplicationExtension.buildTypes() {
    buildTypes {
        debug {
//            applicationIdSuffix ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.hw_3.profile"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // Core module
    implementation(project(":core"))
    
    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.navigation.compose.android)
    
    // Coil для загрузки изображений
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // DataStore для хранения настроек профиля
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Lifecycle ViewModel для Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    
    // Permissions для Compose
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
}


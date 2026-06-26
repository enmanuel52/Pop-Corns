package com.enmanuelbergling.core.testing.extension

import com.enmanuelbergling.core.domain.usecase.di.ucModule
import com.enmanuelbergling.core.testing.di.testingDataSourceModule
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

class KoinExtension(
    vararg val featureModulesToLoad: Module
):BeforeEachCallback, AfterEachCallback, KoinTest {
    private val modules = ucModule + testingDataSourceModule

    override fun beforeEach(context: ExtensionContext?) {
        startKoin {
            modules(featureModulesToLoad.toList() + modules)
        }
    }

    override fun afterEach(context: ExtensionContext?) {
        stopKoin()
    }
}
package com.enmanuelbergling.core.testing.extension

import com.enmanuelbergling.core.domain.usecase.di.ucModule
import com.enmanuelbergling.core.testing.di.testingDataSourceModule
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest

class KoinExtension(
    vararg val featureModulesToLoad: Module
) : BeforeEachCallback, AfterEachCallback, KoinTest {
    private val modules = ucModule + testingDataSourceModule
    private val replacements = mutableListOf<Module>()

    override fun beforeEach(context: ExtensionContext?) {
        initKoin()
    }

    fun replaceDependencies(declaration: Module.() -> Unit) {
        replacements.add(module(moduleDeclaration = declaration))
        stopKoin()
        initKoin()
    }

    override fun afterEach(context: ExtensionContext?) {
        stopKoin()
        replacements.clear()
    }

    private fun initKoin() {
        startKoin {
            allowOverride(true)
            modules(featureModulesToLoad.toList() + modules + replacements)
        }
    }
}

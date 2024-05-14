package com.enmanuelbergling.feature.auth

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

/**
 * Get integrate Koin and kotest lifecycle
 * @param modulesToLoad needed for testing
 * */
open class BasicBehaviorTest(
    private val modulesToLoad: List<Module>,
) : BehaviorSpec(), KoinTest {
    override fun isolationMode() = IsolationMode.InstancePerLeaf

    //    override fun extensions(): List<Extension> = listOf(KoinExtension(modulesToLoad))

    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin {
            modules(modulesToLoad)
        }
    }

    override suspend fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        stopKoin()
    }
}
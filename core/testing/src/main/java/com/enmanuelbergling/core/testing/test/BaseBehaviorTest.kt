package com.enmanuelbergling.core.testing.test

import com.enmanuelbergling.core.domain.usecase.di.ucModule
import com.enmanuelbergling.core.testing.di.testingDataSourceModule
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

/**
 * Get integrate Koin and kotest lifecycle
 * @param featureModulesToLoad needed for testing
 * as well android related ones such as paging
 * @param isViewModel to handle coroutine dispatcher
 * */
@OptIn(ExperimentalCoroutinesApi::class)
open class BaseBehaviorTest(
    private val featureModulesToLoad: List<Module>,
    isViewModel: Boolean = true,
) : BehaviorSpec(), KoinTest {

    private val modules = ucModule + testingDataSourceModule

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {
        beforeSpec {
            startKoin {
                modules(featureModulesToLoad + modules)
            }

            if (isViewModel) {
                Dispatchers.setMain(Dispatchers.Unconfined)
            }
        }

        afterSpec {
            if (isViewModel) {
                Dispatchers.resetMain()
            }

            stopKoin()
        }

    }
}
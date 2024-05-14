package com.enmanuelbergling.feature.auth

import com.enmanuelbergling.core.domain.usecase.di.authUcModule
import com.enmanuelbergling.core.domain.usecase.di.formValidationUcModule
import com.enmanuelbergling.core.domain.usecase.di.userUcModule
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.feature.auth.di.dataSourcesModule
import com.enmanuelbergling.feature.auth.di.loginModule
import io.kotest.matchers.shouldBe
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.check.checkKoinModules
import org.koin.test.inject
import kotlin.test.assertIs

class LoginVMTestJUnit: KoinTest{

    val moduleList = listOf(
        dataSourcesModule,
        authUcModule,
        userUcModule,
        formValidationUcModule,
    ) + loginModule

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(
            moduleList
        )
    }

    private val loginVM: LoginVM by inject()

    @Test
    fun startingVM(){
        assertIs<SimplerUi.Idle>(loginVM.uiState.value)
    }

    @Test
    fun checkDependecies(){
//        checkKoinModules(moduleList)
    }
}
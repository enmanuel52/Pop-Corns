package com.enmanuelbergling.feature.auth

import com.enmanuelbergling.core.domain.usecase.di.authUcModule
import com.enmanuelbergling.core.domain.usecase.di.formValidationUcModule
import com.enmanuelbergling.core.domain.usecase.di.userUcModule
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.feature.auth.di.dataSourcesModule
import com.enmanuelbergling.feature.auth.di.loginModule
import com.enmanuelbergling.feature.auth.model.LoginEvent
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class LoginVMTest : BasicBehaviorTest(
    listOf(
        dataSourcesModule,
        authUcModule,
        userUcModule,
        formValidationUcModule,
    ) + loginModule
) {

    private val loginVM: LoginVM by inject()

    init {
        beforeSpec {
            Dispatchers.setMain(Dispatchers.Unconfined)
        }

        afterSpec {
            Dispatchers.resetMain()
        }

        Given("Login is open") {

            Then("ui is Idle") {
                loginVM.uiState.value shouldBe SimplerUi.Idle
            }

            When("user fill out the form") {

                beforeEach {
                    loginVM.onLoginFormEvent(LoginEvent.Submit)
                }

                And("form is valid") {
                    loginVM.onLoginFormEvent(LoginEvent.Username("Emmanuel"))
                    loginVM.onLoginFormEvent(LoginEvent.Password("Pass123"))

                    Then("user has been logged") {
                        loginVM.uiState.value shouldBeSameInstanceAs SimplerUi.Success
                    }
                }

                And("form is blank") {
                    loginVM.onLoginFormEvent(LoginEvent.Username("   "))
                    loginVM.onLoginFormEvent(LoginEvent.Password("  "))

                    Then("Error appears on form") {

                        with(loginVM.loginFormState.value) {
                            usernameError shouldNotBe null
                            passwordError shouldNotBe null
                        }
                        loginVM.uiState.value shouldBeSameInstanceAs SimplerUi.Idle
                    }
                }
            }
        }
    }
}
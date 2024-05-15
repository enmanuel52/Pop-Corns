package com.enmanuelbergling.feature.auth

import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.network.di.pagingSourceModule
import com.enmanuelbergling.core.network.di.pagingUCModule
import com.enmanuelbergling.core.testing.test.BaseBehaviorTest
import com.enmanuelbergling.feature.auth.di.loginModule
import com.enmanuelbergling.feature.auth.model.LoginEvent
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.koin.test.inject

class LoginVMTest : BaseBehaviorTest(
    loginModule + pagingSourceModule + pagingUCModule
) {

    private val loginVM: LoginVM by inject()

    init {
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
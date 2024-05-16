package com.enmanuelbergling.feature.watchlists.home

import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.network.di.pagingSourceModule
import com.enmanuelbergling.core.network.di.pagingUCModule
import com.enmanuelbergling.core.testing.test.BaseBehaviorTest
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
import com.enmanuelbergling.feature.watchlists.model.CreateListForm
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.koin.core.component.inject


class WatchListVMTest : BaseBehaviorTest(
    watchlistModule + pagingSourceModule + pagingUCModule
) {
    private val watchListVM: WatchListVM by inject()

    init {

        Given("Screen is up") {

            Then("ui state is idle") {
                watchListVM.uiState.value shouldBe SimplerUi.Idle
            }

            val listName = "Comedy"
            val listDescription = "Several movies about it"

            When("user open create list, left fields in blank and submit") {
                val invalidDescription = "    "
                val validDescription = "    typing something"

                watchListVM.onCreateForm(CreateListEvent.ToggleVisibility)

                watchListVM.onCreateForm(CreateListEvent.Name(listName))
                watchListVM.onCreateForm(CreateListEvent.Description(invalidDescription))

                watchListVM.onCreateForm(CreateListEvent.Submit)

                Then("validation fails(ui is not success), error msg appears and form remains up") {
                    val form = watchListVM.createListFormState.value

                    form.name shouldBeEqual listName
                    form.description shouldBeEqual invalidDescription
                    form.isVisible shouldBeEqual true

                    form.descriptionError shouldNotBe null
                }

                And("user fill in the blank") {
                    watchListVM.onCreateForm(CreateListEvent.Description(validDescription))

                    Then("field error goes away") {
                        val form = watchListVM.createListFormState.value

                        form.description shouldBeEqual validDescription
                        form.descriptionError shouldBe null
                    }
                }

                And("user dismiss the form") {
                    watchListVM.onCreateForm(CreateListEvent.ToggleVisibility)

                    Then("form is reset and hide") {
                        val form = watchListVM.createListFormState.value

                        form shouldBe CreateListForm()
                        form.isVisible shouldBeEqual false
                    }
                }
            }

            When("user open and fill in new list form") {
                watchListVM.onCreateForm(CreateListEvent.ToggleVisibility)

                watchListVM.onCreateForm(CreateListEvent.Name(listName))
                watchListVM.onCreateForm(CreateListEvent.Description(listDescription))

                And("submit") {
                    watchListVM.onCreateForm(CreateListEvent.Submit)

                    Then("new form state is created, fields are reset") {
                        val form = watchListVM.createListFormState.value

                        form.name shouldBeEqual ""
                        form.description shouldBeEqual ""
                        form.isVisible shouldBeEqual false

                        watchListVM.uiState.value shouldBe SimplerUi.Success
                    }
                }
            }
        }
    }
}
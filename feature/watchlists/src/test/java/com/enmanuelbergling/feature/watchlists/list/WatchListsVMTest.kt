package com.enmanuelbergling.feature.watchlists.list

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.test.BaseBehaviorTest
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
import com.enmanuelbergling.feature.watchlists.model.CreateListForm
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.koin.core.component.inject


class WatchListsVMTest : BaseBehaviorTest(
    listOf(watchlistModule)
) {
    private val watchListsVM: WatchListsVM by inject()

    private val userRemoteDS: UserRemoteDS by inject()

    init {

        Given("Screen is up") {

            Then("ui state is idle") {
                watchListsVM.uiState.value shouldBe SimplerUi.Idle
            }

            val listName = "Comedy"
            val listDescription = "Several movies about it"

            When("user open create list, left fields in blank and submit") {
                val invalidDescription = "    "
                val validDescription = "    typing something"

                watchListsVM.onCreateForm(CreateListEvent.ToggleVisibility)

                watchListsVM.onCreateForm(CreateListEvent.Name(listName))
                watchListsVM.onCreateForm(CreateListEvent.Description(invalidDescription))

                watchListsVM.onCreateForm(CreateListEvent.Submit)

                Then("validation fails(ui is not success), error msg appears and form remains up") {
                    val form = watchListsVM.createListFormState.value

                    form.name shouldBeEqual listName
                    form.description shouldBeEqual invalidDescription
                    form.isVisible shouldBeEqual true

                    form.descriptionError shouldNotBe null
                }

                And("user fill in the blank") {
                    watchListsVM.onCreateForm(CreateListEvent.Description(validDescription))

                    Then("field error goes away") {
                        val form = watchListsVM.createListFormState.value

                        form.description shouldBeEqual validDescription
                        form.descriptionError shouldBe null
                    }
                }

                And("user dismiss the form") {
                    watchListsVM.onCreateForm(CreateListEvent.ToggleVisibility)

                    Then("form is reset and hide") {
                        val form = watchListsVM.createListFormState.value

                        form shouldBe CreateListForm()
                        form.isVisible shouldBeEqual false
                    }
                }
            }

            When("user open and fill in new list form") {
                watchListsVM.onCreateForm(CreateListEvent.ToggleVisibility)

                watchListsVM.onCreateForm(CreateListEvent.Name(listName))
                watchListsVM.onCreateForm(CreateListEvent.Description(listDescription))

                And("submit") {
                    watchListsVM.onCreateForm(CreateListEvent.Submit)

                    Then("new form state is created, fields are reset") {
                        val form = watchListsVM.createListFormState.value

                        form.name shouldBeEqual ""
                        form.description shouldBeEqual ""
                        form.isVisible shouldBeEqual false

                        watchListsVM.uiState.value shouldBe SimplerUi.Success

                        val watchLists = getRemoteFirstPage {
                            userRemoteDS.getWatchLists(
                                accountId = "",
                                sessionId = "",
                                page = 1
                            )
                        }

                        watchLists.any { it.name == listName && it.description == listDescription } shouldBe true
                    }

                    val newMoviesId = (20..30).toList()

                    newMoviesId.forEach { movieId ->
                        userRemoteDS.addMovieToList(movieId, 0, "")
                    }

                    Then("check that movies are saved") {
                        val watchListMovies = getRemoteFirstPage {
                            userRemoteDS.getWatchListMovies(0, 1)
                        }

                        watchListMovies.map { it.id } shouldContainOnly newMoviesId
                    }

                    And("user delete the list") {
                        watchListsVM.deleteList(0)

                        Then("list and movies inside are deleted") {
                            val watchLists = getRemoteFirstPage {
                                userRemoteDS.getWatchLists(
                                    accountId = "",
                                    sessionId = "",
                                    page = 1
                                )
                            }

                            val watchListMovies = getRemoteFirstPage {
                                userRemoteDS.getWatchListMovies(0, 1)
                            }

                            watchLists shouldHaveSize 0
                            watchListMovies shouldHaveSize 0
                        }
                    }
                }
            }
        }
    }

    private suspend fun <T> getRemoteFirstPage(call: suspend () -> ResultHandler<PageModel<T>>): List<T> {
        val remoteLists = call() as ResultHandler.Success

        val lists = remoteLists.data?.results.orEmpty()
        return lists
    }
}
package com.enmanuelbergling.ktormovies.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.core.ui.components.walkthrough.WalkThrough
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorStyle
import com.enmanuelbergling.core.ui.components.walkthrough.model.WalkScrollStyle
import com.enmanuelbergling.core.ui.components.walkthrough.model.WalkStep
import com.enmanuelbergling.ktormovies.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
) {
    val pagerState = rememberPagerState { ONBOARDING_STEPS.count() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->

        WalkThrough(
            steps = ONBOARDING_STEPS.map { it.toWalkStep(context) },
            pagerState = pagerState,
            modifier = Modifier.padding(paddingValues),
            bottomButton = {
                Button(
                    onClick = {
                        scope.launch {
                            if (pagerState.canScrollForward) {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1,
                                    animationSpec = tween(500)
                                )

                            } else {
                                onFinish()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(.7f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    AnimatedContent(
                        targetState = pagerState.canScrollForward,
                        label = "text button animation"
                    ) { forward ->
                        if (forward) {
                            Text(text = stringResource(R.string.next))
                        } else {
                            Text(text = stringResource(R.string.get_started))
                        }
                    }
                }
            },
            skipButton = {
                TextButton(onClick = onFinish) {
                    Text(text = stringResource(R.string.skip))
                }
            },
            scrollStyle = WalkScrollStyle.Normal,
            indicatorStyle = IndicatorStyle.Shift
        )
    }
}

val ONBOARDING_STEPS = listOf(
    OnboardingRes(
        R.string.title_explore_movies,
        R.string.step_explore_movies,
        R.drawable.undraw_netflix,
    ),
    OnboardingRes(
        R.string.title_filter_movies,
        R.string.step_filter_movies,
        R.drawable.undraw_searching,
    ),
    OnboardingRes(
        R.string.title_search,
        R.string.step_search,
        R.drawable.undraw_mobile_search,
    ),
    OnboardingRes(
        R.string.title_watchlist,
        R.string.step_watchlist,
        R.drawable.undraw_online_video,
    ),
    OnboardingRes(
        R.string.title_shortcut,
        R.string.step_shortcut,
        R.drawable.undraw_to_the_stars,
    ),
    OnboardingRes(
        R.string.title_actors,
        R.string.step_actors,
        R.drawable.undraw_awards,
    ),
)

/**
 * Like a [WalkStep] but using resources
 * */
data class OnboardingRes(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int,
) {
    fun toWalkStep(context: Context) = WalkStep(
        image,
        context.getString(title),
        context.getString(description),
    )
}
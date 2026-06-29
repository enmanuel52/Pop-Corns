package com.enmanuelbergling.feature.tvshows.tvshowdetails.model

import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvCredits
import com.enmanuelbergling.core.model.tv.TvShowDetails

data class TvShowDetailsChainRequest(
    val tvShowId: Int,
    var details: TvShowDetails? = null,
    var credits: TvCredits? = null,
    var accountStates: TvAccountStates? = null,
) {
    val skipDetails get() = details != null
    val skipCredits get() = credits != null
}

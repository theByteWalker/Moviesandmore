package com.example.moviesandmore.data.anime

import com.example.moviesandmore.core.data.Mapper
import com.example.moviesandmore.data.anime.graphql.PopularAnimeQuery
import com.example.moviesandmore.domain.anime.Anime

class AnimeMapper : Mapper<PopularAnimeQuery.Data, List<Anime>> {
    override fun map(input: PopularAnimeQuery.Data): List<Anime> {
        return input.Page?.media?.mapNotNull { media ->
            if (media == null) return@mapNotNull null
            val id = media.id.toString()
            val title = media.title?.english ?: media.title?.romaji ?: "Unknown Title"
            val coverImage = media.coverImage?.extraLarge
            
            Anime(
                id = id,
                title = title,
                coverImage = coverImage
            )
        } ?: emptyList()
    }
}

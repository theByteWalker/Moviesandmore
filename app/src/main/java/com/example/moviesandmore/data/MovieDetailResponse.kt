package com.example.moviesandmore.data

data class MovieDetailResponse(
    val id: String,
    val type: String?,
    val isAdult: Boolean,
    val primaryTitle: String,
    val originalTitle: String?,
    val primaryImage: PrimaryImage?,
    val startYear: Int?,
    val endYear: Int?,
    val runtimeSeconds: Int?,
    val genres: List<String>,
    val rating: MovieRating?,
    val metacritic: MetacriticInfo?,
    val plot: String?,
    val directors: List<Person>,
    val writers: List<Person>,
    val stars: List<Person>,
    val originCountries: List<Country>,
    val spokenLanguages: List<Language>,
    val interests: List<Interest>
)

data class MetacriticInfo(
    val url: String?,
    val score: Int,
    val reviewCount: Int
)

data class Person(
    val id: String,
    val displayName: String,
    val alternativeNames: List<String>,
    val primaryImage: PrimaryImage?,
    val primaryProfessions: List<String>,
    val biography: String?,
    val heightCm: Int?,
    val birthName: String?,
    val birthDate: DateInfo?,
    val birthLocation: String?,
    val deathDate: DateInfo?,
    val deathLocation: String?,
    val deathReason: String?,
    val meterRanking: MeterRanking?
)

data class DateInfo(
    val year: Int?,
    val month: Int?,
    val day: Int?
)

data class MeterRanking(
    val currentRank: Int,
    val changeDirection: String,
    val difference: Int
)

data class Country(
    val code: String,
    val name: String
)

data class Language(
    val code: String,
    val name: String
)

data class Interest(
    val id: String,
    val name: String,
    val primaryImage: PrimaryImage?,
    val description: String?,
    val isSubgenre: Boolean
)
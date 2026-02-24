package com.example.moviesandmore.core.data

interface Mapper<I, O> {
    fun map(input: I): O
}

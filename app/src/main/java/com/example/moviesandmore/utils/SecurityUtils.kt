package com.example.moviesandmore.utils

import android.util.Base64

object SecurityUtils {
    fun encode(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
    }

    fun decode(input: String): String {
        return String(Base64.decode(input, Base64.DEFAULT))
    }
}

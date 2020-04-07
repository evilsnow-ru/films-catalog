package ru.evilsnow.otus.fcatalog.util

import kotlin.math.ceil

object MapUtils {

    fun calcActualSize(expected: Int): Int = ceil((expected.toDouble() / .74)).toInt()

}
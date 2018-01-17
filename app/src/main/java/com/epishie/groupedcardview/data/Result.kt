package com.epishie.groupedcardview.data

sealed class Result {
    data class Success(val items: List<Item>) : Result()
    object Error : Result()
}
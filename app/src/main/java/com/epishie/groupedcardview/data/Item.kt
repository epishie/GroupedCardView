package com.epishie.groupedcardview.data

import java.util.Date

data class Item(val id: String,
                val pushed_at: Date,
                val amount: Int,
                val currency: String,
                val description: String,
                val kind: String)

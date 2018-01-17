package com.epishie.groupedcardview.data

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ItemsLoader(context: Context) : AsyncTaskLoader<Result>(context) {
    private var items: Result? = null
    private val api: Api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://www.mocky.io")
            .build()
            .create(Api::class.java)

    override fun loadInBackground(): Result {
        return try {
            val response = api.getItems().execute()
            if (response.isSuccessful) {
                val items = response.body()
                if (items != null) {
                    Result.Success(items)
                } else {
                    Result.Error
                }
            } else {
                Result.Error
            }
        } catch (exception : IOException) {
            Result.Error
        }
    }

    override fun onStartLoading() {
        super.onStartLoading()
        if (items != null) {
            deliverResult(items)
        }
        if (items == null || takeContentChanged()) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
        super.onStopLoading()
    }
}
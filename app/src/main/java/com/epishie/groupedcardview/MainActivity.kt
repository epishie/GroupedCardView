package com.epishie.groupedcardview

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.epishie.groupedcardview.data.ItemsLoader
import com.epishie.groupedcardview.data.Result
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Result> {
    private lateinit var adapter: GroupedCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        adapter = GroupedCardAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false)
        retry.setOnClickListener {
            supportLoaderManager.restartLoader(0, null, this)
        }

        supportLoaderManager.initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Result> {
        progress.visibility = View.VISIBLE
        return ItemsLoader(this)
    }

    override fun onLoadFinished(loader: Loader<Result>, data: Result) {
        progress.visibility = View.GONE
        when (data) {
            is Result.Success -> {
                recyclerView.visibility = View.VISIBLE
                error.visibility = View.GONE
                adapter.addGroup(ItemGroup(this, data.items, { item ->
                    AlertDialog.Builder(this)
                            .setTitle(item.id)
                            .setMessage(item.description)
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                }))
                adapter.addGroup(TotalGroup(this, 0))
            }
            else -> {
                recyclerView.visibility = View.GONE
                error.visibility = View.VISIBLE
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Result>) {
    }
}
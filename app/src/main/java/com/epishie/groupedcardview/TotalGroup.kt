package com.epishie.groupedcardview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.group_total.view.*

class TotalGroup(context: Context, private val total: Int) : GroupedCardAdapter.Group {
    override val header: String? = context.getString(R.string.section_header_2)
    override val footer: String? = context.getString(R.string.section_footer_2)
    override val itemCount: Int = 1
    private val inflater = LayoutInflater.from(context)

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.group_total, parent, false))
    }

    override fun bindViewHolder(index: Int, viewHolder: RecyclerView.ViewHolder) {
        with (viewHolder.itemView) {
            title.text = context.getString(R.string.total)
            value.text = context.getString(R.string.currency_value, total)
        }
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
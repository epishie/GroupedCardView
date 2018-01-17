package com.epishie.groupedcardview

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.epishie.groupedcardview.data.Item
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.group_item.view.*

class ItemGroup(context: Context, private val items: List<Item>, private val listener: (item: Item) -> Unit)
: GroupedCardAdapter.Group {
    companion object {
        private const val KIND_CAPTURED = "capture"
        private const val KIND_REFUND = "refund"
    }

    override val header: String? = context.getString(R.string.section_header_1)
    override val footer: String? = null
    override val itemCount: Int = items.size
    private val inflater = LayoutInflater.from(context)
    private val dateFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.group_item, parent, false))
    }

    override fun bindViewHolder(index: Int, viewHolder: RecyclerView.ViewHolder) {
        val item = items[index]
        with (viewHolder.itemView) {
            title.text = item.description
            value.text = context.getString(R.string.currency_value, item.amount)
            subtitle.text = dateFormatter.format(item.pushed_at)
            type.text = item.kind
            type.background = when (item.kind) {
                KIND_CAPTURED -> ContextCompat.getDrawable(context, R.drawable.kind_bg)
                KIND_REFUND -> ContextCompat.getDrawable(context, R.drawable.kind_greyed_bg)
                else -> null
            }
            val lp = cardView.layoutParams as FrameLayout.LayoutParams
            lp.topMargin = 0
            lp.bottomMargin = 0
            when (index) {
                0 -> lp.bottomMargin = context.resources
                        .getDimensionPixelSize(R.dimen.card_margin_offset)
                items.size - 1 -> lp.topMargin = context.resources
                        .getDimensionPixelSize(R.dimen.card_margin_offset)
                else -> {
                    lp.topMargin = context.resources
                            .getDimensionPixelSize(R.dimen.card_margin_offset)
                    lp.bottomMargin = context.resources
                            .getDimensionPixelSize(R.dimen.card_margin_offset)
                }
            }
            cardView.layoutParams = lp
            divider.visibility = if (index == items.size - 1) {
                View.GONE
            } else {
                View.VISIBLE
            }
            cardView.setOnClickListener {
                listener(items[index])
            }
        }
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
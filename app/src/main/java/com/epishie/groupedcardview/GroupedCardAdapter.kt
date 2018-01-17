package com.epishie.groupedcardview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.group_header.view.*
import kotlinx.android.synthetic.main.group_footer.view.*

class GroupedCardAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val INVALID_COUNT = -1
        private const val HEADER_VIEW_HOLDER = -1
        private const val FOOTER_VIEW_HOLDER = -2
        private const val ITEM_VIEW_HOLDER = 0
    }

    private val inflater = LayoutInflater.from(context)
    private val groups: MutableList<Group> = mutableListOf()
    private var count: Int = INVALID_COUNT

    override fun getItemCount(): Int {
        if (count == INVALID_COUNT) {
            count = 0
            groups.forEach { group ->
                if (group.header != null) {
                    count++
                }
                if (group.footer != null) {
                    count++
                }
                count += group.itemCount
            }
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var currentPosition = 0
        groups.forEachIndexed { index, group ->
            var groupTotal = group.itemCount
            if (group.header != null) {
                groupTotal++
            }
            if (group.footer != null) {
                groupTotal++
            }
            if (position in currentPosition until currentPosition + groupTotal) {
                if (group.header != null && position == currentPosition) {
                    return HEADER_VIEW_HOLDER
                }
                if (group.footer != null && position == currentPosition + groupTotal - 1) {
                    return FOOTER_VIEW_HOLDER
                }
                return ITEM_VIEW_HOLDER + index
            }
            currentPosition += groupTotal
        }
        throw IndexOutOfBoundsException("Position not found")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_HOLDER -> HeaderViewHolder(inflater.inflate(R.layout.group_header,
                    parent, false))
            FOOTER_VIEW_HOLDER -> FooterViewHolder(inflater.inflate(R.layout.group_footer,
                    parent, false))
            else -> {
                groups[viewType - ITEM_VIEW_HOLDER].createViewHolder(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var currentPosition = 0
        for (group in groups) {
            var groupTotal = group.itemCount
            var offset = 0
            if (group.header != null) {
                groupTotal++
                offset = -1
            }
            if (group.footer != null) {
                groupTotal++
            }
            if (position in currentPosition until currentPosition + groupTotal) {
                if (group.header != null && position == currentPosition) {
                    (holder as HeaderViewHolder).bind(group.header!!)
                } else if (group.footer != null && position == currentPosition + groupTotal - 1) {
                    (holder as FooterViewHolder).bind(group.footer!!)
                } else {
                    group.bindViewHolder(position - currentPosition + offset, holder)
                }
                break
            }
            currentPosition += groupTotal
        }
    }

    fun addGroup(group: Group) {
        val lastIndex = itemCount
        groups.add(group)
        var groupCount = group.itemCount
        if (group.header != null) {
            groupCount++
        }
        if (group.footer != null) {
            groupCount++
        }
        count = INVALID_COUNT
        notifyItemRangeInserted(lastIndex, groupCount)
    }

    private class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(text: String) {
            itemView.header.text = text
        }
    }
    private class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(text: String) {
            itemView.footer.text = text
        }
    }

    /*
    private class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateFormatter = SimpleDateFormat("yyyy/MM/dd")
        fun bind(total: Int) {
            with (itemView) {
                title.text = "Total"
                value.text = context.getString(R.string.currency_value, total)
                subtitle.visibility = View.GONE
                type.visibility = View.GONE
            }
        }

        fun bind(item: Item) {
            with (itemView) {
                title.text = item.description
                value.text = context.getString(R.string.currency_value, item.amount)
                subtitle.visibility = View.VISIBLE
                subtitle.text = dateFormatter.format(item.pushed_at)
                type.visibility = View.VISIBLE
                type.text = item.kind
            }
        }
    }
    */

    /*
    data class Group<out T>(val header: String?, val footer: String?, val items: List<T>,
                            val onTapAction: (index: Int) -> Unit)
                            */
    interface Group {
        val header: String?
        val footer: String?
        val itemCount: Int

        fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
        fun bindViewHolder(index: Int, viewHolder: RecyclerView.ViewHolder)
    }
}
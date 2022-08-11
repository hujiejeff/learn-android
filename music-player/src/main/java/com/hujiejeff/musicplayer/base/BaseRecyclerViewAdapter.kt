package com.hujiejeff.musicplayer.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hujiejeff.musicplayer.util.getSuperClassGenericClass

abstract class BaseRecyclerViewAdapter<V : ViewBinding, D>(
    private val context: Context?,
    private val dataList: List<D>
) : RecyclerView.Adapter<BaseViewHolder<V>>() {
    private var clazz: Class<V>? = null

    constructor(context: Context?, clazz: Class<V>, dataList: List<D>) : this(context, dataList) {
        this.clazz = clazz
    }

    private var mItemClick: ((position: Int) -> Unit)? = null
    private var mItemLongClick: ((position: Int) -> Boolean)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        if (clazz == null) {
            clazz = getSuperClassGenericClass<V>(this)
        }
        val inflateMethod = clazz!!.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val binding: V =
            inflateMethod.invoke(null, LayoutInflater.from(context), parent, false) as V
        return BaseViewHolder<V>(binding)
    }


    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        val data = dataList[position]
        holder.itemView.setOnClickListener {
            mItemClick?.invoke(position)
        }
        holder.itemView.setOnLongClickListener {
            if (mItemLongClick == null) {
                false
            } else {
                mItemLongClick!!.invoke(position)
            }
        }
        convert(holder, data, position)
    }

    override fun getItemCount() = dataList.size
    abstract fun convert(holder: BaseViewHolder<V>, data: D, position: Int)
    fun setOnItemClickListener(itemClick: (position: Int) -> Unit) {
        mItemClick = itemClick
    }

    fun setOnItemLongClickListener(itemLongClick: (position: Int) -> Boolean) {
        mItemLongClick = itemLongClick
    }
}

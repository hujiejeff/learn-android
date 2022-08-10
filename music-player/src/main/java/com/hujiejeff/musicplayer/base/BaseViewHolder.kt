package com.hujiejeff.musicplayer.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<out V: ViewBinding>(val mBinding: V) : RecyclerView.ViewHolder(mBinding.root) {
//    private val views: SparseArray<View> = SparseArray()
//    fun <V : View> getView(id: Int): V {
//        var view = views[id]
//        if (views[id] == null) {
//            view = itemView.findViewById(id)
//            views[id] = view
//        }
//        return view as V
//    }

}
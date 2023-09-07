package com.hujiejeff.learn_android.material3

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityCarouseDemoBinding
import com.hujiejeff.learn_android.databinding.ItemCarouseSampleBinding


class CarouseDemoActivity : BaseActivity<ActivityCarouseDemoBinding>() {
    override fun ActivityCarouseDemoBinding.initView() {
        rvCarouse.apply {
            layoutManager = CarouselLayoutManager().apply {

            }
            adapter = TestAdapter().apply {
                submitList(listOf(0,1,2,3,4,5))
            }
        }


    }


    class TestAdapter : BaseQuickAdapter<Int, TestAdapter.VH>() {

        // 自定义ViewHolder类
        class VH(
            parent: ViewGroup,
            val binding: ItemCarouseSampleBinding = ItemCarouseSampleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
        ) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
            // 返回一个 ViewHolder
            return VH(parent)
        }

        override fun onBindViewHolder(holder: VH, position: Int, item: Int?) {
            // 设置item数据
            holder.binding.run {
                tv.text = item.toString()
                carouselImageView.setImageResource(R.mipmap.sample)
            }
        }

    }
}
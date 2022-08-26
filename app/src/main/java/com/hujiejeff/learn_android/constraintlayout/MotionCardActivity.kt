package com.hujiejeff.learn_android.constraintlayout

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Carousel
import com.bumptech.glide.Glide
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.Api
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.base.CardNewsItem
import com.hujiejeff.learn_android.base.Rep
import com.hujiejeff.learn_android.databinding.ActivityMotionCardBinding
import com.hujiejeff.learn_android.databinding.ItemCardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MotionCardActivity : BaseActivity<ActivityMotionCardBinding>() {
    private val color = arrayOf(
        R.color.red,
        R.color.purple_200,
        R.color.black,
        R.color.teal_200,
        R.color.purple_700,
        R.color.purple_500
    )
    private lateinit var list: List<CardNewsItem>

    override fun ActivityMotionCardBinding.initView() {
        initNetwork()


    }

    private fun iniAdapter() {
        mBinding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int = list.size

            @RequiresApi(Build.VERSION_CODES.M)
            override fun populate(view: View, index: Int) {
                Log.d("TAG", "populate: " + index)
//                (view as ViewGroup).getChildAt(0).setBackgroundColor(getColor(color[index]))
                val itemCardBinding = ItemCardBinding.bind((view as ViewGroup).getChildAt(0))
                itemCardBinding.tvTitle.text = list[index].title
                itemCardBinding.tvSummery.text = list[index].summary
                Glide.with(view.context).load(list[index].thumb).into(itemCardBinding.ivCover)
            }

            override fun onNewItem(index: Int) {
                // called when an item is set
                Log.d("TAG", "onNewItem: " + index)
            }
        })
        mBinding.carousel.refresh()
    }


    private fun initNetwork() {
//        val retrofit =
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://m-api.dutenews.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(Api::class.java)
        api.getCardNewsItem().enqueue(object : Callback<Rep> {
            override fun onResponse(call: Call<Rep>, response: Response<Rep>) {
                Log.d("hujie", "onResponse: " + response.body())
                list = response.body()!!.data.flycard.list
                iniAdapter()
            }

            override fun onFailure(call: Call<Rep>, t: Throwable) {

            }
        })
    }
}
package com.hujiejeff.learn_android.base

import retrofit2.Call
import retrofit2.http.GET


interface Api {

    /**
     * 首页文章
     */
    @GET("/v2/menudata?listsiteid=0&clientid=1&app_version=6.2.0&device_id=f927e67a-52a2-4cb1-92f3-ad60a9f76da3&thumbrate=3&system_name=hmos&ip=172.20.209.214&sign=5cd46e7c0fb53cc814efc7d3d7872748&areas=%2C%2C&device_type=default&type=android&modules=flycard%3A1&slide=0&lastContentId=0&isFilter=1&pagesize=20&menuid=207&siteid=10001&time=1651214279548")
    fun getCardNewsItem(): Call<Rep>
}
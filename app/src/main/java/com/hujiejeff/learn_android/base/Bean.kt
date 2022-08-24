package com.hujiejeff.learn_android.base

data class Rep(
    val `data`: Data,
    val info: String,
    val state: Boolean
)

data class Data(
    val flycard: Flycard
)

data class Flycard(
    val exist: Any,
    val lastupdate: Int,
    val list: List<CardNewsItem>,
    val nextpage: Boolean,
    val total: Int,
    val version: String
)

data class CardNewsItem(
    val appid: Int,
    val card_menu_id: String,
    val card_template_id: Int,
    val card_template_thumb: String,
    val category: String,
    val comments: Int,
    val contentid: Int,
    val created: String,
    val created_str: String,
    val current_time: Int,
    val digg: Int,
    val enablecopyright: Int,
    val end_time: Int,
    val id: Int,
    val is_portrait: Int,
    val last_comment_at: String,
    val multi_card_template_thumb: List<String>,
    val ontop: Int,
    val palytime: String,
    val published: String,
    val pv: Int,
    val show_mode: Int,
    val skip: Any,
    val source: String,
    val summary: String,
    val text_audio_url: String,
    val thumb: String,
    val thumb_ratio: Int,
    val thumbs: List<String>,
    val title: String,
    val total_digg: Int,
    val url: String,
    val video: String,
    val virtual_pv: Int
)
package com.hujiejeff.musicplayer.data.entity

data class BannerResponse(
    val banners: List<Banner>,
    val code: Int
)

data class Banner(
    val adDispatchJson: Any,
    val adLocation: Any,
    val adSource: Any,
    val adid: Any,
    val adurlV2: Any,
    val alg: String,
    val bannerId: String,
    val dynamicVideoData: Any,
    val encodeId: String,
    val event: Any,
    val exclusive: Boolean,
    val extMonitor: Any,
    val extMonitorInfo: Any,
    val logContext: Any,
    val monitorBlackList: Any,
    val monitorClick: Any,
    val monitorClickList: List<Any>,
    val monitorImpress: Any,
    val monitorImpressList: List<Any>,
    val monitorType: Any,
    val pic: String,
    val pid: Any,
    val program: Any,
    val requestId: String,
    val s_ctrp: String,
    val scm: String,
    val showAdTag: Boolean,
    val showContext: Any,
    val song: Song,
    val targetId: Long,
    val targetType: Int,
    val titleColor: String,
    val typeTitle: String,
    val url: Any,
    val video: Any
)


data class H(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
)

data class Hr(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
)

data class L(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
)

data class M(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
)

data class Privilege(
    val chargeInfoList: List<ChargeInfo>,
    val cp: Int,
    val cs: Boolean,
    val dl: Int,
    val dlLevel: String,
    val downloadMaxBrLevel: String,
    val downloadMaxbr: Int,
    val fee: Int,
    val fl: Int,
    val flLevel: String,
    val flag: Int,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val id: Int,
    val maxBrLevel: String,
    val maxbr: Int,
    val payed: Int,
    val pl: Int,
    val plLevel: String,
    val playMaxBrLevel: String,
    val playMaxbr: Int,
    val preSell: Boolean,
    val rscl: Any,
    val sp: Int,
    val st: Int,
    val subp: Int,
    val toast: Boolean
)

data class Sq(
    val br: Int,
    val fid: Int,
    val size: Int,
    val sr: Int,
    val vd: Int
)

data class ChargeInfo(
    val chargeMessage: Any,
    val chargeType: Int,
    val chargeUrl: Any,
    val rate: Int
)

data class FreeTrialPrivilege(
    val listenType: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
)
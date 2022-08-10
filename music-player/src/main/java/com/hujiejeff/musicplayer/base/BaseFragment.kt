package com.hujiejeff.musicplayer.base

import PermissionReq
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hujiejeff.musicplayer.util.setFragmentContentView

abstract class BaseFragment<V: ViewBinding> : Fragment() {
    protected lateinit var mBinding: V
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = setFragmentContentView(layoutInflater, container)!!
        val view = mBinding.root
        mBinding.initView()
        return view
    }

    abstract fun V.initView()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionReq.onRequestPermissionResult(requestCode, permissions, grantResults)
    }
}
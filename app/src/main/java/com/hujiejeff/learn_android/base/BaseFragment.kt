package com.hujiejeff.learn_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hujiejeff.learn_android.util.setFragmentContentView

abstract class BaseFragment<V : ViewBinding> : Fragment() {
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

    companion object {
        /*inline fun <reified T : Fragment> newInstance(vararg pairs: Pair<String, Any>): Fragment {
            val clazz = T::class.java
            val fragment = clazz.newInstance() as T
            fragment.run {
                arguments = Bundle().apply {
                    pairs.forEach {
                        when(it.second) {
                            is String -> putString(it.first, it.second as String)
                            is Int -> putInt(it.first, it.second as Int)
                        }
                    }
                }
            }
            return fragment
        }
*/
    }
}
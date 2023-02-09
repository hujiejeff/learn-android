package com.hujiejeff.learn_android.materia_design.toolbar

import android.content.Context
import android.graphics.Outline
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.databinding.FragmentTollbarDemoBinding

class ToolbarDemoFragment: BaseFragment<FragmentTollbarDemoBinding>() {
    override fun FragmentTollbarDemoBinding.initView() {
        ViewCompat.setTransitionName(root, "ToolbarDemoFragment")
        ivClipTest.clipToOutline = true
        ivClipTest.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(50, 50, view.width, view.height)
            }
        }
    }
/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
                val searchItem = menu.findItem(R.id.search)
                val searchView = searchItem.actionView as SearchView
                searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                        Snackbar.make(view, "onMenuItemActionExpand", LENGTH_SHORT).show()
                        return true
                    }

                    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                        Snackbar.make(view, "onMenuItemActionCollapse", LENGTH_SHORT).show()
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Snackbar.make(view, "onMenuItemSelected", LENGTH_SHORT).show()
                return true
            }
        }, requireActivity(), Lifecycle.State.CREATED)
    }*/
}
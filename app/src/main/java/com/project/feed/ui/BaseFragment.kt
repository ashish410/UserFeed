package com.project.feed.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.project.feed.R
import com.project.feed.SNACK_BAR_TEXT_SIZE
import com.project.feed.network.connectivity.base.ConnectivityProvider
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

open class BaseFragment : Fragment(), ConnectivityProvider.ConnectivityStateListener {

    private var snackBar: Snackbar? = null

    @Inject
    lateinit var connectivityProvider: ConnectivityProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityProvider.addListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityProvider.removeListener(this)
    }

    internal fun showSnackBar(view: View, message: String, length: Int) {
        snackBar = Snackbar.make(view, message, length)
        val snackBarView = snackBar?.view
        snackBarView?.setBackgroundColor(
            ContextCompat.getColor(requireActivity().applicationContext, R.color.purple_500)
        )
        val textView =
            snackBarView?.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = SNACK_BAR_TEXT_SIZE
        snackBar?.show()
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        if (state.hasInternet()) {
            snackBar?.dismiss()
            return
        }
    }

    internal fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }
}
package com.iloatnew.weather.ui.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtil {
    private var toast: Toast? = null

    fun showShort(context: Context, @StringRes resId: Int) {
        toast?.cancel()
        toast = Toast.makeText(context.applicationContext, resId, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

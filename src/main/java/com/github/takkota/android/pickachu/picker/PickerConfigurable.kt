package com.github.takkota.android.pickachu.picker

import com.github.takkota.android.pickachu.viewmodel.PickerViewModel
import java.io.Serializable

/**
 * Created by kota on 2018/04/16.
 */
interface PickerMediaLoadListener {
    fun onLoadCompleted(media: ArrayList<UserMedia>)
    fun onLoadFailed()
}

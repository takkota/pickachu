package com.github.takkota.android.pickachu.viewmodel

import android.content.Context
import com.github.takkota.android.pickachu.picker.MediaLoader
import com.github.takkota.android.pickachu.picker.Picker
import com.github.takkota.android.pickachu.picker.UserMedia

/**
 * Created by kota on 2018/05/16.
 */

class PickerViewModel(context: Context, val picker: Picker) {
    var items: MutableList<UserMedia> = mutableListOf()
    var loading: Boolean = false
    var mediaLoader = MediaLoader(context, picker)

    fun clearLoadedMedia() {
        items = mutableListOf()
    }

    fun loadMedia() {
        loading = true
        mediaLoader.loadMedia()
    }
}

package com.github.takkota.android.pickachu.picker

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.airbnb.epoxy.EpoxyRecyclerView
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

open class Picker(val dataSource: PickerDataSource, val structure: PickerStructure) : PickerMediaLoadListener, Serializable {
    private var viewProvider: ViewProvider? = null

    interface ViewProvider {
        fun setupRecyclerView(media: ArrayList<UserMedia>)
    }

    fun setViewProvider(viewProvider: ViewProvider) {
        this.viewProvider = viewProvider
    }

    override fun onLoadCompleted(media: ArrayList<UserMedia>) {
        Log.d("aaa","view作るぞ")
        viewProvider?.setupRecyclerView(media)
    }

    override fun onLoadFailed() {
    }

    open fun buildView(media: ArrayList<UserMedia>, epoxyRecyclerView: EpoxyRecyclerView, context: Context) {}
}
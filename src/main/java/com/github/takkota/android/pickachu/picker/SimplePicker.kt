package com.github.takkota.android.pickachu.picker

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.takkota.android.pickachu.MainActivity
import com.github.takkota.android.pickachu.extension.withModels
import com.github.takkota.android.pickachu.view.PickerActivity
import com.github.takkota.android.pickachu.view.pickerItem
import com.github.takkota.android.pickachu.viewmodel.PickerViewModel

class SimplePicker(dataSource: PickerDataSource, structure: PickerStructure): Picker(dataSource, structure) {

    override fun buildView(media: ArrayList<UserMedia>, epoxyRecyclerView: EpoxyRecyclerView, context: Context) {
        val lm = GridLayoutManager(context,3)
        epoxyRecyclerView.layoutManager = lm
        epoxyRecyclerView.withModels {
            this.spanCount = 3
            lm.spanSizeLookup = this.spanSizeLookup
            media.forEachIndexed { index, item ->
                this.pickerItem {
                    id("item:$index")
                    spanSizeOverride { totalSpanCount, position, itemCount ->  1}
                    imageResource(item.path)
                    clickListener { model, parentView, clickedView, position ->  }
                }
            }
        }
    }

    fun show(context: Context){
        context.startActivity(PickerActivity.createIntent(context, this))
    }

    class Builder: PickerBuilderBase() {
        fun build(): SimplePicker {
            return SimplePicker(
                    PickerDataSource(this.storage, this.mediaType),
                    PickerStructure(this.groupBy, this.order))
        }
    }

    companion object {
        fun build(f: Builder.() -> Unit): SimplePicker {
            val builder = Builder().apply {
                f()
            }
            return builder.build()
        }
    }
}
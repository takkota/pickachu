package com.github.takkota.android.pickachu.view

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.github.takkota.android.pickachu.R
import com.github.takkota.android.pickachu.R2
import com.github.takkota.android.pickachu.extension.decodeImageFromPath
import kotlinx.android.synthetic.main.item_picker.view.*

/**
 *
 */
@ModelView(defaultLayout = R2.layout.item_picker, saveViewState = false)
class PickerItem: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var sideSize = 0

    init {
        sideSize = context.resources.displayMetrics.widthPixels / 3
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    fun clickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }

    @ModelProp
    fun setImageResource(filePath: String?) {
        if (filePath != null) {
            itemImage.decodeImageFromPath(filePath, sideSize, sideSize, true) {
                itemImage.adjustViewBounds = true
                itemImage.visibility = View.VISIBLE
            }
        } else {
            itemImage.setImageBitmap(null)
            itemImage.visibility = View.INVISIBLE
        }
    }
    @OnViewRecycled
    fun clear() {
        itemImage.setImageBitmap(null)
        itemImage.visibility = View.INVISIBLE
        setOnClickListener(null)
        selectedIcon.setImageDrawable(null)
        // 画像の切り替わりを自然にするため、Recycle時に黒背景のViewを設定する
    }
}
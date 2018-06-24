package com.github.takkota.android.pickachu
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.github.takkota.android.pickachu.picker.*
import com.github.takkota.android.pickachu.utility.PermissionUtil
import com.github.takkota.android.pickachu.view.PickerActivity.Companion.REQUEST_PERMISSION_PICKER
import com.github.takkota.android.pickachu.viewmodel.PickerViewModel
import com.trello.rxlifecycle2.components.RxActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_picker.*

class MainActivity: RxActivity() {

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
        setContentView(R.layout.activity_main)
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
        mainButton.setOnClickListener {
            PermissionUtil.requestStoragePermission(this, REQUEST_PERMISSION_PICKER, {
                SimplePicker.build {
                    this.mediaType = MediaType.IMAGE
                    this.storage = Storage.REMOTE
                }.show(this)
            })
        }
    }
}
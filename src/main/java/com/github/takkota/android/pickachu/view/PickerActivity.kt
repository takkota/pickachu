package com.github.takkota.android.pickachu.view
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.github.takkota.android.pickachu.R
import com.github.takkota.android.pickachu.R2
import com.github.takkota.android.pickachu.picker.MediaType
import com.github.takkota.android.pickachu.picker.Picker
import com.github.takkota.android.pickachu.picker.SimplePicker
import com.github.takkota.android.pickachu.picker.UserMedia
import com.github.takkota.android.pickachu.utility.PermissionUtil
import com.github.takkota.android.pickachu.viewmodel.PickerViewModel
import com.trello.rxlifecycle2.components.RxActivity
import kotlinx.android.synthetic.main.activity_picker.*

class PickerActivity : RxActivity(), Picker.ViewProvider {
    lateinit var picker: Picker
    lateinit var vm: PickerViewModel

    companion object {
        val pickerKey = "pickerKey"
        const val REQUEST_PERMISSION_ALBUM = 1001
        const val REQUEST_PERMISSION_PICKER = 1002
        const val REQUEST_PERMISSION_ALBUM_PAGER = 1003
        const val REQUEST_PERMISSION_PROFILE = 1004
        fun createIntent(context: Context, picker: Picker): Intent {
            return Intent(context, PickerActivity::class.java).apply {
                putExtra(pickerKey, picker)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        picker = intent.getSerializableExtra(pickerKey) as Picker
        picker.setViewProvider(this)
        vm = PickerViewModel(this, picker)
    }

    override fun onResume() {
        super.onResume()
        vm.loadMedia()
    }

    override fun setupRecyclerView(media: ArrayList<UserMedia>) {
        Log.d("aaa", "setupRecycler")
        picker.buildView(media, recyclerView, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_PERMISSION_PICKER -> {
                    vm.loadMedia()
                }
            }
        }
    }

}
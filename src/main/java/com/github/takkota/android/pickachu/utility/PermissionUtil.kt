package com.github.takkota.android.pickachu.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker

/**
 * Created by kota on 2018/06/23.
 */
class PermissionUtil {
    companion object {
        // forActivity
        @Suppress("unused")
        fun requestStoragePermission(activity: Activity, requestCode: Int, callbackIfPermitted: () -> Unit) {
            if (checkPermissionGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 既に許可している
                callbackIfPermitted()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode)
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }
            }
        }

        // forFragment
        fun requestStoragePermission(fragment: Fragment, requestCode: Int, callbackIfPermitted: () -> Unit) {
            if (checkPermissionGranted(fragment.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 既に許可している
                callbackIfPermitted()
            } else {
                if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermission(fragment, Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode)
                } else {
                    // 次回以降表示しない
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", fragment.context!!.packageName, null)
                    }
                    fragment.startActivity(intent)
                }
            }
        }

        fun checkPermissionGranted(context: Context, permission: String): Boolean {
            return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        // forActivity
        // 許可を求める
        // 結果はactivityのonRequestPermissionsResultでハンドリングする
        private fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(permission),
                    requestCode)
        }

        // forFragment
        // 許可を求める
        // 結果はfragmentのonRequestPermissionsResultでハンドリングする
        private fun requestPermission(fragment: Fragment, permission: String, requestCode: Int) {
            fragment.requestPermissions(
                    arrayOf(permission),
                    requestCode)
        }

    }
}

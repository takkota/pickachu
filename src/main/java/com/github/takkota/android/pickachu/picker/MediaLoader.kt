package com.github.takkota.android.pickachu.picker

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.*

/**
 * Created by kota on 2018/05/16.
 */
// meidaをロードする機能本体
class MediaLoader(val context: Context, val picker: Picker) {
    var mediaType: MediaType = picker.dataSource.mediaType
    var storage: Storage = picker.dataSource.storage
    var order: ORDER = picker.structure.order

    private val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.LATITUDE,
            MediaStore.Images.ImageColumns.LONGITUDE,
            MediaStore.Video.VideoColumns.LATITUDE,
            MediaStore.Video.VideoColumns.LONGITUDE
    )

    fun loadMedia() {
        val outerContext = context
        Log.d("aaa", "loadするぞ")
        launch(UI) {
            Log.d("aaa", "load開始")
            val userMedia = async {
                readMediaStore(outerContext)
            }.await()
            Log.d("aaa", "loadCompleted")
            picker.onLoadCompleted(userMedia)
        }
    }

    private suspend fun readMediaStore(context: Context): ArrayList<UserMedia> {
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" +
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR " +
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=" +
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        var results = arrayListOf<UserMedia>()
        val cursor: Cursor?
        cursor = try {
            context.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    selection,
                    null,
                    MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )
        } catch (e: SecurityException) {
            Log.d("aaa", "エラーだ")
            return results
        }
        if (cursor == null) {
            Log.d("aaa", "データない")
            return results
        }

        cursor.moveToFirst()
        cursor.use {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(projection[0]))
                val name = cursor.getString(cursor.getColumnIndex(projection[1]))
                val path = cursor.getString(cursor.getColumnIndex(projection[2]))
                val type = cursor.getInt(cursor.getColumnIndex(projection[3]))
                val createdAt = cursor.getInt(cursor.getColumnIndex(projection[4]))
                val imageLatitude = cursor.getDouble(cursor.getColumnIndex(projection[5]))
                val imageLongitude = cursor.getDouble(cursor.getColumnIndex(projection[6]))
                val videoLatitude = cursor.getDouble(cursor.getColumnIndex(projection[7]))
                val videoLongitude = cursor.getDouble(cursor.getColumnIndex(projection[8]))

                val file = makeSafeFile(path)
                if (file != null && file.exists()) {
                    val createdAtDate= Date(createdAt * 1000L)

                    val medium: UserMedia
                    medium = when (type) {
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> UserMedia(id.toInt(), MediaType.MOVIE, path, createdAtDate, videoLatitude, videoLongitude)
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> UserMedia(id.toInt(), MediaType.IMAGE, path, createdAtDate, imageLatitude, imageLongitude)
                        else -> // ここには来ない
                            UserMedia(id.toInt(), MediaType.IMAGE, path, createdAtDate, videoLatitude, videoLongitude)
                    }
                    results.add(medium)
                }
            } while (cursor.moveToNext())
        }
        Log.d("aaa", "読み込みDone" + results.size)
        return results
    }

    private fun makeSafeFile(path: String?): File? {
        if (path == null || path.isEmpty()) {
            return null
        }
        return try {
            File(path)
        } catch (ignored: Exception) {
            null
        }
    }
}

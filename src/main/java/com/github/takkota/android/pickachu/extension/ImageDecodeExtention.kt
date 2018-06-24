package com.github.takkota.android.pickachu.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.takkota.android.pickachu.utility.GlideApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

/**
 * Created by kota on 2018/05/16.
 */
fun ImageView.decodeImageFromPath(path: String, width: Int, height: Int, centerCrop: Boolean = true, callback: () -> Unit): Disposable {
    return Observable.create<Bitmap> { emitter ->
        // Progressive Jpeg的な挙動を実現するため、blurImageをまずは表示させる
        val blurBitmap = decodeBitmapFilePath(context, path, width, height, centerCrop, true)
        if (blurBitmap != null) {
            emitter.onNext(blurBitmap)
        }
        val bitmap = decodeBitmapFilePath(context, path, width, height, centerCrop, false)
        if (bitmap != null) {
            emitter.onNext(bitmap)
        }
    }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe {
        this.setImageBitmap(it)
        callback()
    }
}

@SuppressLint("CheckResult")
private fun decodeBitmapFilePath(context: Context, path: String, width: Int, height: Int, centerCrop: Boolean, interlace: Boolean): Bitmap? {
    try {
        // 画像ファイルが大きすぎると表示できないので、リサイズする
        val target = GlideApp.with(context)
                .asBitmap()
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        if (interlace) {
            target.thumbnail(0.1f)
        }

        if (centerCrop) {
            target.centerCrop()
        } else {
            target.fitCenter()
        }

        return target.submit(width, height).get()
    } catch (e: IOException) {
        return null
    } catch (e: InterruptedException) {
        return null
    }
}

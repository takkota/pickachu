package com.github.takkota.android.pickachu.picker

import java.io.Serializable
import java.util.*

/**
 * Created by kota on 2018/05/16.
 */
class UserMedia(
        val id: Int,
        val type: MediaType,
        val path: String,
        val createdAt: Date,
        val latitude: Double?,
        val longitude: Double?
) : Serializable

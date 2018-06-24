package com.github.takkota.android.pickachu.picker

open class PickerBuilderBase {
    var storage: Storage = Storage.LOCAL
    var mediaType: MediaType = MediaType.IMAGE
    var groupBy: GroupBy = GroupBy.CREATED
    var order: ORDER = ORDER.DESC
}


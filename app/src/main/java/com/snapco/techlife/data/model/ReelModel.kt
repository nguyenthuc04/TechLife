package com.snapco.techlife.data.model

import com.google.firebase.Timestamp

data class ReelModel(
    var videoId : String = "",
    var title : String = "",
    var url : String = "",
    var uploaderId : String = "",
    var createdTime : Timestamp = Timestamp.now()
)
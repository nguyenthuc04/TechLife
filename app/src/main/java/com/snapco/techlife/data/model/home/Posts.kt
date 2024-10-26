package com.snapco.techlife.data.model.home

data class Posts (val username: String? = "",
                  val location: String?="",
                  val image:String?="",
                  val date: String?="",
                  val caption: String?="",
                  val likes: Int?= 0,
//                  val userid: String?="",
                  val comment_count: String?="",
                  val profileImage: String?= "",){
}
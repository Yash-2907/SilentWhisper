package com.example.silentwhisper.Notifications

class Token(var token: String) {

    fun getToken(): String {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }
}


//package com.example.silentwhisper.Notifications
//
//class Token {
//    var token: String=""
//    constructor(token: String){
//        this.token=token
//
//    }
//    fun getToken():String?{
//        return token
//    }
//    fun setToken(token:String?)
//    {
//        this.token=token!!
//    }
//
//}
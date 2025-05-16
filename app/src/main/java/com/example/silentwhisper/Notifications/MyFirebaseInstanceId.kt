package com.example.silentwhisper.Notifications

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceId : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        // Retrieve the token and update it in the database
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val refreshToken = task.result
                if (firebaseUser != null && refreshToken != null) {
                    updateToken(refreshToken)
                }
            } else {
                // Log the exception for debugging
                task.exception?.let { exception ->
                    Log.e("MyFirebaseInstanceId", "Token retrieval failed", exception)
                }
            }
        }
    }

    fun updateToken(refreshToken: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val ref = FirebaseDatabase.getInstance().getReference("Tokens")
            val token = Token(refreshToken)
            ref.child(firebaseUser.uid).setValue(token)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("MyFirebaseInstanceId", "Token successfully updated")
                    } else {
                        // Log error if token update fails
                        task.exception?.let { exception ->
                            Log.e("MyFirebaseInstanceId", "Failed to update token", exception)
                        }
                    }
                }
        }
    }
}







//package com.example.silentwhisper.Notifications
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
//import com.google.firebase.messaging.FirebaseMessagingService
//
//class MyFirebaseInstanceId:FirebaseMessagingService()
//{
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        val firebaseUser= FirebaseAuth.getInstance().currentUser
//        val refreshToken=MyFirebaseInstanceId.getInstance().token
//        if(firebaseUser!=null)
//        {
//            updateToken(refreshToken)
//        }
//    }
//
//    private fun updateToken(refreshToken: String?) {
//        val firebaseUser= FirebaseAuth.getInstance().currentUser
//        val ref=FirebaseDatabase.getInstance().getReference().child("Tokens")
//        val token=token(refreshToken!!)
//        ref.child(firebaseUser!!.uid).setValue(token)
//
//    }
//}
package com.example.silentwhisper

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.silentwhisper.R
import com.example.silentwhisper.databinding.ActivityAddUsersBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

lateinit var abind:ActivityAddUsersBinding
class AddUsers : AppCompatActivity() {
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var allUsers: List<User> // Store all users to filter later

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abind = ActivityAddUsersBinding.inflate(layoutInflater)
        setContentView(abind.root)

        abind.backbtn.setOnClickListener {
            startActivity(Intent(this@AddUsers, FriendsPage::class.java))
            finish()
        }

        // Initialize the adapter
        adapter = GroupAdapter<GroupieViewHolder>()
        abind.addRecyclerView.adapter = adapter // Set the adapter to RecyclerView

        // Set up search functionality
        abind.searchInput.addTextChangedListener { text ->
            if(isInternetAvailable(this@AddUsers)) {
                filterUsers(text.toString()) // Call filter method on text change
            }
        }

        fetchUsers() // Fetch users
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users") // Reference to the "users" collection
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid // Get the current user's ID

        usersRef.get().addOnSuccessListener { querySnapshot ->
            val usersList = mutableListOf<User>() // Temporary list to hold users

            for (document in querySnapshot) {
                val user = document.toObject(User::class.java).copy(id = document.id) // Set the user ID
                // Check if the user ID is not the current user's ID
                if (document.id != currentUserId) {
                    usersList.add(user) // Add the user to the list
                    adapter.add(UserItem(user)) // Add the UserItem to the adapter
                }
            }
            allUsers = usersList // Store all users for filtering
        }.addOnFailureListener { exception ->
            Log.e("AddUsers", "Error getting users: ", exception) // Log errors if any
        }
    }

    private fun filterUsers(query: String) {
        adapter.clear() // Clear the current displayed items
        val filteredUsers = allUsers.filter { user ->
            user.username.contains(query, ignoreCase = true) // Filter based on username
        }
        for (user in filteredUsers) {
            adapter.add(UserItem(user)) // Add filtered users to the adapter
        }
    }
}



class UserItem(private val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // Set the user data to the view
        viewHolder.itemView.findViewById<TextView>(R.id.container_name).text = user.username
        viewHolder.itemView.findViewById<ImageButton>(R.id.container_add).setOnClickListener {
            val intent = Intent(it.context, ChatPage::class.java)
            intent.putExtra("USER_ID", user.id) // Pass the user ID
            it.context.startActivity(intent)
        }

        // Load the profile picture using Glide
        Glide.with(viewHolder.itemView)
            .load(user.profilePic)
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.container_image))
    }

    override fun getLayout(): Int {
        return R.layout.newuser_container
    }
}


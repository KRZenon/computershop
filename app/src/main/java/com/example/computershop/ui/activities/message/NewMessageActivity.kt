package com.example.computershop.ui.activities.message

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.computershop.R
import com.example.computershop.databinding.ActivityNewMessageBinding
import com.example.computershop.firestore.FirestoreClass
import com.example.computershop.models.User
import com.example.computershop.ui.activities.BaseActivity
import com.example.computershop.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder

class NewMessageActivity : BaseActivity() {
    private var binding: ActivityNewMessageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()
        fetchUser()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun fetchUser() {
        val adapter = GroupAdapter<ViewHolder>()
        FirebaseFirestore.getInstance().collection(Constants.USERS)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                snapshot?.documents?.forEach { document ->
                    val user = document.toObject(User::class.java)!!
                    if (user.id != FirestoreClass().getCurrentUserId()) {
                        adapter.add(UserItem(user))
                    }
                }

            }
        adapter.setOnItemClickListener(onItemClickListener)
        binding?.recyclerviewNewMessage?.layoutManager =
            LinearLayoutManager(this@NewMessageActivity)
        binding?.recyclerviewNewMessage?.adapter = adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>

    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is UserItem) {
            val intent = Intent(this@NewMessageActivity, ChatLogActivity::class.java)
            intent.putExtra(Constants.USER_INFO, item.user)
            startActivity(intent)
            finish()
        }
        false
    }

    class UserItem(val user: User) : Item<ViewHolder>() {
        @SuppressLint("SetTextI18n")
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.user_name).text =
                "${user.firstName} ${user.lastName}"
            if (user.image != "") {
                Picasso.get().load(user.image)
                    .into(viewHolder.itemView.findViewById<ImageView>(R.id.iv_user_icon))
            }
        }

        override fun getLayout(): Int {
            return R.layout.user_item_row
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarNewMessageActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@NewMessageActivity,
                    R.drawable.app_gradient_color_background
                )
            )
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        binding?.toolbarNewMessageActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

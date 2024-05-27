package com.example.appproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityTransactionDetailBinding

class TransactionDetailActivity : Activity() {
    private lateinit var binding: ActivityTransactionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val event = intent.getStringExtra("event")


        binding.transactionName.text = name
        binding.transactionEvent.text = event

        binding.exitBtn.setOnClickListener {
            finish()
        }
    }
}

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
        val amount = intent.getStringExtra("amount")
        val card = intent.getStringExtra("card")
        val type = intent.getStringExtra("type")


        binding.transactionName.text = "이름 : " + name
        binding.transactionEvent.text = "가격 : " + amount
        binding.transactionCard.text = "가격 : " + card
        if (type == "food")
            binding.transactionType.text = "분야 : " + "식비"
        else
            binding.transactionType.text = "분야 : " + "여가"


        binding.exitBtn.setOnClickListener {
            finish()
        }
    }
}

package com.example.appproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityCategoryBinding
import java.util.Date

//분야별 소비량 확인 액티비티
class CategoryActivity : AppCompatActivity() {
    private lateinit var transactionsList: MutableList<TransactionItem>
    private lateinit var filterdtransactionsList: MutableList<TransactionItem>
    private var date: Date = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="분야별 소비량"

        var play_out=0//총 여가비
        var food_out=0//총 식비

        transactionsList = JsonUtil.readJsonFromFile(this, "transactions.json")
        filterdtransactionsList = JsonUtil.filterTransactionsByDate(transactionsList, date)

        JsonUtil.filterTransactions(filterdtransactionsList,"","play").forEach {
            play_out += it.amount
        }
        JsonUtil.filterTransactions(filterdtransactionsList,"","food").forEach {
            food_out += it.amount
        }

        binding.playOutcome.text=play_out.toString()
        binding.foodOutcome.text=food_out.toString()

        binding.back.setOnClickListener{
            finish()
        }
    }
}
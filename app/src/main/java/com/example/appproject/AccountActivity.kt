package com.example.appproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityAccountBinding
import java.util.Date

//계좌별 소비량 확인 액티비티
class AccountActivity : AppCompatActivity() {
    private lateinit var transactionsList: MutableList<TransactionItem>
    private lateinit var filterdtransactionsList: MutableList<TransactionItem>
    private var date: Date = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="계좌별 소비량"

        var xx=0//xx카드 총 소비량
        var bb=0//블랙은행 계좌 총 소비량

        transactionsList = JsonUtil.readJsonFromFile(this, "transactions.json")
        filterdtransactionsList = JsonUtil.filterTransactionsByDate(transactionsList, date)

        JsonUtil.filterTransactions(filterdtransactionsList,"XX Card","").forEach {
            xx += it.amount
        }
        JsonUtil.filterTransactions(filterdtransactionsList,"Black Card","").forEach {
            bb += it.amount
        }

        binding.xxcardOutcome.text=xx.toString()
        binding.blackbankOutcome.text=bb.toString()

        binding.back.setOnClickListener{
            finish()
        }
    }
}
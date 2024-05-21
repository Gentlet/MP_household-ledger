package com.example.appproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityAccountBinding

//계좌별 소비량 확인 액티비티
class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="계좌별 소비량"

        val xx=intent.getIntExtra("xx",0)//xx카드 총 소비량
        val bb=intent.getIntExtra("bb",0)//블랙은행 계좌 총 소비량

        binding.xxcardOutcome.text=xx.toString()
        binding.blackbankOutcome.text=bb.toString()

        binding.back.setOnClickListener{
            finish()
        }
    }
}
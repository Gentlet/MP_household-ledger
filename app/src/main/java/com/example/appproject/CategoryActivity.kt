package com.example.appproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityCategoryBinding

//분야별 소비량 확인 액티비티
class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="분야별 소비량"

        val play_out=intent.getIntExtra("play",0)//총 여가비
        val food_out=intent.getIntExtra("food",0)//총 식비

        binding.playOutcome.text=play_out.toString()
        binding.foodOutcome.text=food_out.toString()

        binding.back.setOnClickListener{
            finish()
        }
    }
}
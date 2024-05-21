package com.example.appproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appproject.databinding.ActivityChobocBinding

//목표 한도 소비량 변경 액티비티

class ChobocActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityChobocBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "목표 한도 소비량 변경"

        var setnew:Int? //새로운 목표 한도 소비량

        //목표 한도 소비량을 바꾸지 않고 뒤로 가기
        binding.back.setOnClickListener{
            finish()
        }

        //목표 한도 소비량 변경
        binding.set.setOnClickListener{
            setnew = binding.newob.text.toString().toIntOrNull()
            val resultIntent = intent
            resultIntent.putExtra("EXTRA_RETURN_VALUE", setnew)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
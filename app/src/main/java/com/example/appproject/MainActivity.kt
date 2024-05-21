package com.example.appproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appproject.databinding.ActivityChobocBinding
import com.example.appproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    lateinit var startForResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="메인 메뉴"

        //한도 도달 여부 확인 함수
        fun inobject(o:Int, r:Int){   //o:목표 한도 소비량  &  r:실제 총 소비량
            if(r>=o){
                binding.oboc.setTextColor(Color.RED)
            }
            else{
                binding.oboc.setTextColor(Color.BLUE)
            }
        }

        //계좌 및 분야별 소비량 초기화
        var blackbank_food:Int=14000   //블랙은행 식비 14000원
        var blackbank_play:Int=7000    //블랙은행 여가비 7000원
        var xxcard_food:Int=19000      //XX카드 식비 19000원
        var xxcard_play:Int=10000      //XX카드 여가비 10000원

        //이번달 총 소비량
        var monthoutcome:Int=xxcard_food+xxcard_play+blackbank_play+blackbank_food
        binding.monthoc.text=monthoutcome.toString()

        //목표 한도 소비량(70000원으로 초기화)
        var objectoutcome:Int=70000
        binding.oboc.text=objectoutcome.toString()
        inobject(objectoutcome,monthoutcome)

        //--------------------------------------------------------
        //다른 메뉴(액티비티)로 이동하기

        //분야별 소비량 확인하기
        binding.cate.setOnClickListener{
            val intent=Intent(this,CategoryActivity::class.java)
            intent.putExtra("play",xxcard_play+blackbank_play)
            intent.putExtra("food",xxcard_food+blackbank_food)
            startActivity(intent)
        }
        //계좌별 소비량 확인하기
        binding.acco.setOnClickListener {
            val intent=Intent(this,AccountActivity::class.java)
            intent.putExtra("xx",xxcard_food+xxcard_play)
            intent.putExtra("bb",blackbank_food+blackbank_play)
            startActivity(intent)
        }
        //목표 한도 소비량 바꾸기
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                objectoutcome = result.data?.getIntExtra("EXTRA_RETURN_VALUE", objectoutcome) ?: 0
                binding.oboc.text=objectoutcome.toString()
                inobject(objectoutcome, monthoutcome)
            }
        }
        binding.choboc.setOnClickListener {
            val intent=Intent(this,ChobocActivity::class.java)
            startForResult.launch(intent)
        }

        //-----------------------------------------------------------
        //결제 시뮬레이터

        //xx카드로 식비 3000원 결제하기
        binding.xxFood3000.setOnClickListener { 
            xxcard_food+=3000
            monthoutcome+=3000
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //xx카드로 여가비 8500원 결제하기
        binding.xxPlay8500.setOnClickListener {
            xxcard_play+=8500
            monthoutcome+=8500
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //블랙은행 계좌로 식비 5500원 결제하기
        binding.bbFood5500.setOnClickListener {
            blackbank_food+=5500
            monthoutcome+=5500
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //블랙은행 계좌로 여가비 7000원 결제하기
        binding.bbPlay7000.setOnClickListener {
            blackbank_play+=7000
            monthoutcome+=7000
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
    }
}
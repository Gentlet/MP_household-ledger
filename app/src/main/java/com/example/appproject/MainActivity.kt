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
import java.util.Date

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var transactionsList: MutableList<TransactionItem>
    private lateinit var filterdtransactionsList: MutableList<TransactionItem>
    private var date: Date = Date()

    //계좌 및 분야별 소비량 초기화
    var blackbank_food:Int=0   //블랙은행 식비 14000원
    var blackbank_play:Int=0    //블랙은행 여가비 7000원
    var xxcard_food:Int=0      //XX카드 식비 19000원
    var xxcard_play:Int=0      //XX카드 여가비 10000원

    var monthoutcome:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="메인 메뉴"


//        val intent=Intent(this,Calendar::class.java)
//        startActivity(intent)

        //한도 도달 여부 확인 함수
        fun inobject(o:Int, r:Int){   //o:목표 한도 소비량  &  r:실제 총 소비량
            if(r>=o){
                binding.oboc.setTextColor(Color.RED)
            }
            else{
                binding.oboc.setTextColor(Color.BLUE)
            }
        }

        LoadTrasactions()


        //목표 한도 소비량(70000원으로 초기화)
        var objectoutcome:Int=70000
        binding.oboc.text=objectoutcome.toString()
        inobject(objectoutcome,monthoutcome)

        //--------------------------------------------------------
        //다른 메뉴(액티비티)로 이동하기

        //분야별 소비량 확인하기
        binding.cate.setOnClickListener{
            val intent=Intent(this,CategoryActivity::class.java)
            startActivity(intent)
        }
        //계좌별 소비량 확인하기
        binding.acco.setOnClickListener {
            val intent=Intent(this,AccountActivity::class.java)
            startActivity(intent)
        }
        //계좌별 소비량 확인하기
        binding.calendar.setOnClickListener {
            val intent=Intent(this,Calendar::class.java)
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
            filterdtransactionsList.add(TransactionItem("XX card Food",3000,date,"XX Card", "food"))
            JsonUtil.writeJsonToFile(this, filterdtransactionsList, "transactions.json")
            LoadTrasactions()
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //xx카드로 여가비 8500원 결제하기
        binding.xxPlay8500.setOnClickListener {
            filterdtransactionsList.add(TransactionItem("XX card Play",8500,date,"XX Card", "play"))
            JsonUtil.writeJsonToFile(this, filterdtransactionsList, "transactions.json")
            LoadTrasactions()
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //블랙은행 계좌로 식비 5500원 결제하기
        binding.bbFood5500.setOnClickListener {
            filterdtransactionsList.add(TransactionItem("Black Card Play",5500,date,"Black Card", "food"))
            JsonUtil.writeJsonToFile(this, filterdtransactionsList, "transactions.json")
            LoadTrasactions()
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
        //블랙은행 계좌로 여가비 7000원 결제하기
        binding.bbPlay7000.setOnClickListener {
            filterdtransactionsList.add(TransactionItem("Black Card Play",7000,date,"Black Card", "play"))
            JsonUtil.writeJsonToFile(this, filterdtransactionsList, "transactions.json")
            LoadTrasactions()
            binding.monthoc.text=monthoutcome.toString()
            inobject(objectoutcome,monthoutcome)
        }
    }

    fun LoadTrasactions() {
        xxcard_play = 0
        xxcard_food = 0
        blackbank_play = 0
        blackbank_food = 0

        transactionsList = JsonUtil.readJsonFromFile(this, "transactions.json")
        filterdtransactionsList = JsonUtil.filterTransactionsByDate(transactionsList, date)

        JsonUtil.filterTransactions(filterdtransactionsList,"XX Card","play").forEach {
            xxcard_play += it.amount
        }
        JsonUtil.filterTransactions(filterdtransactionsList,"XX Card","food").forEach {
            xxcard_food += it.amount
        }
        JsonUtil.filterTransactions(filterdtransactionsList,"Black Card","play").forEach {
            blackbank_play += it.amount
        }
        JsonUtil.filterTransactions(filterdtransactionsList,"Black Card","food").forEach {
            blackbank_food += it.amount
        }

        //이번달 총 소비량
        monthoutcome =xxcard_food+xxcard_play+blackbank_play+blackbank_food
        binding.monthoc.text=monthoutcome.toString()
    }
}
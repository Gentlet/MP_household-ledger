package com.example.appproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appproject.databinding.ActivityCalendarBinding
import com.example.appproject.databinding.CalendarDayItemBinding
import com.example.appproject.databinding.CalendarTransactionItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

interface DayClickListener {
    fun onDayClick(position: Int)
}

interface TransactionClickListener {
    fun onTransactionClick(position: Int)
}

class Calendar : AppCompatActivity(), DayClickListener, TransactionClickListener {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var daysList: List<DayItem>
    private lateinit var transactionsList: List<TransactionItem>
    private var date: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.month.text = getFormattedDate(date)
        binding.leftMonth.text = " < "
        binding.rightMonth.text = " > "

        binding.leftMonth.setOnClickListener {
            setDateToPrevMonth()
        }
        binding.rightMonth.setOnClickListener {
            setDateToNextMonth()
        }


        transactionsList = JsonUtil.filterTransactionsByDate(JsonUtil.readJsonFromFile(this, "transactions.json"), date)


        // 상단의 달력 RecyclerView 설정
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)
        daysList = generateDaysList()
        val calendarAdapter = CalendarAdapter(daysList, this)
        binding.recyclerView.adapter = calendarAdapter

        // 하단의 목록 RecyclerView 설정
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val transAdapter = TransactionsAdapter(transactionsList, this)
        binding.recyclerViewList.adapter = transAdapter
    }

    private fun generateDaysList(): List<DayItem> {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val days = mutableListOf<DayItem>()
        for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            days.add(DayItem(JsonUtil.filterTransactionsByDate(transactionsList, calendar.time, i)))
        }
        return days
    }

    private fun setDateToNextMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        date = calendar.time
        binding.month.text = getFormattedDate(date)

        // Update the transactions list based on the new date
        transactionsList = JsonUtil.filterTransactionsByDate(JsonUtil.readJsonFromFile(this, "transactions.json"), date)

        daysList = generateDaysList()
        val calendarAdapter = CalendarAdapter(daysList, this)
        binding.recyclerView.adapter = calendarAdapter

        val transAdapter = TransactionsAdapter(transactionsList, this)
        binding.recyclerViewList.adapter = transAdapter
    }
    private fun setDateToPrevMonth() {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, -1)
        date = calendar.time
        binding.month.text = getFormattedDate(date)

        // Update the transactions list based on the new date
        transactionsList = JsonUtil.filterTransactionsByDate(JsonUtil.readJsonFromFile(this, "transactions.json"), date)

        daysList = generateDaysList()
        val calendarAdapter = CalendarAdapter(daysList, this)
        binding.recyclerView.adapter = calendarAdapter

        val transAdapter = TransactionsAdapter(transactionsList, this)
        binding.recyclerViewList.adapter = transAdapter
    }

    private fun getFormattedDate(date: Date, format:String = "yyyy.MM"): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun onDayClick(position: Int) {
        val calendarAdapter = binding.recyclerView.adapter as CalendarAdapter
        calendarAdapter.setSelectedPosition(position)

        val transAdapter = TransactionsAdapter(daysList[position].transactionList, this)
        binding.recyclerViewList.adapter = transAdapter
    }

    override fun onTransactionClick(position: Int) {
        val transAdapter = binding.recyclerViewList.adapter as TransactionsAdapter
        transAdapter.setSelectedPosition(position)
    }
}

data class DayItem(val transactionList: List<TransactionItem>)

class CalendarAdapter(
    private val daysList: List<DayItem>,
    private val dayClickListener: DayClickListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val dayItem = daysList[position]
        holder.bind(dayItem, dayClickListener, position)
        holder.binding.layout.setBackgroundColor(
            if (position == selectedPosition) Color.parseColor("#3F95FF") else Color.parseColor("#FFFFFF")
        )
    }

    override fun getItemCount(): Int = daysList.size

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }


    class CalendarViewHolder(val binding: CalendarDayItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dayItem: DayItem, clickListener: DayClickListener, position: Int) {
            binding.tvDate.text = (position + 1).toString()
            binding.income.text = "+ " + getAllIncome(dayItem.transactionList).toString()
            binding.spending.text = "- " + getAllSpending(dayItem.transactionList).toString()
            binding.root.setOnClickListener {
                clickListener.onDayClick(position)
            }
        }

        private fun getAllIncome(transactionList: List<TransactionItem>): Int {
            var result = 0
            for (trans in transactionList) {
                if(trans.amount > 0)
                    result += trans.amount
            }

            return result
        }
        private fun getAllSpending(transactionList: List<TransactionItem>): Int {
            var result = 0
            for (trans in transactionList) {
                if(trans.amount < 0)
                    result += trans.amount
            }

            return result
        }
    }
}

class TransactionsAdapter(
    private val transactionsList: List<TransactionItem>,
    private val transactionClickListener: TransactionClickListener
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = CalendarTransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transactionItem = transactionsList[position]
        holder.bind(transactionItem, transactionClickListener, position)
        holder.binding.layout.setBackgroundColor(
            if (position == selectedPosition) Color.parseColor("#3F95FF") else Color.parseColor("#FFFFFF")
        )
    }

    override fun getItemCount(): Int = transactionsList.size

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    class TransactionViewHolder(val binding: CalendarTransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trans: TransactionItem, clickListener: TransactionClickListener, position: Int) {
            binding.name.text = "${trans.name} : ${trans.amount}"
            binding.root.setOnClickListener {
                clickListener.onTransactionClick(position)

                val context = binding.root.context

                val intent = Intent(context, TransactionDetailActivity::class.java).apply {
                    putExtra("name", trans.name)
                    putExtra("amount", trans.amount.toString())
                    putExtra("card", trans.card)
                    putExtra("type", trans.type)
                }
                context.startActivity(intent)
            }
        }
    }
}
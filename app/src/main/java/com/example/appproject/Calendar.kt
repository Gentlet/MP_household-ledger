package com.example.appproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appproject.databinding.ActivityCalendarBinding
import com.example.appproject.databinding.CalendarDayItemBinding
import com.example.appproject.databinding.CalendarTransactionItemBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단의 달력 RecyclerView 설정
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)
        daysList = generateDaysList()
        val calendarAdapter = CalendarAdapter(daysList, this)
        binding.recyclerView.adapter = calendarAdapter

        // 하단의 목록 RecyclerView 설정
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this)
        transactionsList = generateTransactionsList()
        val transAdapter = TransactionsAdapter(transactionsList, this)
        binding.recyclerViewList.adapter = transAdapter
    }

    private fun generateDaysList(): List<DayItem> {
        val days = mutableListOf<DayItem>()
        for (i in 1..31) {
            days.add(DayItem(i.toString(), "Event $i"))
        }
        return days
    }

    private fun generateTransactionsList(): List<TransactionItem> {
        val transactions = mutableListOf<TransactionItem>()
        for (i in 1..20) {
            transactions.add(TransactionItem(i.toString(), "Event $i"))
        }
        return transactions
    }

    override fun onDayClick(position: Int) {
        val calendarAdapter = binding.recyclerView.adapter as CalendarAdapter
        calendarAdapter.setSelectedPosition(position)
    }

    override fun onTransactionClick(position: Int) {
        val transAdapter = binding.recyclerViewList.adapter as TransactionsAdapter
        transAdapter.setSelectedPosition(position)
    }
}

data class DayItem(val date: String, val event: String)

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
            binding.tvDate.text = dayItem.date
            binding.root.setOnClickListener {
                clickListener.onDayClick(position)
            }
        }
    }
}

data class TransactionItem(val name: String, val event: String)

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
            binding.name.text = "${trans.name} : ${trans.event}"
            binding.root.setOnClickListener {
                clickListener.onTransactionClick(position)
            }
        }
    }
}

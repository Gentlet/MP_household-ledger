package com.example.appproject


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appproject.databinding.ActivityCalendarBinding
import com.example.appproject.databinding.CalendarDayItemBinding

interface DayClickListener {
    fun onDayClick(dayItem: DayItem)
}

class Calendar : AppCompatActivity(), DayClickListener {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var daysList: List<DayItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 날짜 선택 리스너 설정
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)

        daysList = generateDaysList()
        val adapter = CalendarAdapter(daysList, this)
        binding.recyclerView.adapter = adapter
    }

    private fun generateDaysList(): List<DayItem> {
        val days = mutableListOf<DayItem>()
        for (i in 1..31) {
            days.add(DayItem(i.toString(), "Event $i"))
        }
        return days
    }

    override fun onDayClick(dayItem: DayItem) {
        for (day in daysList) {
            day.binding.layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        // 날짜 클릭 시 처리할 이벤트
        dayItem.binding.layout.setBackgroundColor(Color.parseColor("#3F95FF"))
        // Toast.makeText(this, "Clicked date: ${dayItem.date}", Toast.LENGTH_SHORT).show()
    }
}

data class DayItem(val date: String, val event: String){
    lateinit var binding:CalendarDayItemBinding
}

class CalendarAdapter(
    private val daysList: List<DayItem>,
    private val dayClickListener: DayClickListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val dayItem = daysList[position]
        holder.bind(dayItem, dayClickListener)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    class CalendarViewHolder(val binding: CalendarDayItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dayItem: DayItem, clickListener: DayClickListener) {
            dayItem.binding = binding

            binding.tvDate.text = dayItem.date
            binding.root.setOnClickListener {
                clickListener.onDayClick(dayItem)
            }
        }
    }
}
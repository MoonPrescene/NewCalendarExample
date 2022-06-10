package com.example.calendarexample2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendarexample2.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), CalendarMonthAdapter.OnItemCalendarListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarMonthAdapter: CalendarMonthAdapter
    private var month: Int = 6
    private var year: Int = 2022

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setListMonthView()
        calendarMonthAdapter.setMonthYear(6, 2022)
        binding.apply {
            nextButton.setOnClickListener {
                scrollToNextMonth()
            }

            previousButton.setOnClickListener {
                scrollToPreviousMonth()
            }
        }
    }

    private fun setListMonthView() {
        calendarMonthAdapter = CalendarMonthAdapter(mutableListOf(), this)
        binding.recyclerViewMonth.apply {
            adapter = calendarMonthAdapter
            layoutManager = GridLayoutManager(context, 7)
        }
    }

    private fun scrollToNextMonth(){
        if (month < 12){
            month += 1
            calendarMonthAdapter.setMonthYear(month, year)
        }
    }

    private fun scrollToPreviousMonth(){
        if (month > 1){
            month -= 1
            calendarMonthAdapter.setMonthYear(month, year)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDaySelected(day: Int) {
        calendarMonthAdapter.daySelected = day
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSelected(date: Date, day: Int) {
        var dayOfWeek: String = ""
        for (day in DAYOFWEEK.values()){
            if (getDayOfWeek(date) == day.ordinal){
                dayOfWeek = day.foo()
            }
        }
        binding.dateTextView.text = "$dayOfWeek, ngày $day tháng $month năm $year"

    }

    @SuppressLint("SimpleDateFormat")
    private fun convertToCustomFormat(dateStr: String?): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val destFormat = SimpleDateFormat("dd-MM-yyyy")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate)
    }

    private fun getDayOfWeek(date: Date): Int{
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return if (dayOfWeek - 2 == -1){
            6
        }else{
            dayOfWeek - 2
        }
    }

}

enum class DAYOFWEEK(){
    MONDAY(){
        override fun foo(): String {
            return "Thứ Hai"
        }
    },
    TUESDAY(){
        override fun foo(): String {
            return "Thứ Ba"
        }
    },
    WEDNESDAY(){
        override fun foo(): String {
            return "Thứ Tư"
        }
    },
    THURSDAY(){
        override fun foo(): String {
            return "Thứ Năm"
        }
    },
    FRIDAY(){
        override fun foo(): String {
            return "Thứ Sáu"
        }
    },
    SATURDAY(){
        override fun foo(): String {
            return "Thứ Bảy"
        }
    },
    SUNDAY(){
        override fun foo(): String {
            return "Chủ nhật"
        }
    };

    abstract fun foo(): String
}
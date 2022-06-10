package com.example.calendarexample2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarexample2.databinding.ItemCalendarLayoutBinding
import java.util.*

class CalendarMonthAdapter(
    var listDayOfMonth: MutableList<Int>,
    private val onItemCalendarListener: OnItemCalendarListener,
    var daySelected: Int = 0,
    var isCurrentMonth: Boolean = true,
    var isCurrentYear: Boolean = true,
    var month: Int = 0,
    var year: Int = 0,
    private var currentDay: Int = 0
): RecyclerView.Adapter<CalendarMonthAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ItemCalendarLayoutBinding,
        private val onItemCalendarListener: OnItemCalendarListener
        ): RecyclerView.ViewHolder(binding.root){
            @SuppressLint("NotifyDataSetChanged")
            fun bindView(day: Int){
                binding.apply {
                    if (day != 0){
                        textViewDay.text = "$day"
                    }else{
                        textViewDay.text = ""
                    }

                    if (isCurrentMonth && isCurrentYear && currentDay != 0 && currentDay == day){
                        cardViewParent.setCardBackgroundColor(
                            ContextCompat.getColor(binding.root.context, R.color.red)
                        )
                    }

                    cardViewChild.setOnClickListener {
                        daySelected = day
                        notifyDataSetChanged()
                        onItemCalendarListener.onDaySelected(day)
                        onItemCalendarListener
                            .onDateSelected(getCurrentDateFrom(daySelected ,month, year), day)
                    }

                    if (daySelected == day && daySelected != 0){
                        cardViewChild.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.selectedDayColor)
                        )
                    }else{
                        cardViewChild.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.white)
                        )
                    }
                }
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setMonthYear(m: Int, y: Int){
        month = m
        year = y
        isCurrentMonth = m == getCurrentMonth()
        isCurrentYear = y == getCurrentYear()
        currentDay = getCurrentDay()
        if (isCurrentMonth && isCurrentYear){
            daySelected = currentDay
        }else{
            daySelected = 1
        }

        onItemCalendarListener.onDateSelected(getCurrentDateFrom(daySelected ,month, year), daySelected)
        listDayOfMonth = getDayOfMonthListOf(month, year)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDayMonthYear(d: Int, m: Int, y: Int){
        month = m
        year = y
        isCurrentMonth = m == getCurrentMonth()
        isCurrentYear = y == getCurrentYear()
        currentDay = getCurrentDay()
        daySelected = d
        notifyDataSetChanged()
    }

    private fun getDayOfMonthListOf(month: Int, year: Int): MutableList<Int>{
        val dayListInCalendar: MutableList<Int> = mutableListOf()
        val firstDate = firstDateOf(month, year)
        val firstDateDayOfWeek = getDayOfWeek(firstDate)
        val lengthOfMonth = getNumberDayOfMonth(month, year)

        var numberItemOfCalendar = firstDateDayOfWeek + getNumberDayOfMonth(month, year)
        numberItemOfCalendar = if (numberItemOfCalendar <= 28){
            28
        }else if (numberItemOfCalendar <= 35){
            35
        }else{
            42
        }

        (0 until numberItemOfCalendar).forEach{
            if (it in firstDateDayOfWeek until firstDateDayOfWeek + lengthOfMonth){
                dayListInCalendar.add(it - firstDateDayOfWeek + 1)
            }else{
                dayListInCalendar.add(0)
            }
        }

        return dayListInCalendar

    }

    private fun getNumberDayOfMonth(month: Int, year: Int): Int{
        val lengthOfMonthList = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val lengthOfMonthList2 = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        return if(year % 4 == 0 && year % 100 != 0){
            lengthOfMonthList2[month - 1]
        }else{
            lengthOfMonthList[month - 1]
        }
    }

    private fun firstDateOf(month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time
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

    private fun getCurrentDay(): Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun getCurrentMonth(): Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }

    private fun getCurrentYear(): Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    private fun getCurrentDateFrom(day: Int, month: Int, year: Int): Date{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return calendar.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCalendarLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_calendar_layout,
            parent,
            false
        )
        return ViewHolder(binding, onItemCalendarListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day: Int = listDayOfMonth[position]
        return holder.bindView(day)
    }

    override fun getItemCount(): Int {
        return listDayOfMonth.size
    }

    interface OnItemCalendarListener{
        fun onDaySelected(day: Int)
        fun onDateSelected(date: Date, day: Int)
    }
}
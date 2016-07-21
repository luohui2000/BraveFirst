package org.brave.util.util

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

class CalendarTool {
  def getToday: String = {
    val now = new Date()
    val format = new SimpleDateFormat("yyyyMMdd")
    val today = format.format(now)
    today
  }

  def getYesterday: String = {
    val format = new SimpleDateFormat("yyyyMMdd")
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    val yesterday = format.format(cal.getTime)
    yesterday
  }
}
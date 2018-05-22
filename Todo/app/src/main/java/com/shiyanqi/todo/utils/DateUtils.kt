package com.shiyanqi.todo.utils

import org.joda.time.DateTime

import java.util.Calendar
import java.util.Date

/**
 * Created by hcl on 15/8/8.
 */
object DateUtils {

    private val WEEK_DAYS = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    fun fromatShortTime(time: Long): String {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - time < 1000 * 60) {
            return "刚刚"
        }

        // 当前时间
        val now = DateTime()

        val nowYear = now.getYear()
        val nowWeekOfYear = now.getWeekOfWeekyear()
        val nowHourOfDay = now.getHourOfDay()
        val nowDayOfYear = now.getDayOfYear()

        //输入时间
        val dateTime = DateTime(time)

        val dayOfYear = dateTime.getDayOfYear()
        val year = dateTime.getYear()
        val weekOfYear = dateTime.getWeekOfWeekyear()
        val month = dateTime.getMonthOfYear()
        val day = dateTime.getDayOfMonth()
        var weekDay = dateTime.getDayOfWeek()
        if (weekDay == 7) {
            weekDay = 0
        }

        val sb = StringBuilder()

        // 日期
        if (nowDayOfYear != dayOfYear) {
            if (nowYear == year && nowDayOfYear - dayOfYear == 1 || nowDayOfYear == 1 && nowYear - year == 1 && month == 12 && day == 31) {
                // 昨天
                sb.append("昨天")
            } else if (weekOfYear == nowWeekOfYear) {
                // 同一周
                sb.append(WEEK_DAYS[weekDay])
            } else {
                if (nowYear != year) {
                    sb.append(year).append("年")
                }
                sb.append(month).append("月").append(day).append("日")
            }
            return sb.toString()
        }

        val hourOfDay = dateTime.getHourOfDay()
        val minute = dateTime.getMinuteOfHour()

        // 时间
        if (hourOfDay < 6) {
            if (nowHourOfDay >= 6) {
                sb.append("凌晨")
            }
        } else if (hourOfDay < 12) {
            if (nowHourOfDay >= 12 || nowHourOfDay < 6) {
                sb.append("早上")
            }
        } else if (hourOfDay < 13) {
            if (nowHourOfDay != 12) {
                sb.append("中午")
            }
        } else if (hourOfDay < 18) {
            if (nowHourOfDay >= 18 || nowHourOfDay < 13) {
                sb.append("下午")
            }
        } else {
            if (nowHourOfDay < 18) {
                sb.append("晚上")
            }
        }

        if (hourOfDay < 10) {
            sb.append("0")
        }
        sb.append(hourOfDay).append(":")

        if (minute < 10) {
            sb.append("0")
        }
        sb.append(minute)

        return sb.toString()
    }

    fun fromatTime(time: Long): String {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis > time && currentTimeMillis - time < 1000 * 60) {
            return "刚刚"
        }

        // 当前时间
        val now = DateTime()
        val nowYear = now.getYear()
        val nowWeekOfYear = now.getWeekOfWeekyear()
        val nowHourOfDay = now.getHourOfDay()
        val nowDayOfYear = now.getDayOfYear()

        val dateTime = DateTime(time)
        val dayOfYear = dateTime.getDayOfYear()
        val year = dateTime.getYear()
        val weekOfYear = dateTime.getWeekOfWeekyear()
        val month = dateTime.getMonthOfYear()
        val day = dateTime.getDayOfMonth()
        var weekDay = dateTime.getDayOfWeek()
        if (weekDay == 7) {
            weekDay = 0
        }


        //        Calendar cal = Calendar.getInstance();
        //        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //        cal.setTimeInMillis(currentTimeMillis);
        //
        //        int nowYear = cal.get(Calendar.YEAR);
        //        int nowWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        //        int nowHourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        //        int nowDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        //
        //        cal.setTimeInMillis(time);
        //
        //        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        //        int year = cal.get(Calendar.YEAR);
        //        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        //        int month = cal.get(Calendar.MONTH);
        //        int day = cal.get(Calendar.DAY_OF_MONTH);
        //        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        //        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        //        int minute = cal.get(Calendar.MINUTE);

        val sb = StringBuilder()

        // 日期
        if (nowDayOfYear != dayOfYear) {
            if (nowYear == year && nowDayOfYear - dayOfYear == 1 || nowDayOfYear == 1 && nowYear - year == 1 && month == 12 && day == 31) {
                // 昨天
                sb.append("昨天")
            } else if (weekOfYear == nowWeekOfYear) {
                // 同一周
                sb.append(WEEK_DAYS[weekDay])
            } else {
                if (nowYear != year) {
                    sb.append(year).append("年")
                }
                sb.append(month).append("月").append(day).append("日")
            }
            sb.append(" ")
        }

        val hourOfDay = dateTime.getHourOfDay()
        val minute = dateTime.getMinuteOfHour()

        // 时间
        if (hourOfDay < 6) {
            if (nowHourOfDay >= 6) {
                sb.append("凌晨")
            }
        } else if (hourOfDay < 12) {
            if (nowHourOfDay >= 12 || nowHourOfDay < 6) {
                sb.append("早上")
            }
        } else if (hourOfDay < 13) {
            if (nowHourOfDay != 12) {
                sb.append("中午")
            }
        } else if (hourOfDay < 18) {
            if (nowHourOfDay >= 18 || nowHourOfDay < 13) {
                sb.append("下午")
            }
        } else {
            if (nowHourOfDay < 18) {
                sb.append("晚上")
            }
        }

        if (hourOfDay < 10) {
            sb.append("0")
        }
        sb.append(hourOfDay).append(":")

        if (minute < 10) {
            sb.append("0")
        }
        sb.append(minute)

        return sb.toString()
    }


    fun isYesterday(time: Long): Boolean {
        val now = DateTime()
        val nowYear = now.getYear()
        val nowDayOfYear = now.getDayOfYear()

        val dateTime = DateTime(time)
        val dayOfYear = dateTime.getDayOfYear()
        val year = dateTime.getYear()
        val month = dateTime.getMonthOfYear()
        val day = dateTime.getDayOfMonth()
        if (nowDayOfYear != dayOfYear) {
            if (nowYear == year && nowDayOfYear - dayOfYear == 1 || nowDayOfYear == 1 && nowYear - year == 1 && month == 12 && day == 31) {
                // 昨天
                return true
            }
        }
        return false
    }

    val yesterDay: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -1)
            val date = calendar.time
            return date
        }
}

package com.lms.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class để tính toán ngày nghỉ
 */
public class DateUtil {
    
    /**
     * Tính số ngày làm việc giữa 2 ngày (không tính thứ 7, chủ nhật)
     */
    public static int calculateWorkingDays(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            return 0;
        }
        
        int workingDays = 0;
        LocalDate current = fromDate;
        
        while (!current.isAfter(toDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        
        return workingDays;
    }
    
    /**
     * Tính tổng số ngày (bao gồm cả cuối tuần)
     */
    public static int calculateTotalDays(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(fromDate, toDate) + 1;
    }
    
    /**
     * Kiểm tra xem một ngày có phải là ngày làm việc không
     */
    public static boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
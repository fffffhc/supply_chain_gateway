package com.scf.erdos.factoring.service.impl.repaymentPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description : 获取俩个时间点之间的所有月份
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */

public class getAllMonths {
    public static List<String> months(String date1, String date2, Integer interestPayDay) throws ParseException {
        Date startDate = new SimpleDateFormat("yyyy-MM").parse(date1);
        Date endDate = new SimpleDateFormat("yyyy-MM").parse(date2);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        // 获取开始年份和开始月份
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        // 获取结束年份和结束月份
        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        //
        List<String> list = new ArrayList<String>();
        for (int i = startYear; i <= endYear; i++) {
            String date = "";
            if (startYear == endYear) {
                for (int j = startMonth; j <= endMonth; j++) {
                    if (j < 9) {
                        date = i + "-0" + (j + 1);
                    } else {
                        date = i + "-" + (j + 1);
                    }
                    list.add(date + "-" + interestPayDay);
                }

            } else {
                if (i == startYear) {
                    for (int j = startMonth; j < 12; j++) {
                        if (j < 9) {
                            date = i + "-0" + (j + 1);
                        } else {
                            date = i + "-" + (j + 1);
                        }
                        list.add(date + "-" + interestPayDay);
                    }
                } else if (i == endYear) {
                    for (int j = 0; j <= endMonth; j++) {
                        if (j < 9) {
                            date = i + "-0" + (j + 1);
                        } else {
                            date = i + "-" + (j + 1);
                        }
                        list.add(date + "-" + interestPayDay);
                    }
                } else {
                    for (int j = 0; j < 12; j++) {
                        if (j < 9) {
                            date = i + "-0" + (j + 1);
                        } else {
                            date = i + "-" + (j + 1);
                        }
                        list.add(date + "-" + interestPayDay);
                    }
                }
            }
        }
        return list;
    }
}

package com.scf.erdos.factoring.service.impl.repaymentPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description : 获取俩个时间点之间的所有日
 * @author：bao-clm
 * @date: 2020/10/14
 * @version：1.0
 */
public class getAllDays {
    /**
     * 俩个时间点之间的天数
     */
    public static Integer days(String day1, String day2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(day1);
        Date end = simpleDateFormat.parse(day2);

        final long nd = 1000 * 24 * 60 * 60;
        Date startDay = new Date(start.getTime() - start.getTime() % nd);
        Date endDay = new Date(end.getTime() - end.getTime() % nd);
        return (int) ((endDay.getTime() - startDay.getTime()) / nd);
    }
}

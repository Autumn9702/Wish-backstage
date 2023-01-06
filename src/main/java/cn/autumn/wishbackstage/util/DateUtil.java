package cn.autumn.wishbackstage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.autumn.wishbackstage.config.Configuration.DATE_FORMAT;
import static cn.autumn.wishbackstage.config.Configuration.THOUSAND;
/**
 * @author Autumn
 * Created in 2023/1/6
 * Description
 */
public final class DateUtil {

    public static String secondToStr(int time) {
        return ymdHmsSdf().format(new Date((long) time * THOUSAND));
    }

    public static String bannedTimeSecondToStr(int time) {
        if (time == 0) {
            return "-";
        }
        return secondToStr(time);
    }

    private static SimpleDateFormat ymdHmsSdf() {
        return new SimpleDateFormat(DATE_FORMAT.ymdHms);
    }
}

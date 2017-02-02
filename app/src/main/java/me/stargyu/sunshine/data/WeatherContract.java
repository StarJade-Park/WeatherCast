package me.stargyu.sunshine.data;

import android.provider.BaseColumns;
import android.text.format.Time;

// 컨트렉트만 했음
public class WeatherContract {

    public static long normalizeDate(long startDate) { // db 날짜 저장할 때 많이 씀
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class LocationEntry implements BaseColumns { // 기본 컬럼 가져옴
        // LocationEntry table, 공통되게 의미 부여
        // 글자들의 나열, 컬럼/스키마 설정
        // 스키마를 알아야 db에 접근할 수 있다
        // 계약/약속, 어떤 데이터를 저장할 것이다.
        // 스키마 선언과 같다. 엔트리-속성(스키마 내용)

        public static final String TABLE_NAME = "location";

        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        public static final String COLUMN_CITY_NAME = "city_name";

        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }

    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";

        public static final String COLUMN_SHORT_DESC = "short_desc";

        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        public static final String COLUMN_HUMIDITY = "humidity";

        public static final String COLUMN_PRESSURE = "pressure";

        public static final String COLUMN_WIND_SPEED = "wind";

        public static final String COLUMN_DEGREES = "degrees";
    }
}
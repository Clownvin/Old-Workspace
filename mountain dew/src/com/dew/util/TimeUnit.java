package com.dew.util;

import java.util.Calendar;

public enum TimeUnit {
	NANOSECOND, MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, YEAR;
	
	public long getValue() {
		switch (this) {
		case NANOSECOND:
			return System.nanoTime();
		case MILLISECOND:
			return System.currentTimeMillis();
		case SECOND:
			return (int) (System.currentTimeMillis() / 1000);
		case MINUTE:
			return Calendar.getInstance().get(Calendar.MINUTE);
		case HOUR:
			return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		case DAY:
			return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		case WEEK:
			return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		case YEAR:
			return Calendar.getInstance().get(Calendar.YEAR);
		}
		return 0;
	}
}

package www.yuntdev.com.pickerviewlibrary.view;

import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import www.yuntdev.com.pickerviewlibrary.R;
import www.yuntdev.com.pickerviewlibrary.adapter.ArrayWheelAdapter;
import www.yuntdev.com.pickerviewlibrary.adapter.NumericWheelAdapter;
import www.yuntdev.com.pickerviewlibrary.lib.WheelView;
import www.yuntdev.com.pickerviewlibrary.listener.OnItemSelectedListener;
import www.yuntdev.com.pickerviewlibrary.utils.ChinaDate;
import www.yuntdev.com.pickerviewlibrary.utils.LunarCalendar;

public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    private WheelView wv_seconds;
    private int gravity;

    private boolean[] type;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;

    private int startYear = DEFAULT_START_YEAR;
    private int endYear = DEFAULT_END_YEAR;
    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY;
    private int currentYear;
    private int textSize = 18;
    int textColorOut;
    int textColorCenter;
    int dividerColor;
    float lineSpacingMultiplier = 1.6F;

    private WheelView.DividerType dividerType;

    private boolean isLunarCalendar = false;

    public WheelTime(View view) {
        super();
        this.view = view;
        type = new boolean[]{true, true, true, true, true, true};
        setView(view);
    }

    public WheelTime(View view, boolean[] type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        setView(view);
    }


    public void setLunarCalendar(boolean isLunarCalendar) {
        this.isLunarCalendar = isLunarCalendar;
    }

    public boolean isLunarCalendar() {
        return isLunarCalendar;
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        if (isLunarCalendar) {
            int[] lunar = LunarCalendar.solarToLunar(year, month + 1, day);
            setLunar(lunar[0], lunar[1], lunar[2], lunar[3] == 1, h, m, s);
        } else {
            setSolar(year, month, day, h, m, s);
        }
    }

    private void setLunar(int year, final int month, int day, boolean isLeap, int h, int m, int s) {
        wv_year = view.findViewById(R.id.year);
        wv_year.setAdapter(new ArrayWheelAdapter(ChinaDate.getYears(startYear, endYear)));
        wv_year.setLabel("");
        wv_year.setCurrentItem(year - startYear);
        wv_year.setGravity(gravity);
        wv_month = view.findViewById(R.id.month);
        wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year)));
        wv_month.setLabel("");
        wv_month.setCurrentItem(month);
        wv_month.setGravity(gravity);
        wv_day = view.findViewById(R.id.day);
        if (ChinaDate.leapMonth(year) == 0) {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month))));
        } else {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year))));
        }
        wv_day.setLabel("");
        wv_day.setCurrentItem(day - 1);
        wv_day.setGravity(gravity);
        wv_hours = view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCurrentItem(h);
        wv_hours.setGravity(gravity);
        wv_mins = view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setCurrentItem(m);
        wv_mins.setGravity(gravity);

        wv_seconds = view.findViewById(R.id.second);
        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));

        wv_seconds.setCurrentItem(m);
        wv_seconds.setGravity(gravity);
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year_num)));
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    wv_month.setCurrentItem(wv_month.getCurrentItem() + 1);
                } else {
                    wv_month.setCurrentItem(wv_month.getCurrentItem());
                }

                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem()))));
                        maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem());
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1))));
                    maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1);
                }

                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index;
                int year_num = wv_year.getCurrentItem() + startYear;
                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && month_num > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num))));
                        maxItem = ChinaDate.monthDays(year_num, month_num);
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num + 1))));
                    maxItem = ChinaDate.monthDays(year_num, month_num + 1);
                }

                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);


        if (type.length != 6) {
            throw new RuntimeException("type[] length is not 6");
        }
        wv_year.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wv_month.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wv_day.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        wv_hours.setVisibility(type[3] ? View.VISIBLE : View.GONE);
        wv_mins.setVisibility(type[4] ? View.VISIBLE : View.GONE);
        wv_seconds.setVisibility(type[5] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private void setSolar(int year, final int month, int day, int h, int m, int s) {
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        /*  final Context context = view.getContext();*/
        currentYear = year;
        wv_year = view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));
        int yea = year - startYear;

        wv_year.setCurrentItem(year - startYear);
        wv_year.setGravity(gravity);
        wv_month = view.findViewById(R.id.month);
        if (startYear == endYear) {
            wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == startYear) {
            wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_month.setCurrentItem(month);
        }
        /*   wv_month.setLabel(context.getString(R.string.pickerview_month));*/

        wv_month.setGravity(gravity);
        wv_day = view.findViewById(R.id.day);

        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {

                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_day.setCurrentItem(day - 1);
        } else {
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {

                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_day.setCurrentItem(day - 1);
        }

        /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/

        wv_day.setGravity(gravity);
        wv_hours = view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCurrentItem(h);
        wv_hours.setGravity(gravity);
        wv_mins = view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setCurrentItem(m);
        wv_mins.setGravity(gravity);
        wv_seconds = view.findViewById(R.id.second);
        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        wv_seconds.setCurrentItem(s);
        wv_seconds.setGravity(gravity);
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                currentYear = year_num;
                int currentMonthItem = wv_month.getCurrentItem();
                if (startYear == endYear) {
                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));

                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + startMonth;

                    if (startMonth == endMonth) {
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else {
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == startYear) {
                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));

                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {
                        setReDay(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        setReDay(year_num, month, 1, 31, list_big, list_little);
                    }

                } else if (year_num == endYear) {
                    wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }

                } else {
                    wv_month.setAdapter(new NumericWheelAdapter(1, 12));
                    setReDay(year_num, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);

                }

            }
        };
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }

                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }

                } else {
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);

                }


            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);
        if (type.length != 6) {
            throw new IllegalArgumentException("type[] length is not 6");
        }
        wv_year.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wv_month.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wv_day.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        wv_hours.setVisibility(type[3] ? View.VISIBLE : View.GONE);
        wv_mins.setVisibility(type[4] ? View.VISIBLE : View.GONE);
        wv_seconds.setVisibility(type[5] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_day.getCurrentItem();

        int maxItem;
        if (list_big
                .contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
                maxItem = endD;
            }
        }

        if (currentItem > wv_day.getAdapter().getItemsCount() - 1) {
            currentItem = wv_day.getAdapter().getItemsCount() - 1;
            wv_day.setCurrentItem(currentItem);
        }
    }


    private void setContentTextSize() {
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);
        wv_seconds.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wv_day.setTextColorOut(textColorOut);
        wv_month.setTextColorOut(textColorOut);
        wv_year.setTextColorOut(textColorOut);
        wv_hours.setTextColorOut(textColorOut);
        wv_mins.setTextColorOut(textColorOut);
        wv_seconds.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wv_day.setTextColorCenter(textColorCenter);
        wv_month.setTextColorCenter(textColorCenter);
        wv_year.setTextColorCenter(textColorCenter);
        wv_hours.setTextColorCenter(textColorCenter);
        wv_mins.setTextColorCenter(textColorCenter);
        wv_seconds.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wv_day.setDividerColor(dividerColor);
        wv_month.setDividerColor(dividerColor);
        wv_year.setDividerColor(dividerColor);
        wv_hours.setDividerColor(dividerColor);
        wv_mins.setDividerColor(dividerColor);
        wv_seconds.setDividerColor(dividerColor);
    }

    private void setDividerType() {

        wv_day.setDividerType(dividerType);
        wv_month.setDividerType(dividerType);
        wv_year.setDividerType(dividerType);
        wv_hours.setDividerType(dividerType);
        wv_mins.setDividerType(dividerType);
        wv_seconds.setDividerType(dividerType);

    }

    private void setLineSpacingMultiplier() {
        wv_day.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_month.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_year.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_hours.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_mins.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_seconds.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        if (isLunarCalendar) {
            return;
        }

        if (label_year != null) {
            wv_year.setLabel(label_year);
        } else {
            wv_year.setLabel(view.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
            wv_month.setLabel(label_month);
        } else {
            wv_month.setLabel(view.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
            wv_day.setLabel(label_day);
        } else {
            wv_day.setLabel(view.getContext().getString(R.string.pickerview_day));
        }
        if (label_hours != null) {
            wv_hours.setLabel(label_hours);
        } else {
            wv_hours.setLabel(view.getContext().getString(R.string.pickerview_hours));
        }
        if (label_mins != null) {
            wv_mins.setLabel(label_mins);
        } else {
            wv_mins.setLabel(view.getContext().getString(R.string.pickerview_minutes));
        }
        if (label_seconds != null) {
            wv_seconds.setLabel(label_seconds);
        } else {
            wv_seconds.setLabel(view.getContext().getString(R.string.pickerview_seconds));
        }
    }

    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
        wv_seconds.setCyclic(cyclic);
    }

    public String getTime() {
        if (isLunarCalendar) {
            return getLunarTime();
        }
        StringBuffer sb = new StringBuffer();
        if (currentYear == startYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((wv_month.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_day.getCurrentItem() + startDay)).append(" ")
                        .append(wv_hours.getCurrentItem()).append(":")
                        .append(wv_mins.getCurrentItem()).append(":")
                        .append(wv_seconds.getCurrentItem());
            } else {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_day.getCurrentItem() + 1)).append(" ")
                        .append(wv_hours.getCurrentItem()).append(":")
                        .append(wv_mins.getCurrentItem()).append(":")
                        .append(wv_seconds.getCurrentItem());
            }


        } else {

            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_month.getCurrentItem() + 1)).append("-")
                    .append((wv_day.getCurrentItem() + 1)).append(" ")
                    .append(wv_hours.getCurrentItem()).append(":")
                    .append(wv_mins.getCurrentItem()).append(":")
                    .append(wv_seconds.getCurrentItem());
        }

        return sb.toString();
    }

    private String getLunarTime() {
        StringBuffer sb = new StringBuffer();
        int year = wv_year.getCurrentItem() + startYear;
        int month = 1;
        boolean isLeapMonth = false;
        if (ChinaDate.leapMonth(year) == 0) {
            month = wv_month.getCurrentItem() + 1;
        } else {
            if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) <= 0) {
                month = wv_month.getCurrentItem() + 1;
            } else if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) == 1) {
                month = wv_month.getCurrentItem();
                isLeapMonth = true;
            } else {
                month = wv_month.getCurrentItem();
            }
        }
        int day = wv_day.getCurrentItem() + 1;
        int[] solar = LunarCalendar.lunarToSolar(year, month, day, isLeapMonth);

        sb.append(solar[0]).append("-")
                .append(solar[1]).append("-")
                .append(solar[2]).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem()).append(":")
                .append(wv_seconds.getCurrentItem());
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public void setRangDate(Calendar startDate, Calendar endDate) {

        if (startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            if (year > startYear) {
                this.endYear = year;
                this.endMonth = month;
                this.endDay = day;
            } else if (year == startYear) {
                if (month > startMonth) {
                    this.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                } else if (month == startMonth) {
                    if (day > startDay) {
                        this.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                    }
                }
            }

        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            if (year < endYear) {
                this.startMonth = month;
                this.startDay = day;
                this.startYear = year;
            } else if (year == endYear) {
                if (month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    this.startYear = year;
                } else if (month == endMonth) {
                    if (day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        this.startYear = year;
                    }
                }
            }

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.get(Calendar.YEAR);
            this.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    public void isCenterLabel(Boolean isCenterLabel) {

        wv_day.isCenterLabel(isCenterLabel);
        wv_month.isCenterLabel(isCenterLabel);
        wv_year.isCenterLabel(isCenterLabel);
        wv_hours.isCenterLabel(isCenterLabel);
        wv_mins.isCenterLabel(isCenterLabel);
        wv_seconds.isCenterLabel(isCenterLabel);
    }
}

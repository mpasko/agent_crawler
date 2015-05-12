/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.facebook;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author marcin
 */
public class DateParser {
    public DateTimeFormatter missingTimeFormatter;
    public CustomFormatter fewMinutesFormatter;
    public DateTimeFormatter missingYearFormatter;
    public DateTimeFormatter fullFormatter;
    public DateTimeFormatter[] formats;
    public DateTimeFormatter monthYearFormatter;
    public DateTimeFormatter dayMonthFormatter;
    public DateTimeFormatter singleMonthFormatter;
    
    public DateParser() {
        initializeDateFormatter();
    }

    private void initializeDateFormatter() {
        fullFormatter = DateTimeFormat.forPattern("dd MMMM yyyy 'at' HH:mm").withLocale(Locale.UK);
        missingYearFormatter = DateTimeFormat.forPattern("dd MMMM 'at' HH:mm").withLocale(Locale.UK);
        missingTimeFormatter = DateTimeFormat.forPattern("dd MMMM yyyy").withLocale(Locale.UK);
        dayMonthFormatter = DateTimeFormat.forPattern("dd MMMM").withLocale(Locale.UK);
        singleMonthFormatter = DateTimeFormat.forPattern("MMMM").withLocale(Locale.UK);
        monthYearFormatter = DateTimeFormat.forPattern("MMMM yyyy").withLocale(Locale.UK);
        fewMinutesFormatter = new CustomFormatter();
        formats = new DateTimeFormatter[]{fullFormatter, missingYearFormatter, missingTimeFormatter, dayMonthFormatter, singleMonthFormatter, monthYearFormatter, fewMinutesFormatter};
    }

    public DateTime parseDate(String dateString) {
        DateTime date = null;
        int i = 0;
        while (date == null) {
            DateTimeFormatter formatter = formats[i];
            try {
                date = formatter.parseDateTime(dateString);
                if (((formatter == missingYearFormatter) || (formatter == dayMonthFormatter)) && (date != null)) {
                    date = date.withYear(DateTime.now().getYear());
                }
            } catch (IllegalArgumentException ex) {
            }
            ++i;
            if (i >= formats.length) {
                throw new UnsupportedOperationException("Unable to parse date: " + dateString);
            }
        }
        return date;
    }
    
}

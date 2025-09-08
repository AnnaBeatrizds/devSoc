package br.com.soc.sistema.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

public class DateConverter extends StrutsTypeConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && !values[0].isEmpty()) {
            try {
                return sdf.parse(values[0]);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            return sdf.format((Date) o);
        }
        return "";
    }
}
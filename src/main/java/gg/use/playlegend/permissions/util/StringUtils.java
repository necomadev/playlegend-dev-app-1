package gg.use.playlegend.permissions.util;

import com.google.common.base.CaseFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

  public static final Pattern PIPE = Pattern.compile("\\|");
  public static final Pattern COLON = Pattern.compile(":");
  public static final Pattern COMMA = Pattern.compile(",");
  public static final Pattern MOJANG_UUID = Pattern.compile(
      "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
  public static final Pattern UUID = Pattern
      .compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
          Pattern.CASE_INSENSITIVE);
  public static final Pattern PUNCTATION = Pattern.compile("\\p{Punct}");
  public static final Pattern SHORT_CHAR = Pattern.compile("[:!'()./;\\[\\]|{}]");

  public static final Map<String, TimeUnit> TIMEUNIT_SHORTCUTS = new HashMap<>();

  private static final Pattern LAST_WORD = Pattern.compile("^.*?(\\w+)\\W*$");

  static {
    TIMEUNIT_SHORTCUTS.put("s", TimeUnit.SECONDS);
    TIMEUNIT_SHORTCUTS.put("m", TimeUnit.MINUTES);
    TIMEUNIT_SHORTCUTS.put("h", TimeUnit.HOURS);
    TIMEUNIT_SHORTCUTS.put("d", TimeUnit.DAYS);
  }

  public static String getDateFromMillis(long millis) {
    return getDateFromMillis(millis, FormatStyle.MEDIUM);
  }

  public static String getDateFromMillis(long millis, FormatStyle format) {
    return DateTimeFormatter.ofLocalizedDateTime(format).withLocale(Locale.GERMANY)
        .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()));
  }

  public static <T> String getStringFromArray(Iterable<T> elements, Function<T, String> converter,
      String delimiter) {
    StringJoiner joiner = new StringJoiner(delimiter);
    for (T element : elements) {
      joiner.add(converter.apply(element));
    }
    return joiner.toString();
  }

  public static <T> String getStringFromArray(T[] elements, Function<T, String> converter,
      String delimiter) {
    StringJoiner joiner = new StringJoiner(delimiter);
    for (T element : elements) {
      joiner.add(converter.apply(element));
    }
    return joiner.toString();
  }

  public static <T> List<T> getListFromString(String source, Pattern delimiter,
      Function<String, T> converter) {
    if (source.isEmpty()) {
      return new ArrayList<>();
    }
    String[] stringElements = delimiter.split(source);
    List<T> elements = new ArrayList<>();
    for (String stringElement : stringElements) {
      elements.add(converter.apply(stringElement));
    }
    return elements;
  }

  public static <T> Set<T> getSetFromString(String source, Pattern delimiter,
      Function<String, T> converter) {
    if (source.isEmpty()) {
      return new HashSet<>();
    }
    String[] stringElements = delimiter.split(source);
    Set<T> elements = new HashSet<>();
    for (String stringElement : stringElements) {
      elements.add(converter.apply(stringElement));
    }
    return elements;
  }

  public static String[] appendStringArrays(String[]... stringArrays) {
    String[] result = new String[stringArrays[0].length];
    for (int i = 0; i < result.length; i++) {
      StringBuilder builder = new StringBuilder(16 * stringArrays.length);
      for (String[] stringArray : stringArrays) {
        builder.append(stringArray[i]);
      }
      result[i] = builder.toString();
    }
    return result;
  }

  public static String removeWhiteSpaces(String string) {
    return string.replaceAll("\\s", "");
  }

  public static String appendAll(String glue, String... input) {
    StringBuilder builder = new StringBuilder(100);
    for (int i = 0; i < input.length; i++) {
      String s = input[i];
      if (i > 0) {
        builder.append(glue);
      }
      builder.append(s);
    }
    return builder.toString();
  }

  public static String getTimeLeft(long end) {
    if (end == -1) {
      return "forever";
    }
    long time = end - System.currentTimeMillis();
    if (time == 0) {
      return "0";
    }
    long seconds = (time / 1000) % 60;
    long minutes = ((time / (1000 * 60)) % 60);
    long hours = ((time / (1000 * 60 * 60)) % 24);
    long days = ((time / (1000 * 60 * 60 * 24)));

    StringBuilder remaining = new StringBuilder();

    boolean spaceNext = false;
    if (days > 0) {
      remaining.append(days).append(" day(s)");
      spaceNext = true;
    }
    if (hours > 0) {
      remaining.append(spaceNext ? " " : "").append(hours).append(" hour(s)");
      spaceNext = true;
    }
    if (minutes > 0) {
      remaining.append(spaceNext ? " " : "").append(minutes).append(" minute(s)");
      spaceNext = true;
    }
    if (seconds > 0) {
      remaining.append(spaceNext ? " " : "").append(seconds).append(" second(s)");
    }
    return remaining.toString();
  }

  public static <T extends Enum<?>> T getEnumFromString(String input, Class<T> en) {
    return Stream.of(en.getEnumConstants()).filter(
            enumConst -> enumConst.name().equalsIgnoreCase(input) || getEnumName(enumConst)
                .equalsIgnoreCase(input)).findFirst()
        .orElse(null);
  }

  public static <T extends Enum<?>> T getEnumFromString(String input, Class<T> en,
      Function<T, String> extraString) {
    return Stream.of(en.getEnumConstants()).filter(
            enumConst -> enumConst.name().equalsIgnoreCase(input) || getEnumName(enumConst)
                .equalsIgnoreCase(input) || extraString.apply(enumConst).equalsIgnoreCase(input))
        .findFirst().orElse(null);
  }

  public static <T extends Enum<?>> List<String> getEnumNameList(T[] enValues) {
    return Stream.of(enValues).map(StringUtils::getEnumName).collect(Collectors.toList());
  }

  public static String getEnumName(Enum<?> en) {
    return getEnumName(en, false);
  }

  public static String getEnumName(Enum<?> en, boolean allowMultiWords) {
    if (en == null) {
      return "null";
    }
    return getReadableEnumName(en.name(), allowMultiWords);
  }

  public static String getReadableEnumName(String name, boolean allowMultiWords) {
    if (name.contains("_")) {
      String[] words = name.split("_");
      StringBuilder builder = new StringBuilder();
      for (String word : words) {
        builder.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, word))
            .append(allowMultiWords ? " " : "");
      }
      return builder.substring(0, allowMultiWords ? builder.length() - 1 : builder.length());
    }
    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
  }

  public static String getReadableSeconds(int totalSecs) {
    int hours = totalSecs / 3600;
    int minutes = (totalSecs % 3600) / 60;
    int seconds = totalSecs % 60;
    if (hours == 0) {
      return String.format("%02d:%02d", minutes, seconds);
    }
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static String getReadableDuration(Duration duration) {
    return duration.toString()
        .substring(2)
        .replaceAll("(\\d[HMS])(?!$)", "$1 ")
        .toLowerCase();
  }

  public static String formatSecondsDiff(int diff) {
    return formatMillisDiff(diff * 1000);
  }

  public static String formatSecondsDiff(int diff, boolean longNames) {
    return formatMillisDiff(diff * 1000, longNames);
  }

  public static String formatMillisDiff(long diff) {
    return formatMillisDiff(diff, true);
  }

  public static String formatMillisDiff(long diff, boolean longNames) {
    return formatMillisDiff(diff, 2, longNames);
  }

  public static String formatMillisDiff(long diff, int accuracy, boolean longNames) {
    Calendar c = new GregorianCalendar();
    c.setTimeInMillis(diff);
    Calendar zero = new GregorianCalendar();
    zero.setTimeInMillis(0);
    return formatDateDiff(zero, c, accuracy, longNames);
  }

  public static String formatDateDiff(long date) {
    return formatDateDiff(date, 2, true);
  }

  public static String formatDateDiff(long date, int accuracy, boolean longNames) {
    Calendar c = new GregorianCalendar();
    c.setTimeInMillis(date);
    Calendar now = new GregorianCalendar();
    return formatDateDiff(now, c, accuracy, longNames);
  }

  public static String formatDateDiff(Calendar fromDate, Calendar toDate, int accuracy,
      boolean longNames) {
    boolean future = false;
    if (toDate.equals(fromDate)) {
      return "now";
    }
    if (toDate.after(fromDate)) {
      future = true;
    }
    StringBuilder sb = new StringBuilder();
    int[] types = new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
        Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
    String[] names =
        longNames ? new String[]{"year", "years", "month", "months", "day", "days", "hour", "hours",
            "minute", "minutes", "second", "seconds"} : new String[]{
            "y", "y", "m", "m", "d", "d", "h", "h", "min", "min", "s", "s"};
    int accurate = 0;
    for (int i = 0; i < types.length; i++) {
      if (accurate > accuracy) {
        break;
      }
      int diff = dateDiff(types[i], fromDate, toDate, future);
      if (diff > 0) {
        accurate++;
        sb.append(" ").append(diff).append(longNames ? " " : "")
            .append(names[i * 2 + (diff > 1 ? 1 : 0)]);
      }
    }
    if (sb.length() == 0) {
      return "now";
    }
    return sb.toString().trim();
  }

  static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
    int diff = 0;
    long savedDate = fromDate.getTimeInMillis();
    while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
      savedDate = fromDate.getTimeInMillis();
      fromDate.add(type, future ? 1 : -1);
      diff++;
    }
    diff--;
    fromDate.setTimeInMillis(savedDate);
    return diff;
  }

}

package cn.autumn.wishbackstage.util;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.ex.DatabaseException;
import org.springframework.lang.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.autumn.wishbackstage.config.Configuration.*;

/**
 * @author cf
 * Created in 2022/10/28
 */
@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public final class Utils {


    /**
     * Gets the language code from a given locale.
     * @param locale A locale.
     * @return A string in the format of 'XX-XX'.
     */
    public static String getLanguageCode(Locale locale) {
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    /**
     * Retrieves a string from an input stream.
     */
    @SuppressWarnings("unchecked")
    public static String readFromInputStream(@Nullable InputStream stream) {
        if (stream == null) return "empty";

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            stream.close();
        } catch (IOException ioe) {
            WishBackstageApplication.getLogger().warn("Failed to read from input stream.");
        } catch (NullPointerException ignored) {
            return "empty";
        }
        return stringBuilder.toString();
    }

    /**
     * Creates a string with the path to a file.
     */
    public static String filePath(String path) {
        return path.replace("/", File.separator);
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Field to db field type.
     * @param f Field
     * @return db type
     */
    @SuppressWarnings("all")
    public static String ofFieldType(Field f) {
        String r;
        String s = f.getType().toString();
        switch (s) {
            case "class java.lang.Integer", "int" -> r = "int(11)";
            case "class java.lang.Long" -> r = "bigint(11)";
            case "class java.lang.String" -> r = "varchar(255)";
            case "class java.lang.Float" -> r = "float";
            case "class java.lang.Double" -> r = "double";
            case "class java.lang.Boolean", "boolean" -> r = "tinyint";
            case "interface java.util.List" -> r = "json";
            default -> throw new DatabaseException("The field type is not supported");
        }
        return r;
    }

    /**
     * Verify field information.
     * @param s The DB field type.
     * @return The DB field type.
     */
    @SuppressWarnings("all")
    public static void verifyField(String s) {
        switch (s) {
            case "int", "bigint", "varchar","float", "double", "tinyint" -> { return; }
            default -> {
                if (s.matches(REGEX_INT) ||
                    s.matches(REGEX_LONG) ||
                    s.matches(REGEX_STRING) ||
                    s.matches(REGEX_FLOAT) ||
                    s.matches(REGEX_DOUBLE)) {
                    return;
                }
            }
        }
        throw new DatabaseException("Field type error: " + s);
    }

    /**
     * Uppercase to underscore letter.
     * @param param The code field.
     * @return The sql field.
     */
    public static String upperCharToUnderLine(String param) {
        Pattern p = Pattern.compile(CAPITAL_LETTERS);
        if (param == null || param.equals(PARAM_EMPTY)) {
            throw new NullPointerException("Field name is null. Unable to convert.");
        }
        StringBuilder b = new StringBuilder(param);
        Matcher m = p.matcher(param);
        int i = 0;
        while (m.find()) {
            b.replace(m.start() + i, m.end() + i, "_" + m.group().toLowerCase());
            i++;
        }

        if (UNDERLINE == b.charAt(0)) {
            b.deleteCharAt(0);
        }
        return b.toString();
    }

}

package cn.autumn.wishbackstage.util;

import cn.autumn.wishbackstage.WishBackstageApplication;
import cn.autumn.wishbackstage.ex.DatabaseException;
import org.springframework.lang.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static cn.autumn.wishbackstage.config.ConfigureContainer.*;

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
        switch (f.getType().toString()) {
            case "class java.lang.Integer" -> r = "int(11)";
            case "class java.lang.Long" -> r = "bigint(11)";
            case "class java.lang.String" -> r = "varchar(255)";
            case "class java.lang.Float" -> r = "float";
            case "class java.lang.Double" -> r = "double";
            case "class java.lang.Boolean" -> r = "tinyint";
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

}

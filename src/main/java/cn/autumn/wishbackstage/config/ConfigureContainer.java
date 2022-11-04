package cn.autumn.wishbackstage.config;

/**
 * @author cf
 * Created in 2022/11/3
 */
public sealed class ConfigureContainer permits Configuration {

    /**
     * Regex
     */
    public static final String REGEX_INT = "int\\(\\d+\\)";
    public static final String REGEX_LONG = "bigint\\(\\d+\\)";
    public static final String REGEX_STRING = "varchar\\(\\d+\\)";
    public static final String REGEX_FLOAT = "float\\(\\d+\\)";
    public static final String REGEX_DOUBLE = "double\\(\\d+\\)";

    public static final String RELATIVE_PATH = "./src/main/java/";

    public static final String FIELD_NAME_ID = "id";

    public static final String TRANSIENT = "transient";

    /**
     * CREATE = A table is added if the table does not exist.
     * UPDATE = Added and deleted when fields change.
     * DELETE = Delete table if entity does not exist.
     * * * * * * * * * * * * * * * * * * * * * * * * *
     * CREATE = CREATE
     * UPDATE = CREATE + UPDATE
     * DELETE = CREATE + UPDATE + DELETE
     */
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

}

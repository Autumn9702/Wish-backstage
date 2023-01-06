package cn.autumn.wishbackstage.config;

import static cn.autumn.wishbackstage.WishBackstageApplication.container;

/**
 * @author cf
 * Created in 2022/11/3
 */
public final class Configuration extends ConfigureContainer {

    public static ConfigureContainer c = container;

    public static final RedisKey REDIS_KEY = container.redisKey;
    public static final HttpIpParam HTTP_IP_PARAM = container.httpIpParam;
    public static final FormatMatch FORMAT_MATCH = container.formatMatch;
    public static final DateFormat DATE_FORMAT = container.dateFormat;

    /**
     * Regex
     */
    public static final String REGEX_INT = "int\\(\\d+\\)";
    public static final String REGEX_LONG = "bigint\\(\\d+\\)";
    public static final String REGEX_STRING = "varchar\\(\\d+\\)";
    public static final String REGEX_FLOAT = "float\\(\\d+\\)";
    public static final String REGEX_DOUBLE = "double\\(\\d+\\)";

    public static final String CAPITAL_LETTERS = "[A-Z]";

    public static final String RELATIVE_PATH = "./src/main/java/";

    public static final String FIELD_NAME_ID = "id";
    public static final String ID_ATTRIBUTE = "NOT NULL AUTO_INCREMENT";

    /**
     * DB
     */
    public static final char UNDERLINE = '_';
    public static final String LOWER_NULL = "null";
    public static final String PARAM_NULL = "NULL";
    public static final String PARAM_NOT_NULL = "NOT NULL";
    public static final String DB_FIELD_IS_NULL = "YES";
    public static final String DB_FIELD_NOT_NULL = "NO";
    public static final String PARAM_EMPTY = "";

    public static final String TRANSIENT = "transient";

    /**
     * DB field type
     */
    public static final String FIELD_TYPE = "FIELD-TYPE";
    public static final String FIELD_NULL = "FIELD-NULL";
    public static final String FIELD_CREATE = "FIELD-CREATE";
    public static final String FIELD_DELETE = "FIELD-DELETE";
    public static final String FIELD_COMMENT = "FIELD-COMMENT";

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

    /**
     * ADD sql or UPDATE sql
     */
    public static final String ADD_SQL = " add";
    public static final String UPDATE_SQL = " modify";
    public static final String DELETE_SQL = " drop";

    /**
     * Request address
     */
    public static final String WISH_PATH = "/wish";
    public static final String API_PATH = "/api";
    public static final String LOGIN_PATH = "/login";

    /**
     * Symbol
     */
    public static final String SLASH = "/";

    /**
     * Redis key
     */
    public static final String ACCOUNT_ONLINE = "online_";
    public static final String ACCOUNT_ONLINE_RSH = "online_refresh_";

    /**
     * Number
     */
    public static final int THOUSAND = 1000;

    public static final int ONLINE_TIME = 3600;
    public static final int ONLINE_TIME_MILLIS = ONLINE_TIME * THOUSAND;
    public static final int ONLINE_TIME_RSH = 5400;

    /**
     * Other
     */
    public static final String LOCALHOST = "127.0.0.1";
    public static final String USER_TYPE = "web";
}

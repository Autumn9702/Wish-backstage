package cn.autumn.wishbackstage;

import ch.qos.logback.classic.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Autumn
 */
@MapperScan("cn.autumn.wishbackstage.mapper")
@SpringBootApplication
public class WishBackstageApplication {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(WishBackstageApplication.class);

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void main(String[] args) {
        SpringApplication.run(WishBackstageApplication.class, args);
    }

}
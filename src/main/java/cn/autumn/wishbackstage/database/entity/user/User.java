package cn.autumn.wishbackstage.database.entity.user;

import cn.autumn.wishbackstage.config.database.FieldAttribute;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author cf
 * Created in 2022/11/1
 */
@TableName(value = "user")
public class User {

    @Getter @Setter @Id
    private Integer id;
//    @Setter private transient Account account;

    @FieldAttribute(name = "nick_name", fieldType = "varchar(30)", isNull = false, comment = "昵称")
    @Getter @Setter private String nickName;
    @Getter @Setter private String signature;
    @Getter @Setter private String headImage;

}

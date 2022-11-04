package cn.autumn.wishbackstage.database.entity.user;


import cn.autumn.wishbackstage.util.Crypto;
import cn.autumn.wishbackstage.util.Utils;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.List;
import java.util.Locale;


/**
 * @author cf
 * Created in 2022/10/31
 */
@TableName(value = "account")
public class Account {

    @Getter @Setter @Id
    private String id;

    @Getter @Setter private String username;
    @Getter @Setter private String password;

    @Setter private String email;
    @Setter private String phone;

    @Getter @Setter private String token;

    @Setter private boolean isBanned;
    @Getter @Setter private int banStartTime;
    @Getter @Setter private int banEndTime;
    @Getter @Setter private String banReason;

    @Getter private List<String> permissions;
    @Getter @Setter private Locale locale;


    public String getEmail() {
        if (email != null && !email.isEmpty()) {
            return email;
        }
        return "";
    }

    public boolean isBanned() {
        if (banEndTime > 0 && banEndTime < System.currentTimeMillis() / 1000) {
            this.isBanned = false;
            this.banStartTime = 0;
            this.banEndTime = 0;
            this.banReason = null;
        }
        return this.isBanned;
    }

    public boolean addPermission(String permission) {
        if (this.permissions.contains(permission)) return false;
        this.permissions.add(permission);
        return true;
    }

    public String generateLoginToken() {
        this.token = Utils.bytesToHex(Crypto.createSessionKey(32));
        return this.token;
    }

}

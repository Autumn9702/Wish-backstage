package cn.autumn.wishbackstage.database.entity.user;


import cn.autumn.wishbackstage.database.entity.Entity;
import cn.autumn.wishbackstage.util.Crypto;
import cn.autumn.wishbackstage.util.Utils;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;


/**
 * @author cf
 * Created in 2022/10/31
 */
@TableName(value = "account")
public class Account extends Entity {

    @Serial
    private static final long serialVersionUID = 7244082835835666821L;

    @Getter @Setter private String username;
    @Getter @Setter private String password;

    @Setter private String email;
    @Setter @Getter private String phone;

    @Getter @Setter private String token;
    /* backstage web */
    @Getter @Setter private String type;

    @Setter private boolean isBanned;
    @Getter @Setter private int banStartTime;
    @Getter @Setter private int banEndTime;
    @Getter @Setter private String banReason;

    @Getter private List<String> permissions;

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

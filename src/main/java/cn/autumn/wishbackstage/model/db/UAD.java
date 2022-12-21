package cn.autumn.wishbackstage.model.db;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cf
 * Created in 2022/11/16
 */
@SuppressWarnings("all")
public final class UAD {

    @Getter @Setter private List<UpTyCl> update;
    @Getter @Setter private List<UpTyCl> create;
    @Getter @Setter private List<UpTyCl> delete;

    public UAD() {}

    public UAD(List<UpTyCl> update) {
        this.update = update;
    }

    public UAD(List<UpTyCl> update, List<UpTyCl> create) {
        this.update = update;
        this.create = create;
    }

    public UAD(List<UpTyCl> update, List<UpTyCl> create, List<UpTyCl> delete) {
        this.update = update;
        this.create = create;
        this.delete = delete;
    }
}

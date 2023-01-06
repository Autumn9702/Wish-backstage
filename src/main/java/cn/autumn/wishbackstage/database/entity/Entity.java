package cn.autumn.wishbackstage.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description
 */
@Data
@MappedSuperclass
public class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = -183215792823642634L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 32, nullable = false)
    private int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || this.getClass() != o.getClass()) {return false;}
        Entity that = (Entity) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

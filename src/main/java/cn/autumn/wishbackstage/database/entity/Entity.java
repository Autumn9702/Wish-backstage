package cn.autumn.wishbackstage.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Autumn
 * Created in 2023/1/5
 * Description
 */
@Data
@MappedSuperclass
public class Entity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 32, nullable = false)
    private int id;
}

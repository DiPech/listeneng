package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import ru.dipech.listeneng.util.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`user_role`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserRole extends BaseEntity {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    @Column(unique = true)
    private String name;

    // -----------------------------------------------------------------------------------------------------------------

    @JsonBackReference
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = Constants.HIBERNATE_BATCH_SIZE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "roles")
    private Set<User> accounts = new HashSet<>();

}

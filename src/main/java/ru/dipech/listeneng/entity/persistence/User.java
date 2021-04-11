package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import ru.dipech.listeneng.util.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`user`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity {

    public static final UUID ADMIN_UUID = UUID.fromString("0a000a00-0000-0a0a-aa00-a00a00aaaaa0");

    @Email
    @Column(unique = true)
    private String email;

    @Column
    @JsonIgnore
    private String password;

    // -----------------------------------------------------------------------------------------------------------------

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonManagedReference
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = Constants.HIBERNATE_BATCH_SIZE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "`user_user_role`",
        joinColumns = @JoinColumn(name = "`user_id`"),
        inverseJoinColumns = @JoinColumn(name = "`role_id`")
    )
    private Set<UserRole> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @OrderBy("priority")
    private List<Section> sections = new ArrayList<>();

}

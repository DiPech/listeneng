package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
@EqualsAndHashCode(of = {"id"})
public abstract class BaseEntity implements Persistable<UUID>, Serializable {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Transient
    private boolean isNew;

    @PostPersist
    @PostLoad
    @PostUpdate
    protected void init() {
        isNew = false;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null || isNew;
    }

    @JsonCreator
    public BaseEntity() {
    }

}

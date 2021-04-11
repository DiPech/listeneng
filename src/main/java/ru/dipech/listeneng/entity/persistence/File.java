package ru.dipech.listeneng.entity.persistence;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import ru.dipech.listeneng.entity.FileScope;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`file`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File extends BaseEntity {

    @Column(name = "`file_name`")
    @Length(max = 256)
    private String fileName;

    @Column(name = "file_scope")
    @Enumerated(EnumType.STRING)
    private FileScope fileScope;

    @Column(name = "`expires_at`")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

}

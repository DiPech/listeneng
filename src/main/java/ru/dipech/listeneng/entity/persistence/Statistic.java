package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`statistic`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Statistic extends BaseEntity implements Comparable<Statistic> {

    @Column(name = "`created_at`")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "`value`")
    @Enumerated(EnumType.STRING)
    private StatisticValue value;

    // -----------------------------------------------------------------------------------------------------------------

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`entry_id`")
    private Entry entry;

    @Override
    public int compareTo(@NotNull Statistic e) {
        return Comparator.comparing(Statistic::getCreatedAt).compare(this, e);
    }

}

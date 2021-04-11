package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`entry`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Entry extends BaseEntity implements Comparable<Entry> {

    @Column(name = "`priority`")
    private Integer priority;

    @Column(name = "`type`")
    @Enumerated(EnumType.STRING)
    private EntryType type;

    @Column(name = "`text`")
    @Length(min = 2, max = 10000)
    private String text;

    @Column(name = "`phrase`")
    @Length(min = 2, max = 512)
    private String phrase;

    @Column(name = "`translation`")
    @Length(min = 2, max = 512)
    private String translation;

    @Column(name = "`divider`")
    @Length(min = 2, max = 256)
    private String divider;

    // -----------------------------------------------------------------------------------------------------------------

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`section_id`")
    private Section section;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "entry", orphanRemoval = true)
    @OrderBy("createdAt")
    private List<Statistic> statistics = new ArrayList<>();

    @Override
    public int compareTo(@NotNull Entry e) {
        return Comparator.comparingInt(Entry::getPriority).compare(this, e);
    }

}

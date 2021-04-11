package ru.dipech.listeneng.entity.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "`section`")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Section extends BaseEntity implements TreeEntity<Section, UUID>, Comparable<Section> {

    @Column(name = "`priority`")
    private Integer priority;

    @Column(name = "`global_priority`")
    private Integer globalPriority;

    @Column(name = "`name`")
    @Length(min = 2, max = 256)
    private String name;

    // -----------------------------------------------------------------------------------------------------------------

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`user_id`")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`parent_id`")
    private Section parent;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "`parent_id`")
    @OrderBy("priority")
    private List<Section> children = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "section", orphanRemoval = true)
    @OrderBy("priority")
    private List<Entry> entries = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public int compareTo(@NotNull Section s) {
        return Comparator.comparingInt(Section::getPriority).compare(this, s);
    }

}

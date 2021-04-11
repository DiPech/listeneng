package ru.dipech.listeneng.entity.persistence;

import java.util.List;

public interface TreeEntity<E, ID> {

    ID getId();

    List<E> getChildren();

}

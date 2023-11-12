package dao;

import java.util.List;

public interface Dao<T, K> {
    T getById(K id);
    List<T> getAll();
    T create(T t);
    T update(T t);
    void delete(T t);
}

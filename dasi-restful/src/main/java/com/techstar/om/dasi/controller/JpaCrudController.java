package com.techstar.om.dasi.controller;

import com.techstar.framework.jpa.common.JpaNamed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public abstract class JpaCrudController<T extends JpaNamed<ID>, ID, R extends JpaRepository<T, ID>> implements CrudController<T, ID> {
    @Autowired
    protected R repos;

    protected abstract T existed(T data);

    protected void assertExisted(T data, String message) {
        T existed = existed(data);
        if (existed != null) { // 存在相同名称的记录
            if (data.getId() == null || !data.getId().equals(existed.getId())) {
                throw new RuntimeException(message);
            }
        }
    }

    protected void assertExisted(T data) {
        assertExisted(data, String.format("已经存在名称为“%s”的记录，不能建立名称重复的记录！"
                , data.getName()));
    }

    @Override
    public List<T> list() {
        return repos.findAll();
    }

    @Override
    public T save(T data) {
        assertExisted(data);
        return repos.save(data);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(@PathVariable ID id) {
        repos.deleteById(id);
    }

}

package com.soho.mybatis.crud.dao;

import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.condition.Cnd;

import java.util.List;

/**
 * MyBatis-CRUD接口
 *
 * @param <E>  数据模型
 * @param <ID> 模型主键
 * @author shadow
 */
public interface MyBatisDAO<E extends IDEntity<ID>, ID> {

    /**
     * 保存实体信息
     *
     * @param entity
     * @return int
     * @throws MybatisDAOEx
     */
    public int insert(E entity) throws MybatisDAOEx;

    /**
     * 保存实体集合信息
     *
     * @param entities
     * @return int
     * @throws MybatisDAOEx
     */
    public int insert(List<E> entities) throws MybatisDAOEx;

    /**
     * 更新实体信息
     *
     * @param entity
     * @return int
     * @throws MybatisDAOEx
     */
    public int update(E entity) throws MybatisDAOEx;

    /**
     * 更新实体信息
     *
     * @param cnd
     * @return int
     * @throws MybatisDAOEx
     */
    public int update(Cnd cnd) throws MybatisDAOEx;

    /**
     * 通过对象删除实体信息
     *
     * @param entities
     * @return int
     * @throws MybatisDAOEx
     */
    public int delete(E... entities) throws MybatisDAOEx;

    /**
     * 通过对象删除实体信息
     *
     * @param entity
     * @return int
     * @throws MybatisDAOEx
     */
    public int delete(E entity) throws MybatisDAOEx;

    /**
     * 通过PK删除实体信息
     *
     * @param ids
     * @return int
     * @throws MybatisDAOEx
     */
    public int delete(ID... ids) throws MybatisDAOEx;

    /**
     * 通过PK删除实体信息
     *
     * @param id
     * @return int
     * @throws MybatisDAOEx
     */
    public int delete(ID id) throws MybatisDAOEx;

    /**
     * 通过Cnd删除实体信息
     *
     * @param cnd
     * @return int
     * @throws MybatisDAOEx
     */
    public int delete(Cnd cnd) throws MybatisDAOEx;

    /**
     * 通过PK查询实体信息
     *
     * @param id
     * @return E
     * @throws MybatisDAOEx
     */
    public E findById(ID id) throws MybatisDAOEx;

    /**
     * 通过Cnd条件对象查询实体数目
     *
     * @param cnd
     * @return int
     * @throws MybatisDAOEx
     */
    public int countByCnd(Cnd cnd) throws MybatisDAOEx;

    /**
     * 查询实体数目
     *
     * @return int
     * @throws MybatisDAOEx
     */
    public int countAll() throws MybatisDAOEx;

    /**
     * 通过Cnd条件对象查询实体集合
     *
     * @param cnd
     * @return List<E>
     * @throws MybatisDAOEx
     */
    public List<E> findByCnd(Cnd cnd) throws MybatisDAOEx;

    /**
     * 查询实体集合
     *
     * @return List<E>
     * @throws MybatisDAOEx
     */
    public List<E> findAll() throws MybatisDAOEx;

    /**
     * 通过Cnd条件对象查询实体
     *
     * @param cnd
     * @return E
     * @throws MybatisDAOEx
     */
    public E findOneByCnd(Cnd cnd) throws MybatisDAOEx;

    /**
     * 通过Cnd条件对象分页查询实体
     *
     * @param cnd
     * @return PagingResult<E>
     * @throws MybatisDAOEx
     */
    public Pagination<E> pagingByCnd(Cnd cnd) throws MybatisDAOEx;

    /**
     * 获取单独的字段集合
     *
     * @param cnd
     * @return List<T>
     * @throws MybatisDAOEx
     */
    public <T> List<T> findOnlyByCnd(Cnd cnd, Class<T> clazz) throws MybatisDAOEx;

    /**
     * 获取单独的字段单条集合
     *
     * @param cnd
     * @return List<T>
     * @throws MybatisDAOEx
     */
    public <T> T findOnlyOneByCnd(Cnd cnd, Class<T> clazz) throws MybatisDAOEx;

}

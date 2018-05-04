package com.soho.mybatis.crud.service;

import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.condition.Cnd;

import java.util.List;
import java.util.Map;

/**
 * MyBatis-CRUD接口
 *
 * @param <E>  数据模型
 * @param <ID> 模型主键
 * @author shadow
 */
public interface BaseService<E extends IDEntity<ID>, ID> {

    public MyBatisDAO<E, ID> getMybatisDAO();

    /**
     * 保存实体信息
     *
     * @param entity
     * @return int
     * @throws BizErrorEx
     */
    public int insert(E entity) throws BizErrorEx;

    /**
     * 保存实体集合信息
     *
     * @param entities
     * @return int
     * @throws BizErrorEx
     */
    public int insert(List<E> entities) throws BizErrorEx;

    /**
     * 更新实体信息
     *
     * @param entity
     * @return int
     * @throws BizErrorEx
     */
    public int update(E entity) throws BizErrorEx;

    /**
     * 更新实体信息
     *
     * @param cnd
     * @return int
     * @throws BizErrorEx
     */
    public int update(Cnd cnd) throws BizErrorEx;

    /**
     * 通过对象删除实体信息
     *
     * @param entities
     * @return int
     * @throws BizErrorEx
     */
    public int delete(E... entities) throws BizErrorEx;

    /**
     * 通过对象删除实体信息
     *
     * @param entity
     * @return int
     * @throws BizErrorEx
     */
    public int delete(E entity) throws BizErrorEx;

    /**
     * 通过PK删除实体信息
     *
     * @param ids
     * @return int
     * @throws BizErrorEx
     */
    public int delete(ID... ids) throws BizErrorEx;

    /**
     * 通过PK删除实体信息
     *
     * @param id
     * @return int
     * @throws BizErrorEx
     */
    public int delete(ID id) throws BizErrorEx;

    /**
     * 通过Cnd删除实体信息
     *
     * @param cnd
     * @return int
     * @throws BizErrorEx
     */
    public int delete(Cnd cnd) throws BizErrorEx;

    /**
     * 通过PK查询实体信息
     *
     * @param id
     * @return E
     * @throws BizErrorEx
     */
    public E findById(ID id) throws BizErrorEx;

    /**
     * 通过Cnd条件对象查询实体数目
     *
     * @param cnd
     * @return int
     * @throws BizErrorEx
     */
    public int countByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 查询实体数目
     *
     * @return int
     * @throws BizErrorEx
     */
    public int countAll() throws BizErrorEx;

    /**
     * 通过Cnd条件对象查询实体集合
     *
     * @param cnd
     * @return List<E>
     * @throws BizErrorEx
     */
    public List<E> findByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 查询实体集合
     *
     * @return List<E>
     * @throws BizErrorEx
     */
    public List<E> findAll() throws BizErrorEx;

    /**
     * 通过Cnd条件对象查询实体
     *
     * @param cnd
     * @return E
     * @throws BizErrorEx
     */
    public E findOneByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 通过Cnd条件对象分页查询实体
     *
     * @param cnd
     * @return PagingResult<E>
     * @throws BizErrorEx
     */
    public Pagination<E> pagingByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 获取多个MAP数据
     *
     * @param cnd
     * @return List<Map<String, Object>>
     * @throws BizErrorEx
     */
    public List<Map<String, Object>> findMapByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 获取多个MAP数据
     *
     * @param cnd
     * @return List<T>
     * @throws BizErrorEx
     */
    public <T> List<T> findMapByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx;

    /**
     * 获取单个MAP数据
     *
     * @param cnd
     * @return Map<String, Object>
     * @throws BizErrorEx
     */
    public Map<String, Object> findMapOneByCnd(Cnd cnd) throws BizErrorEx;

    /**
     * 获取单个MAP数据
     *
     * @param cnd
     * @return T
     * @throws BizErrorEx
     */
    public <T> T findMapOneByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx;

    /**
     * 获取单独的字段单条集合
     *
     * @param cnd
     * @param clazz
     * @return T
     * @throws BizErrorEx
     */
    public <T> T findFieldOneByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx;

    /**
     * 获取单独的字段集合
     *
     * @param cnd
     * @param clazz
     * @return List<T>
     * @throws BizErrorEx
     */
    public <T> List<T> findFieldByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx;

}

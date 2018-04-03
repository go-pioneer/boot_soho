package com.soho.mybatis.crud.dao.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soho.mybatis.crud.aconst.OPT;
import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.pageable.imp.SimplePagination;
import com.soho.mybatis.sqlcode.condition.Cnd;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MyBatisDAO CRUD封装实现类
 *
 * @param <E>
 * @author shadow
 */
public class MyBatisDAOImp<E extends IDEntity<Long>> extends SqlSessionDaoSupport implements MyBatisDAO<E, Long> {

    private final static Logger log = LoggerFactory.getLogger(MyBatisDAOImp.class);

    @Autowired(required = false)
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public int insert(E entity) throws MybatisDAOEx {
        validateNullObject(entity);
        try {
            return getSqlSession().insert(sqlId(OPT.INSERT), entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("保存数据失败", e, "10002");
        }
    }

    public int insert(List<E> entities) throws MybatisDAOEx {
        validateNullObject(entities);
        if (entities.isEmpty()) {
            throw new MybatisDAOEx("实体参数集合不能为空", "10003");
        }
        try {
            for (E entity : entities) {
                this.insert(entity);
            }
            return entities.size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("保存数据失败", e, "10002");
        }
    }

    public int update(E entity) throws MybatisDAOEx {
        validateNullObject(entity);
        try {
            return getSqlSession().update(sqlId(OPT.UPDATE), entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("更新数据失败", e, "10004");
        }
    }

    public int update(Cnd cnd) throws MybatisDAOEx {
        validateNullObject(cnd);
        try {
            return getSqlSession().update(sqlId(OPT.UPDATEBYCND), cnd);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("更新数据失败", e, "10004");
        }
    }

    public int delete(E... entities) throws MybatisDAOEx {
        validateNullObject(entities);
        if (entities.length <= 0) {
            throw new MybatisDAOEx("实体参数集合不能为空", "10003");
        }
        try {
            List<Object> ids = new ArrayList<Object>();
            for (E entity : entities) {
                validateNullObject(entities);
                if (entity.getId() == null) {
                    throw new MybatisDAOEx("实体参数主键不能为空", "10004");
                } else {
                    ids.add(entity.getId());
                }
            }
            return delete(new SQLCnd().in("id", ids.toArray()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("删除数据失败", e, "10005");
        }
    }

    public int delete(Cnd cnd) throws MybatisDAOEx {
        try {
            validateNullObject(cnd);
            return getSqlSession().delete(sqlId(OPT.DELETE), cnd);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("删除数据失败", e, "10005");
        }
    }

    public int delete(E entity) throws MybatisDAOEx {
        try {
            validateNullObject(entity);
            if (entity.getId() == null) {
                throw new MybatisDAOEx("实体参数主键不能为空", "10004");
            }
            return delete(new SQLCnd().eq("id", entity.getId()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("删除数据失败", e, "10005");
        }
    }

    public int delete(Long... ids) throws MybatisDAOEx {
        validateNullObject(ids);
        if (ids.length <= 0) {
            throw new MybatisDAOEx("实体参数集合不能为空", "10003");
        }
        try {
            List<Object> objects = new ArrayList<Object>();
            for (Long id : ids) {
                validateNullObject(id);
                objects.add(id);
            }
            return delete(new SQLCnd().in("id", objects.toArray()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("删除数据失败", e, "10005");
        }
    }

    public int delete(Long id) throws MybatisDAOEx {
        try {
            validateNullObject(id);
            return delete(new SQLCnd().eq("id", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("删除数据失败", e, "10005");
        }
    }

    public E findById(Long id) throws MybatisDAOEx {
        validateNullObject(id);
        try {
            return findOneByCnd(new SQLCnd().eq("id", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据失败", e, "10006");
        }
    }

    public int countByCnd(Cnd cnd) throws MybatisDAOEx {
        validateNullObject(cnd);
        try {
            Integer result = getSqlSession().selectOne(sqlId(OPT.COUNT), cnd);
            if (result == null)
                result = 0;
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    public List<E> findByCnd(Cnd cnd) throws MybatisDAOEx {
        validateNullObject(cnd);
        try {
            List<E> result = getSqlSession().selectList(sqlId(OPT.FIND), cnd);
            if (result == null) {
                result = new ArrayList<E>();
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    public int countAll() throws MybatisDAOEx {
        try {
            return countByCnd(new SQLCnd());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    public List<E> findAll() throws MybatisDAOEx {
        try {
            return findByCnd(new SQLCnd());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    public E findOneByCnd(Cnd cnd) throws MybatisDAOEx {
        List<E> list = findByCnd(cnd);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public Pagination<E> pagingByCnd(Cnd cnd) throws MybatisDAOEx {
        validateNullObject(cnd);
        try {
            Pagination<Object> pagination = cnd.getPagination();
            Integer pageNo = null;
            Integer pageSize = null;
            if (pagination == null) {
                throw new MybatisDAOEx("分页参数对象不能为空", "10008");
            } else {
                pageNo = pagination.getPageNo() == null ? 1 : pagination.getPageNo();
                pageSize = pagination.getPageSize() == null ? 10 : pagination.getPageSize();
            }
            List<E> data = getSqlSession().selectList(sqlId(OPT.FIND), cnd, new RowBounds(pageNo, pageSize));
            if (data == null) {
                data = new ArrayList<E>();
            }
            return new SimplePagination<E>(cnd.getPagination().getPageNo(), cnd.getPagination().getPageSize(), cnd.getPagination().getPageTotal(), data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    @Override
    public <T> List<T> findOnlyByCnd(Cnd cnd, Class<T> clazz) throws MybatisDAOEx {
        List<E> list = findByCnd(cnd);
        if (!list.isEmpty()) {
            String field = cnd.getOnlyField();
            JSONArray array = JSON.parseArray(JSON.toJSONString(list));
            List<T> newList = new ArrayList<>(array.size());
            for (Object object : array) {
                JSONObject json = (JSONObject) object;
                newList.add(json.getObject(field, clazz));
            }
            return newList;
        }
        return new ArrayList<>();
    }

    @Override
    public <T> T findOnlyOneByCnd(Cnd cnd, Class<T> clazz) throws MybatisDAOEx {
        List<T> list = findOnlyByCnd(cnd, clazz);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> findMapByCnd(Cnd cnd) throws MybatisDAOEx {
        validateNullObject(cnd);
        try {
            List<Map<String, Object>> result = getSqlSession().selectList(sqlId(OPT.FINDMAP), cnd);
            if (result == null) {
                result = new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MybatisDAOEx("查询数据记录失败", e, "10007");
        }
    }

    @Override
    public Map<String, Object> findOneMapByCnd(Cnd cnd) throws MybatisDAOEx {
        List<Map<String, Object>> mapList = findMapByCnd(cnd);
        if (!mapList.isEmpty()) {
            return mapList.get(0);
        }
        return null;
    }

    /**
     * 校验空参数
     */
    private void validateNullObject(Object obj) throws MybatisDAOEx {
        if (obj == null) {
            throw new MybatisDAOEx("实体参数对象不能为空", "10001");
        }
    }

    /**
     * 获取DAO模型的访问路径
     *
     * @throws MybatisDAOEx
     */
    protected String sqlId(OPT opt) throws MybatisDAOEx {
        return sqlId(opt.toString());
    }

    protected String sqlId(String opt) throws MybatisDAOEx {
        Class<?>[] classes = getClass().getInterfaces();
        if (classes == null || classes.length == 0) {
            throw new MybatisDAOEx("没有找到上级接口");
        }
        StringBuffer buffer = new StringBuffer();
        String sqlId = buffer.append(classes[0].getName()).append(".").append(opt).toString();
        buffer.setLength(0);
        return sqlId;
    }

}

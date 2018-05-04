package com.soho.mybatis.crud.service.imp;

import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.mybatis.crud.domain.RetMap;
import com.soho.mybatis.crud.service.BaseService;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.condition.Cnd;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImp<E extends IDEntity<ID>, ID> implements BaseService<E, ID> {

    protected RetMap buildRetMap() {
        return new RetMap();
    }

    public abstract MyBatisDAO<E, ID> getMybatisDAO();

    public int insert(E entity) throws BizErrorEx {
        try {
            return getMybatisDAO().insert(entity);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }


    public int insert(List<E> entities) throws BizErrorEx {
        try {
            return getMybatisDAO().insert(entities);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }


    public int update(E entity) throws BizErrorEx {
        try {
            return getMybatisDAO().update(entity);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int update(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().update(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int delete(E... entities) throws BizErrorEx {
        try {
            return getMybatisDAO().delete(entities);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int delete(E entity) throws BizErrorEx {
        try {
            return getMybatisDAO().delete(entity);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int delete(ID... ids) throws BizErrorEx {
        try {
            return getMybatisDAO().delete(ids);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int delete(ID id) throws BizErrorEx {
        try {
            return getMybatisDAO().delete(id);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int delete(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().delete(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public E findById(ID id) throws BizErrorEx {
        try {
            return getMybatisDAO().findById(id);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int countByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().countByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public int countAll() throws BizErrorEx {
        try {
            return getMybatisDAO().countAll();
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public List<E> findByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().findByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public List<E> findAll() throws BizErrorEx {
        try {
            return getMybatisDAO().findAll();
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public E findOneByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().findOneByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public Pagination<E> pagingByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().pagingByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public List<Map<String, Object>> findMapByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().findMapByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public <T> List<T> findMapByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx {
        try {
            return getMybatisDAO().findMapByCnd(cnd, clazz);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public Map<String, Object> findMapOneByCnd(Cnd cnd) throws BizErrorEx {
        try {
            return getMybatisDAO().findMapOneByCnd(cnd);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public <T> T findMapOneByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx {
        try {
            return getMybatisDAO().findMapOneByCnd(cnd, clazz);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public <T> List<T> findFieldByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx {
        try {
            return getMybatisDAO().findFieldByCnd(cnd, clazz);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    public <T> T findFieldOneByCnd(Cnd cnd, Class<T> clazz) throws BizErrorEx {
        try {
            return getMybatisDAO().findFieldOneByCnd(cnd, clazz);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

}

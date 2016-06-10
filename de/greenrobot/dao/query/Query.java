package de.greenrobot.dao.query;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import java.util.List;

public class Query<T> extends AbstractQuery<T> {
    private final int limitPosition;
    private final int offsetPosition;
    private final ThreadLocalQuery<T> threadLocalQuery;

    private static final class ThreadLocalQuery<T2> extends ThreadLocal<Query<T2>> {
        private final AbstractDao<T2, ?> dao;
        private final String[] initialValues;
        private final int limitPosition;
        private final int offsetPosition;
        private final String sql;

        private ThreadLocalQuery(AbstractDao<T2, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
            this.dao = dao;
            this.sql = sql;
            this.initialValues = initialValues;
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }

        protected Query<T2> initialValue() {
            return new Query(this.dao, this.sql, (String[]) this.initialValues.clone(), this.limitPosition, this.offsetPosition, null);
        }
    }

    public static <T2> Query<T2> internalCreate(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return create(dao, sql, initialValues, -1, -1);
    }

    static <T2> Query<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues, int limitPosition, int offsetPosition) {
        return (Query) new ThreadLocalQuery(sql, AbstractQuery.toStringArray(initialValues), limitPosition, offsetPosition, null).get();
    }

    private Query(ThreadLocalQuery<T> threadLocalQuery, AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues);
        this.threadLocalQuery = threadLocalQuery;
        this.limitPosition = limitPosition;
        this.offsetPosition = offsetPosition;
    }

    public Query<T> forCurrentThread() {
        Query<T> query = (Query) this.threadLocalQuery.get();
        String[] initialValues = this.threadLocalQuery.initialValues;
        System.arraycopy(initialValues, 0, query.parameters, 0, initialValues.length);
        return query;
    }

    public void setParameter(int index, Object parameter) {
        if (index < 0 || !(index == this.limitPosition || index == this.offsetPosition)) {
            super.setParameter(index, parameter);
            return;
        }
        throw new IllegalArgumentException("Illegal parameter index: " + index);
    }

    public void setLimit(int limit) {
        checkThread();
        if (this.limitPosition == -1) {
            throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.limitPosition] = Integer.toString(limit);
    }

    public void setOffset(int offset) {
        checkThread();
        if (this.offsetPosition == -1) {
            throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.offsetPosition] = Integer.toString(offset);
    }

    public List<T> list() {
        checkThread();
        return this.daoAccess.loadAllAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }

    public LazyList<T> listLazy() {
        checkThread();
        return new LazyList(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), true);
    }

    public LazyList<T> listLazyUncached() {
        checkThread();
        return new LazyList(this.daoAccess, this.dao.getDatabase().rawQuery(this.sql, this.parameters), false);
    }

    public CloseableListIterator<T> listIterator() {
        return listLazyUncached().listIteratorAutoClose();
    }

    public T unique() {
        checkThread();
        return this.daoAccess.loadUniqueAndCloseCursor(this.dao.getDatabase().rawQuery(this.sql, this.parameters));
    }

    public T uniqueOrThrow() {
        T entity = unique();
        if (entity != null) {
            return entity;
        }
        throw new DaoException("No entity found for query");
    }
}

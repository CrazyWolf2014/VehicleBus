package de.greenrobot.dao.query;

import android.database.Cursor;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

public class CountQuery<T> extends AbstractQuery<T> {
    private final ThreadLocalQuery<T> threadLocalQuery;

    private static final class ThreadLocalQuery<T2> extends ThreadLocal<CountQuery<T2>> {
        private final AbstractDao<T2, ?> dao;
        private final String[] initialValues;
        private final String sql;

        private ThreadLocalQuery(AbstractDao<T2, ?> dao, String sql, String[] initialValues) {
            this.dao = dao;
            this.sql = sql;
            this.initialValues = initialValues;
        }

        protected CountQuery<T2> initialValue() {
            return new CountQuery(this.dao, this.sql, (String[]) this.initialValues.clone(), null);
        }
    }

    public /* bridge */ /* synthetic */ void setParameter(int x0, Object x1) {
        super.setParameter(x0, x1);
    }

    static <T2> CountQuery<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return (CountQuery) new ThreadLocalQuery(sql, AbstractQuery.toStringArray(initialValues), null).get();
    }

    private CountQuery(ThreadLocalQuery<T> threadLocalQuery, AbstractDao<T, ?> dao, String sql, String[] initialValues) {
        super(dao, sql, initialValues);
        this.threadLocalQuery = threadLocalQuery;
    }

    public CountQuery<T> forCurrentThread() {
        CountQuery<T> query = (CountQuery) this.threadLocalQuery.get();
        String[] initialValues = this.threadLocalQuery.initialValues;
        System.arraycopy(initialValues, 0, query.parameters, 0, initialValues.length);
        return query;
    }

    public long count() {
        checkThread();
        Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        try {
            if (!cursor.moveToNext()) {
                throw new DaoException("No result for count");
            } else if (!cursor.isLast()) {
                throw new DaoException("Unexpected row count: " + cursor.getCount());
            } else if (cursor.getColumnCount() != 1) {
                throw new DaoException("Unexpected column count: " + cursor.getColumnCount());
            } else {
                long j = cursor.getLong(0);
                return j;
            }
        } finally {
            cursor.close();
        }
    }
}

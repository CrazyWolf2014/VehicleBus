package de.greenrobot.dao.query;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;

public class DeleteQuery<T> extends AbstractQuery<T> {
    private SQLiteStatement compiledStatement;
    private final ThreadLocalQuery<T> threadLocalQuery;

    private static final class ThreadLocalQuery<T2> extends ThreadLocal<DeleteQuery<T2>> {
        private final AbstractDao<T2, ?> dao;
        private final String[] initialValues;
        private final String sql;

        private ThreadLocalQuery(AbstractDao<T2, ?> dao, String sql, String[] initialValues) {
            this.dao = dao;
            this.sql = sql;
            this.initialValues = initialValues;
        }

        protected DeleteQuery<T2> initialValue() {
            return new DeleteQuery(this.dao, this.sql, (String[]) this.initialValues.clone(), null);
        }
    }

    public /* bridge */ /* synthetic */ void setParameter(int x0, Object x1) {
        super.setParameter(x0, x1);
    }

    static <T2> DeleteQuery<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return (DeleteQuery) new ThreadLocalQuery(sql, AbstractQuery.toStringArray(initialValues), null).get();
    }

    private DeleteQuery(ThreadLocalQuery<T> threadLocalQuery, AbstractDao<T, ?> dao, String sql, String[] initialValues) {
        super(dao, sql, initialValues);
        this.threadLocalQuery = threadLocalQuery;
    }

    public DeleteQuery<T> forCurrentThread() {
        DeleteQuery<T> query = (DeleteQuery) this.threadLocalQuery.get();
        String[] initialValues = this.threadLocalQuery.initialValues;
        System.arraycopy(initialValues, 0, query.parameters, 0, initialValues.length);
        return query;
    }

    public void executeDeleteWithoutDetachingEntities() {
        checkThread();
        SQLiteDatabase db = this.dao.getDatabase();
        if (db.isDbLockedByCurrentThread()) {
            executeDeleteWithoutDetachingEntitiesInsideTx();
            return;
        }
        db.beginTransaction();
        try {
            executeDeleteWithoutDetachingEntitiesInsideTx();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private synchronized void executeDeleteWithoutDetachingEntitiesInsideTx() {
        if (this.compiledStatement != null) {
            this.compiledStatement.clearBindings();
        } else {
            this.compiledStatement = this.dao.getDatabase().compileStatement(this.sql);
        }
        for (int i = 0; i < this.parameters.length; i++) {
            String value = this.parameters[i];
            if (value != null) {
                this.compiledStatement.bindString(i + 1, value);
            } else {
                this.compiledStatement.bindNull(i + 1);
            }
        }
        this.compiledStatement.execute();
    }
}

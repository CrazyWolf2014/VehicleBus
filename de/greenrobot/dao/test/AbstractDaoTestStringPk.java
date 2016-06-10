package de.greenrobot.dao.test;

import de.greenrobot.dao.AbstractDao;

public abstract class AbstractDaoTestStringPk<D extends AbstractDao<T, String>, T> extends AbstractDaoTestSinglePk<D, T, String> {
    public AbstractDaoTestStringPk(Class<D> daoClass) {
        super(daoClass);
    }

    protected String createRandomPk() {
        int len = this.random.nextInt(30) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append((char) (this.random.nextInt(25) + 97));
        }
        return builder.toString();
    }
}

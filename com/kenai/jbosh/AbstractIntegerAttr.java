package com.kenai.jbosh;

abstract class AbstractIntegerAttr extends AbstractAttr<Integer> {
    protected AbstractIntegerAttr(int i) throws BOSHException {
        super(Integer.valueOf(i));
    }

    protected AbstractIntegerAttr(String str) throws BOSHException {
        super(Integer.valueOf(parseInt(str)));
    }

    protected final void checkMinValue(int i) throws BOSHException {
        int intValue = ((Integer) getValue()).intValue();
        if (intValue < i) {
            throw new BOSHException("Illegal attribute value '" + intValue + "' provided.  " + "Must be >= " + i);
        }
    }

    private static int parseInt(String str) throws BOSHException {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            throw new BOSHException("Could not parse an integer from the value provided: " + str, e);
        }
    }

    public int intValue() {
        return ((Integer) getValue()).intValue();
    }
}

package net.kst_d.labs.cassandra;

public class One<T> {
    public final T _1;

    public static <T1> One<T1> of(T1 t1) {
	return new One<>(t1);
    }

    public One(T _1) {
	this._1 = _1;
    }

    public T get_1() {
	return _1;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}

	One one = (One) o;

	if (_1 != null ? !_1.equals(one._1) : one._1 != null) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	return _1 != null ? _1.hashCode() : 0;
    }

    @Override
    public String toString() {
	return "One{" +
			"_1=" + _1 +
			'}';
    }
}

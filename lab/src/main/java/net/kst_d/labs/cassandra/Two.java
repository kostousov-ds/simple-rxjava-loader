package net.kst_d.labs.cassandra;

public class Two<T1, T2> extends One<T1> {
    public final T2 _2;

    public static <T1, T2> Two<T1, T2> of(T1 t1, T2 t2){
	return  new Two<>(t1, t2);
    }

    public Two(T1 _1, T2 _2) {
	super(_1);
	this._2 = _2;
    }

    public T2 get_2() {
	return _2;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}
	if (!super.equals(o)) {
	    return false;
	}

	Two two = (Two) o;

	if (_2 != null ? !_2.equals(two._2) : two._2 != null) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	result = 31 * result + (_2 != null ? _2.hashCode() : 0);
	return result;
    }

    @Override
    public String toString() {
	return "Two{" +
	    "_1=" + _1 +
	    ", _2=" + _2 +
	    "}";
    }
}

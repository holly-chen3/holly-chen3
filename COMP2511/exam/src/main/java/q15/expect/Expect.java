package q15.expect;

public class Expect<E> {
    
    private E inner;

    protected Expect() {}

    public Expect(E obj) {
        this.inner = obj;
    }

    public Expect<E> toEqual(E other) {
        if (this.inner.equals(other)) {
            return new Expect<E>(other);
        }
        return new Expect<>(null);
    }

    public<T extends Comparable<E>> Expect<E> lessThan(T other) {
        if (other.compareTo(inner) > 0) {
            return new Expect<E>(inner);
        }
        return new Expect<>(null);
    }

    public<T extends Comparable<E>> Expect<E> greaterThanOrEqualTo(T other) {
        return null;
    }

    public Expect<E> not() {
        return null;
    }

    public Expect<E> skip() {
        return null;
    }

    public void evaluate() throws ExpectationFailedException{
        if (this.inner == null) {
            throw new ExpectationFailedException("false");
        }
    }

    protected E getInner() {
        return inner;
    }

}
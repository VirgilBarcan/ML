package ml.utils;

/**
 * This class models a Pair
 * Created by virgil on 29.10.2015.
 */
public class Pair<F, S> {

    private F first;
    private S second;

    /**
     * The Pair constructor
     * @param first the first element
     * @param second the second element
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first element of the Pair
     * @return the first element of the Pair
     */
    public F getFirst() {
        return first;
    }

    /**
     * Set the first element of the Pair
     * @param first the new first element
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * Get the second element of the Pair
     * @return the second element of the Pair
     */
    public S getSecond() {
        return second;
    }

    /**
     * Set the second element of the Pair
     * @param second the new second element
     */
    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
        return !(second != null ? !second.equals(pair.second) : pair.second != null);

    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}

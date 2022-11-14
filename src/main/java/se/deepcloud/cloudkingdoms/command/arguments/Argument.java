package se.deepcloud.cloudkingdoms.command.arguments;

public abstract class Argument<K, V> {

    protected final K k;
    protected final V v;

    protected Argument(K k, V v) {
        this.k = k;
        this.v = v;
    }

}
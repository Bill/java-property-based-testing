import java.util.Objects;
import java.util.Optional;

public class Map {

    public static final int SLOTS = 5; // few slots: aids finding hash collisions

    public static Map empty() {
        return new Map();
    }

    private Map() {}
    private Map(final Entry[] entries, final Entry newEntry) {
        System.arraycopy(entries, 0, this.entries,0,5);
        this.entries[hash(newEntry.key)] = newEntry;
    }

    private static class Entry {
        final String key;
        final String value;
        Entry(final String key, final String value) { this.key = key; this.value = value;}
    }

    final Entry[] entries = new Entry[SLOTS];

    public Map put(final String key, final String value) {
        return new Map(entries,new Entry(key,value));
    }

    public Optional<String> get(final String key) {
        final Entry entry = entries[hash(key)];
        return null != entry && entry.key == key ? Optional.of(entry.value) : Optional.empty();
    }

    public static int hash(final String key) {
        // first broken impl
        //return 0;
        // second broken impl
        //return key.hashCode();
        // third broken impl
        // return key.hashCode() % SLOTS;
        return (int)(Integer.toUnsignedLong(key.hashCode()) % SLOTS);
    }
}

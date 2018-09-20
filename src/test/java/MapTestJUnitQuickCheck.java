import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assume.*;

@RunWith(JUnitQuickcheck.class)
public class MapTestJUnitQuickCheck {

    @Test
    public void empty() {
        assertThat(Map.empty(), is(notNullValue()));
    }

    @Property
    public void putThenGet(final String key) {
        final Optional<String> vals = Map.empty().put(key, "hello").get(key);
        assertThat(vals.isPresent(), is(true));
        final String val = vals.get();
        assertThat(val,is(equalTo("hello")));
    }

    @Test
    public void putDifferentKeys() {
        Map map = Map.empty().put("a", "1").put("b", "2");
        assertContains(map, "a", "1");
        assertContains(map, "b", "2");
    }

    private void assertContains(final Map map, final String key, String value) {
        Optional<String> aVal = map.get(key);
        assertThat(aVal.isPresent(),is(true));

        assertThat(aVal.get(),is(equalTo(value)));
    }

    @Property
    public void collision(final String originalKey, final String newKey) {
        assumeThat(newKey, is(not(equalTo(originalKey))));
        final int originalKeyHash = Map.hash(originalKey);
        assumeThat(isCollision(originalKey, originalKeyHash, newKey), is(true));

        final Map firstMap = Map.empty().put(originalKey, "hello");
        final Map secondMap = firstMap.put(newKey, "goodbye");
        final Optional<String> originalVal = secondMap.get(originalKey);
        final Optional<String> collisionVal = secondMap.get(newKey);

        assertThat(originalVal.isPresent(), is(true));
        assertThat(originalVal.get(),is(equalTo("hello")));
        assertThat(collisionVal.isPresent(), is(true));
        assertThat(collisionVal.get(),is(equalTo("goodbye")));
    }

    private boolean isCollision(final String originalKey, int originalKeyHash, final String newKey) {
        final int newKeyHash = Map.hash(newKey);
        return originalKeyHash == newKeyHash && !originalKey.equals(newKey);
    }
}
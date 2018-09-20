import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@RunWith(JUnitQuickcheck.class)
public class MapTestJUnitQuickCheckAssertJ {

    @Test
    public void empty() {
        assertThat(Map.empty()).isNotNull();
    }

    @Property
    public void putThenGet(final String key) {
        final Optional<String> vals = Map.empty().put(key, "hello").get(key);
        assertThat(vals.isPresent()).isTrue();
        final String val = vals.get();
        assertThat(val).isEqualTo("hello");
    }

    @Test
    public void putDifferentKeys() {
        Map map = Map.empty().put("a", "1").put("b", "2");
        assertContains(map, "a", "1");
        assertContains(map, "b", "2");
    }

    private void assertContains(final Map map, final String key, String value) {
        Optional<String> aVal = map.get(key);
        assertThat(aVal.isPresent()).isTrue();

        assertThat(aVal.get()).isEqualTo(value);
    }

    @Property
    public void collision(final String originalKey, final String newKey) {
        assumeThat(newKey).isNotEqualTo(originalKey);
        final int originalKeyHash = Map.hash(originalKey);
        assumeThat(isCollision(originalKey, originalKeyHash, newKey)).isTrue();

        final Map firstMap = Map.empty().put(originalKey, "hello");
        final Map secondMap = firstMap.put(newKey, "goodbye");
        final Optional<String> originalVal = secondMap.get(originalKey);
        final Optional<String> collisionVal = secondMap.get(newKey);

        assertThat(originalVal.isPresent()).isTrue();
        assertThat(originalVal.get()).isEqualTo("hello");
        assertThat(collisionVal.isPresent());
        assertThat(collisionVal.get()).isEqualTo("goodbye");
    }

    private boolean isCollision(final String originalKey, int originalKeyHash, final String newKey) {
        final int newKeyHash = Map.hash(newKey);
        return originalKeyHash == newKeyHash && !originalKey.equals(newKey);
    }
}
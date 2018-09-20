import org.jetbrains.jetCheck.Generator;
import org.jetbrains.jetCheck.PropertyChecker;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jetbrains.jetCheck.Generator.asciiPrintableChars;

public class MapTestJetCheck {

    @Test
    public void empty() {
        assertThat(Map.empty()).isNotNull();
    }

    @Test
    public void putThenGet() {
        PropertyChecker.forAll(Generator.stringsOf(asciiPrintableChars()),
                key -> Map.empty().put(key, "hello")
                        .get(key).map(value -> Objects.equals(value, "hello"))
                        .orElse(false));
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

    @Test
    public void collision() {
        final Map firstMap = Map.empty().put("message", "hello");
        final int originalKeyHash = Map.hash("message");
        PropertyChecker.customized().withIterationCount(100).forAll(Generator.stringsOf(asciiPrintableChars())
                        .suchThat(newKey -> isCollision("message", originalKeyHash, newKey)),
                key -> {
                    final Map secondMap = firstMap.put(key, "goodbye");
                    final Optional<String> messageVal = secondMap.get("message");
                    final Optional<String> collisionVal = secondMap.get(key);
                    return messageVal.isPresent() && "hello".equals(messageVal.get()) && collisionVal.isPresent() && "goodbye".equals(collisionVal.get());
                });
    }

    private boolean isCollision(final String originalKey, int originalKeyHash, final String newKey) {
        final int newKeyHash = Map.hash(newKey);
        return originalKeyHash == newKeyHash && !originalKey.equals(newKey);
    }
}
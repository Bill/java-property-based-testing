This repo contains some simple experiments with a couple Java property-based testing frameworks.

We're using a very simple hash map class (see `Map`) as the test subject.

All the tests are built on JUnit 4.12. Some use AssertJ for assertions, others use built-in JUnit assertions and Hamcrest matchers.

There are three test classes:

* `MapTestJetCheck`: uses jetCheck and AssertJ assertions
* `MapTestJUnitQuickCheckAssertJ`: uses junit-quickcheck and AssertJ assertions
* `MapTestJUnitQuickCheck`: uses junit-quickcheck and JUnit/Hamcrest assertions

There are two property-based tests defined on each of the three test classes:

* `putThenGet()`: given a key, ensure that it's possible to put a value for it and get the value back
* `collision()`: given two colliding keys, ensure that it's possible to put values for each, and get the values back

The `Map` class does not handle key collisions. As a result, you can see the property-based `collision()` test fail on all three classes.

## Run It

```
./gradlew test
```

...then look at the HTML file containing the test output.

## Results

Here is what you see from JetCheck (`MapTestJetCheck`):

```
4:44:29 PM: failed on iteration 1 (iteration seed=4739085502432650115L, size hint=1, global seed=4739085502432650115L), shrinking...

org.jetbrains.jetCheck.PropertyFalsified: Falsified on {
Couldn't minimize, tried 3 examples

To reproduce the minimal failing case, run
  PropertyChecker.customized().rechecking("5/SknAjDjp3LGwEBew==")
    .forAll(...)
To re-run the test with all intermediate minimization steps, use `recheckingIteration(4739085502432650115L, 1)` instead for last iteration, or `withSeed(4739085502432650115L)` for all iterations
```

This is the output from junit-quickcheck with JUnit/Hamcrest assertions (`MapTestJUnitQuickCheck`):

```
java.lang.AssertionError: Property named 'collision' failed (
Expected: is <true>
     but: was <false>):
With arguments: [䱋媗욯鱪ꍽ黉夰ঁ쫞寎栰⯏ꕇ禦变鄘ࣵ隯玊鼗丿㙄, 帤➥뒉ߊ㈈]
Seeds for reproduction: [-5876988696104468400, 3174944953708823729] <Click to see difference>
<<stack trace omitted>>
```

Apparently, junit-quickcheck diagnostics do not play well with AssertJ assertions. Here is output of `MapTestJUnitQuickCheckAssertJ` when run from IntelliJ:

```
java.lang.AssertionError: Property named 'collision' failed (
Expected :true
Actual   :false
 <Click to see difference>
 <<stack trace omitted>>
```

Oddly, when run from the terminal, `MapTestJUnitQuickCheckAssertJ` seems to give better output: the arguments and seed information is printed in that case.

## Experiment With It

Have a look at the `Map.hash()` method. There are three alternative implementations of that method (see the comments). Each of them is broken in a different way, and each one causes tests to fail. Try each implementation and notice which tests fail.


# Release Notes - 2.1.0

## Breaking changes

* **Build now requires JDK 21.** `maven.compiler.source/target` was raised from 11 to 21 because the new Checkstyle 13.x test dependency requires JDK 21 at runtime. The published artifact still contains only the `checkstyle.xml` / `pmd-ruleset.xml` resources, so downstream consumers are unaffected at runtime — but anyone building this repo from source now needs JDK 21.

## Other changes

* Add `com.puppycrawl.tools:checkstyle` 13.4.2 as a `test`-scope dependency.
* Add `CheckstyleConfigLoadabilityTest` that loads `checkstyle.xml` via `ConfigurationLoader` and configures a `Checker` with it, so future Checkstyle bumps surface broken module references or removed properties in CI.
* CI workflow now runs on JDK 21.
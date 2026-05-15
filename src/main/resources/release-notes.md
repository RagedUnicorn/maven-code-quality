# Release Notes - 2.0.0

## Breaking changes

* **PMD upgraded from 6.21.0 to 7.24.0.** Downstream projects must use `maven-pmd-plugin` **3.22.0 or newer** — earlier versions still ship PMD 6 and cannot parse the new ruleset.
* **JSP, Velocity, XML, and XSL rules removed.** The ruleset is now Java-only. Projects relying on the previous non-Java rule coverage must configure those checks separately.
* **Many rule names changed in PMD 7.** Code using `@SuppressWarnings("PMD.<rule>")` against PMD 6 names will need updating. Notable renames: `UnusedImports`/`DuplicateImports`/`DontImportJavaLang`/`ImportFromSamePackage` → `UnnecessaryImport`; `Empty{If,While,Try,Synchronized,Switch,...}Stmt` → `EmptyControlStatement`; `{Boolean,Byte,Integer,Long,Short}Instantiation` / `UnnecessaryWrapperObjectCreation` → `UnnecessaryBoxing`; `UseAssert*InsteadOf*` / `SimplifyBooleanAssertion` → `SimplifiableTestAssertion`; `MissingBreakInSwitch` → `ImplicitSwitchFallThrough`; `ReturnEmptyArrayRatherThanNull` → `ReturnEmptyCollectionRatherThanNull`; `ExcessiveClassLength` / `ExcessiveMethodLength` → `NcssCount`; `PositionLiteralsFirst*` → `LiteralsFirstInComparisons`; `BadComparison` → `ComparisonWithNaN`; `DoNotCallSystemExit` → `DoNotTerminateVM`; `NonCaseLabelInSwitchStatement` → `NonCaseLabelInSwitch`; `TooFewBranchesForASwitchStatement` → `TooFewBranchesForSwitch`; `UnnecessaryLocalBeforeReturn` → `VariableCanBeInlined`; `DefaultLabelNotLastInSwitchStmt` → `DefaultLabelNotLastInSwitch`; `SwitchStmtsShouldHaveDefault` → `NonExhaustiveSwitch`; `GenericsNaming` → `TypeParameterNamingConventions`; `JUnit4TestShouldUseAfterAnnotation` → `UnitTestShouldUseAfterAnnotation`; `JUnit4TestShouldUseBeforeAnnotation` → `UnitTestShouldUseBeforeAnnotation`; `JUnit4TestShouldUseTestAnnotation` → `UnitTestShouldUseTestAnnotation`; `JUnitTestsShouldIncludeAssert` → `UnitTestShouldIncludeAssert`. See the [PMD 7 migration guide](https://docs.pmd-code.org/latest/pmd_release_notes_pmd7.html#removed-rules) for the complete mapping.

## Removed (no replacement in PMD 7)

* `performance/AvoidUsingShortType`
* `performance/SimplifyStartsWith`
* `errorprone/CloneThrowsCloneNotSupportedException`
* `errorprone/DoNotHardCodeSDCard`
* `errorprone/ProperCloneImplementation`
* `errorprone/EmptyStatementNotInLoop` (the rule itself is removed; `codestyle/UnnecessarySemicolon` covers stray semicolons)
* `errorprone/AvoidLosingExceptionInformation` (PMD 7 points to `UselessPureMethodCall` as the replacement, but with different semantics — re-add explicitly if you want that coverage)
* `errorprone/DontImportSun` (PMD 7 points to `UnsupportedJdkApiUsage` as the replacement, but with different semantics and required configuration — re-add explicitly if you want that coverage)
* `errorprone/AvoidCatchingNPE`, `errorprone/AvoidCatchingThrowable` (PMD 7 points to `AvoidCatchingGenericException` as the replacement, but it isn't available in PMD 7.17.0 — re-add the new rule once you're on a PMD version that ships it)
* `errorprone/UselessOperationOnImmutable` (scheduled for removal in PMD 8 with no replacement — dropped now to keep the ruleset forward-compatible)

## Other changes

* Add `RulesetLoadabilityTest` that loads the ruleset via PMD's `RuleSetLoader` in strict compatibility mode, so future PMD bumps surface broken rule references in CI.
* Set Maven compiler source/target to Java 11.

[![FinalTest](https://github.com/cscenter/csc-kotlin-final-test/actions/workflows/FinalTest.yml/badge.svg)](https://github.com/cscenter/csc-kotlin-final-test/actions/workflows/FinalTest.yml)

### Final test

In this test you need to implement one task:
- [Task#1](./task1/README.md)

See the provided links to get more details.

**If you have two red build, the total points will be halved**

The project has tests that must be fully passed before submitting the assignment. 
In addition, the project has style checks configured by Detekt and 
Diktat that must be passed (using the Suppress annotation is prohibited).

To run all tests locally you can use:`./gradlew test`;

To run task#1 tests locally you can use:`./gradlew :task1:test`;

To run Detekt locally you can use: `./gradlew detektCheckAll`;

To run Diktat locally you can use: `./gradlew diktatCheckAll`.

# maven-code-quality

> A maven dependency for providing code quality presets to projects

[![Maven Central](https://img.shields.io/maven-central/v/com.ragedunicorn.tools.maven/maven-code-quality?label=Maven%20Central)](https://central.sonatype.com/artifact/com.ragedunicorn.tools.maven/maven-code-quality)
[![Renovate enabled](docs/renovate-badge.svg)](https://renovatebot.com)

## How To Use

This dependency is usually not used as a direct dependency to a project but rather by plugin configurations such as pmd and checkstyle.

## Compatibility

Configurations in this artifact need to match with the used checkstyle and pmd version.

Supported Versions:

|            |        |
|------------|--------|
| Java       | 11+    |
| Checkstyle | 8.29   |
| PMD        | 7.24.0 |

> **Breaking changes in 2.0.0**
> - PMD 7.0+ is now required. Earlier PMD versions cannot parse the new ruleset.
> - `maven-pmd-plugin` **3.22.0 or newer** is required. Older versions still ship PMD 6 and cannot load this ruleset.
> - JSP, Velocity, XML, and XSL rules have been **removed**. The ruleset is now Java-only.
> - Many rule names changed in PMD 7. If your code uses `@SuppressWarnings("PMD.OldName")`, see the [PMD 7 migration guide](https://docs.pmd-code.org/latest/pmd_release_notes_pmd7.html#removed-rules) for the full rename mapping.

### Checkstyle Usage Example

Maven usage

```
mvn checkstyle:checkstyle
```

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <dependencies>
          <dependency>
            <groupId>com.ragedunicorn.tools.maven</groupId>
            <artifactId>maven-code-quality</artifactId>
            <version>[version]</version>
          </dependency>
          <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>8.29</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
    </plugins>
  </reporting>
  ...
</project>
```

Additionally, the config and header location properties have to be set in the project pom. 

```xml
<properties>
  <checkstyle.config.location>checkstyle.xml</checkstyle.config.location>
  <checkstyle.header.location>LICENSE.txt</checkstyle.header.location>
</properties>
```

### PMD Usage Example

Maven usage

```
mvn pmd:pmd
```

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.22.0</version>
        <configuration>
          <rulesets>
            <ruleset>pmd-ruleset.xml</ruleset>
          </rulesets>
          <printFailingErrors>true</printFailingErrors>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>com.ragedunicorn.tools.maven</groupId>
            <artifactId>maven-code-quality</artifactId>
            <version>[version]</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
  
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
    </plugins>
  </reporting>
  ...
</project>
```

## Development

#### Build Project

```
mvn clean install
```

#### Create a Release

This project has GitHub action profiles for different Devops related work such as deployments to different places. See .github folder for details.
The project is deployed to three different places. Each deployment has its own Maven profile for configuration.

##### GitHub Release

`.github/workflows/github_release.yaml` - Creates a tag and release on GitHub

##### GitHub Package Release

`.github/workflows/github_package_release.yaml` - Releases a package on GitHub

##### Maven Central Package Release

`.github/workflows/central_package_release.yaml` - Publishes a package to Maven Central via the Sonatype Central Portal

All steps are required to make a full release of the plugin but can be done independently of each other. The workflows have to be manually invoked on GitHub.

## License

Copyright (c) 2026 Michael Wiesendanger

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

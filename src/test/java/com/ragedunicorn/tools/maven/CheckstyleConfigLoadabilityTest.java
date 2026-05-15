package com.ragedunicorn.tools.maven;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckstyleConfigLoadabilityTest {

  private static final String CONFIG_RESOURCE = "checkstyle.xml";

  @Test
  void configLoadsAndCheckerAcceptsItUnderCheckstyle13() throws Exception {
    URL configUrl = getClass().getClassLoader().getResource(CONFIG_RESOURCE);
    assertNotNull(configUrl, CONFIG_RESOURCE + " not found on test classpath");

    Path headerFile = Files.createTempFile("checkstyle-header", ".txt");
    try {
      Properties props = new Properties();
      props.setProperty("checkstyle.header.file", headerFile.toString());

      Configuration config = ConfigurationLoader.loadConfiguration(
          configUrl.toString(),
          new PropertiesExpander(props),
          ConfigurationLoader.IgnoredModulesOptions.EXECUTE);
      assertNotNull(config, "ConfigurationLoader returned null configuration");
      assertTrue(config.getChildren().length > 0, "configuration has no child modules");

      Checker checker = new Checker();
      checker.setModuleClassLoader(Checker.class.getClassLoader());
      try {
        checker.configure(config);
      } finally {
        checker.destroy();
      }
    } finally {
      Files.deleteIfExists(headerFile);
    }
  }
}
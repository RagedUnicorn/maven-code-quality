package com.ragedunicorn.tools.maven;

import net.sourceforge.pmd.lang.rule.RuleSet;
import net.sourceforge.pmd.lang.rule.RuleSetLoader;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class RulesetLoadabilityTest {

  private static final String RULESET_RESOURCE = "pmd-ruleset.xml";

  @Test
  void everyRuleReferenceResolvesUnderPmd7() throws Exception {
    List<String> refs = collectRuleRefs();
    assertFalse(refs.isEmpty(), "no <rule ref> entries found in " + RULESET_RESOURCE);

    List<String> broken = new ArrayList<>();
    for (String ref : refs) {
      String inlineRuleset =
          "<?xml version=\"1.0\"?>"
          + "<ruleset xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\" name=\"probe\">"
          + "<description>probe</description>"
          + "<rule ref=\"" + ref + "\"/>"
          + "</ruleset>";
      try {
        new RuleSetLoader().loadFromString("probe.xml", inlineRuleset);
      } catch (RuntimeException e) {
        broken.add(ref + "  --  " + e.getMessage());
      }
    }

    if (!broken.isEmpty()) {
      fail("the following rule refs do not resolve under PMD 7:\n  " + String.join("\n  ", broken));
    }
  }

  @Test
  void rulesetLoadsAsAWhole() {
    RuleSet rs = new RuleSetLoader()
        .warnDeprecated(true)
        .loadFromResource(RULESET_RESOURCE);
    assertNotNull(rs);
    assertFalse(rs.getRules().isEmpty());
  }

  @Test
  void criticalReplacementRulesArePresent() {
    RuleSet rs = new RuleSetLoader().loadFromResource(RULESET_RESOURCE);
    String[] mustHave = {
        "UnnecessaryImport", "EmptyControlStatement",
        "LiteralsFirstInComparisons", "SimplifiableTestAssertion",
        "ImplicitSwitchFallThrough", "NcssCount", "UnnecessaryBoxing"
    };
    for (String name : mustHave) {
      assertNotNull(rs.getRuleByName(name), name + " missing from ruleset");
    }
  }

  @Test
  void noRuleIsDeprecated() {
    RuleSet rs = new RuleSetLoader().loadFromResource(RULESET_RESOURCE);
    List<String> deprecated = new ArrayList<>();
    rs.getRules().forEach(r -> {
      if (r.isDeprecated()) {
        deprecated.add(r.getName());
      }
    });
    if (!deprecated.isEmpty()) {
      fail("the following rules are deprecated in PMD 7 and should be replaced:\n  "
          + String.join("\n  ", deprecated));
    }
  }

  private static List<String> collectRuleRefs() throws Exception {
    try (InputStream in = RulesetLoadabilityTest.class.getClassLoader().getResourceAsStream(RULESET_RESOURCE)) {
      assertNotNull(in, "ruleset resource not found on test classpath");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(false);
      Document doc = dbf.newDocumentBuilder().parse(in);
      NodeList rules = doc.getElementsByTagName("rule");
      List<String> refs = new ArrayList<>();
      for (int i = 0; i < rules.getLength(); i++) {
        Element rule = (Element) rules.item(i);
        String ref = rule.getAttribute("ref");
        if (!ref.isEmpty()) {
          refs.add(ref);
        }
      }
      return refs;
    }
  }
}

/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks.regex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.sonar.check.Rule;
import org.sonar.java.regex.RegexParseResult;
import org.sonar.java.regex.ast.CharacterTree;
import org.sonar.java.regex.ast.FlagSet;
import org.sonar.java.regex.ast.JavaCharacter;
import org.sonar.java.regex.ast.RegexBaseVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;

@Rule(key = "S5866")
public class UnicodeCaseCheck extends AbstractRegexCheck {

  private static final String MESSAGE = "Also use %s to correctly handle non-ASCII letters.";

  @Override
  public void checkRegex(RegexParseResult regexForLiterals, MethodInvocationTree mit) {
    new Visitor(mit).visit(regexForLiterals);
  }

  private class Visitor extends RegexBaseVisitor {

    final Set<JavaCharacter> problematicFlags = new HashSet<>();

    boolean problematicFlagSetOutsideOfRegex = false;

    final MethodInvocationTree mit;

    Visitor(MethodInvocationTree mit) {
      this.mit = mit;
    }

    @Override
    protected void visitCharacter(CharacterTree tree) {
      if (isProblematic(tree.activeFlags(), tree.codePointOrUnit())) {
        JavaCharacter character = tree.activeFlags().getJavaCharacterForFlag(Pattern.CASE_INSENSITIVE);
        if (character == null) {
          problematicFlagSetOutsideOfRegex = true;
        } else {
          problematicFlags.add(character);
        }
      }
    }

    @Override
    protected void after(RegexParseResult regexParseResult) {
      if (problematicFlagSetOutsideOfRegex) {
        getFlagsTree(mit).ifPresent( flagsTree ->
          reportIssue(flagsTree, String.format(MESSAGE, "\"Pattern.UNICODE_CASE\""))
        );
      }
      for (JavaCharacter flag : problematicFlags) {
        reportIssue(flag, String.format(MESSAGE, "the \"u\" flag"), null, Collections.emptyList());
      }
    }

    boolean isNonAsciiLetter(int codePointOrUnit) {
      return codePointOrUnit > 127 && Character.isLetter(codePointOrUnit);
    }

    boolean isProblematic(FlagSet activeFlags, int codePointOrUnit) {
      return activeFlags.contains(Pattern.CASE_INSENSITIVE) && !activeFlags.contains(Pattern.UNICODE_CASE) && isNonAsciiLetter(codePointOrUnit);
    }
  }

}

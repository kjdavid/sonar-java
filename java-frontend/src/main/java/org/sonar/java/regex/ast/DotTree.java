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
package org.sonar.java.regex.ast;

import javax.annotation.Nonnull;

public class DotTree extends RegexTree {

  public DotTree(RegexSource source, IndexRange range) {
    super(source, range);
  }

  @Override
  public void accept(RegexVisitor visitor) {
    visitor.visitDot(this);
  }

  @Override
  public Kind kind() {
    return RegexTree.Kind.DOT;
  }

  @Nonnull
  @Override
  public TransitionType incomingTransitionType() {
    return TransitionType.CHARACTER;
  }

}

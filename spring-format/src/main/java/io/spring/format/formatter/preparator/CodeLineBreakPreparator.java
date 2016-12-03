/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.format.formatter.preparator;

import io.spring.formatter.eclipse.formatter.Preparator;
import io.spring.formatter.eclipse.formatter.Token;
import io.spring.formatter.eclipse.formatter.TokenManager;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

/**
 * {@link Preparator} to fine tune curly-brace line breaks.
 *
 * @author Phillip Webb
 */
public class CodeLineBreakPreparator implements Preparator {

	@Override
	public void apply(TokenManager tokenManager, ASTNode astRoot) {
		ASTVisitor visitor = new Vistor(tokenManager);
		astRoot.accept(visitor);
	}

	private class Vistor extends ASTVisitor {

		private final TokenManager tokenManager;

		public Vistor(TokenManager tokenManager) {
			this.tokenManager = tokenManager;
		}

		@Override
		public boolean visit(TypeDeclaration node) {
			visitType(node);
			return true;
		}

		@Override
		public boolean visit(AnnotationTypeDeclaration node) {
			visitType(node);
			return true;
		}

		private void visitType(AbstractTypeDeclaration node) {
			SimpleName name = node.getName();
			int openBraceIndex = (name == null
					? this.tokenManager.firstIndexIn(node, TerminalTokens.TokenNameLBRACE)
					: this.tokenManager.firstIndexAfter(name,
							TerminalTokens.TokenNameLBRACE));
			Token openBraceToken = this.tokenManager.get(openBraceIndex);
			openBraceToken.clearLineBreaksAfter();
			openBraceToken.putLineBreaksAfter(2);
			int closeBraceIndex = this.tokenManager.lastIndexIn(node,
					TerminalTokens.TokenNameRBRACE);
			Token closeBraceToken = this.tokenManager.get(closeBraceIndex);
			closeBraceToken.clearLineBreaksBefore();
			closeBraceToken.putLineBreaksBefore(2);
		}

		@Override
		public boolean visit(FieldDeclaration node) {
			Token token = this.tokenManager.lastTokenIn(node,
					TerminalTokens.TokenNameSEMICOLON);
			token.clearLineBreaksAfter();
			token.putLineBreaksAfter(2);
			return true;
		}

	}

}

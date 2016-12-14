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
import org.eclipse.jdt.core.dom.EnumDeclaration;
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

		private static final int ANY_TOKEN = -1;

		private final TokenManager tokenManager;

		Vistor(TokenManager tokenManager) {
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

		@Override
		public boolean visit(EnumDeclaration node) {
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

		// @Override
		// public boolean visit(FieldDeclaration node) {
		// int typeIndex = this.tokenManager.firstIndexIn(node.getType(), ANY_TOKEN);
		// int firstIndexInLine = this.tokenManager.findFirstTokenInLine(typeIndex);
		// int lastIndex = this.tokenManager.lastIndexIn(node, ANY_TOKEN);
		// lastIndex = Math.min(lastIndex, this.tokenManager.size() - 2);
		// for (int i = firstIndexInLine; i <= lastIndex; i++) {
		// Token token = this.tokenManager.get(i);
		// Token next = this.tokenManager.get(i + 1);
		// // boolean lineBreak = token.getLineBreaksAfter() > 0
		// // || next.getLineBreaksBefore() > 0;
		// // if (lineBreak) {
		// // if (token.tokenType == TokenNameCOMMENT_BLOCK) {
		// // token.setAlign(maxCommentAlign);
		// // }
		// // else {
		// // this.tm.addNLSAlignIndex(i, maxCommentAlign);
		// // }
		// // }
		// // else if (next.tokenType == TokenNameCOMMENT_LINE
		// // || (next.tokenType == TokenNameCOMMENT_BLOCK && i == lastIndex)) {
		// // next.setAlign(maxCommentAlign);
		// // }
		// }
		// return true;
		// }

		@Override
		public boolean visit(FieldDeclaration node) {
			int index = this.tokenManager.lastIndexIn(node,
					TerminalTokens.TokenNameSEMICOLON);
			while (tokenIsOfType(index + 1, TerminalTokens.TokenNameCOMMENT_LINE,
					TerminalTokens.TokenNameCOMMENT_BLOCK)) {
				index++;
			}
			Token token = this.tokenManager.get(index);
			token.clearLineBreaksAfter();
			token.putLineBreaksAfter(2);
			return true;
		}

		private boolean tokenIsOfType(int index, int... types) {
			if (index < this.tokenManager.size()) {
				Token token = this.tokenManager.get(index);
				for (int type : types) {
					if (token.tokenType == type) {
						return true;
					}
				}
			}
			return false;
		}

	}

}

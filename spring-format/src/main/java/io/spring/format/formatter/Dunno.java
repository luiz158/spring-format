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

package io.spring.format.formatter;

import java.util.List;

import io.spring.formatter.eclipse.formatter.Preparator;
import io.spring.formatter.eclipse.formatter.Token;
import io.spring.formatter.eclipse.formatter.TokenManager;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

/**
 * TODO.
 *
 * @author Phillip Webb
 */
public class Dunno implements Preparator {

	@Override
	public void apply(TokenManager tokenManager, ASTNode astRoot) {
		List<Comment> comments = getComments(astRoot);
		if (comments != null) {
			Vistor visitor = new Vistor(tokenManager);
			for (Comment comment : comments) {
				comment.accept(visitor);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<Comment> getComments(ASTNode astRoot) {
		if (astRoot.getRoot() instanceof CompilationUnit) {
			CompilationUnit compilationUnit = (CompilationUnit) astRoot.getRoot();
			return compilationUnit.getCommentList();
		}
		return null;
	}

	private class Vistor extends ASTVisitor {

		private final TokenManager tokenManager;

		private List<Token> commentStructure;

		private Object commentIndent;

		private TokenManager commentTokenManager;

		Vistor(TokenManager tokenManager) {
			this.tokenManager = tokenManager;
		}

		@Override
		public boolean visit(Javadoc node) {
			int commentIndex = this.tokenManager.firstIndexIn(node,
					TerminalTokens.TokenNameCOMMENT_JAVADOC);
			Token commentToken = this.tokenManager.get(commentIndex);
			this.commentStructure = commentToken.getInternalStructure();
			this.commentIndent = this.tokenManager.toIndent(commentToken.getIndent(),
					true);
			this.commentTokenManager = new TokenManager(
					commentToken.getInternalStructure(), this.tokenManager);
			// for (Token token : this.commentTokenManager) {
			// // System.out.println(token.getClass());
			// // token.breakAfter();
			// }
			return true;
		}

		@Override
		public boolean visit(TagElement node) {
			if ("@xxreturn".equals(node.getTagName())) {
				System.out.println(node.getTagName() + " " + node.getParent());
				int startIndex = tokenStartingAt(node.getStartPosition());
				Token token = this.commentTokenManager.get(startIndex);
				token.clearLineBreaksBefore();
				token.breakBefore();
				System.out.println(token);
			}
			return true;
		}

		private int tokenStartingAt(int start) {
			int tokenIndex = this.commentTokenManager.findIndex(start, -1, false);
			Token token = this.commentTokenManager.get(tokenIndex);
			if (token.originalStart == start) {
				return tokenIndex;
			}

			assert start > token.originalStart && start <= token.originalEnd;
			splitToken(token, tokenIndex, start);
			return tokenIndex + 1;
		}

		private void splitToken(Token token, int tokenIndex, int splitPosition) {
			assert splitPosition > token.originalStart
					&& splitPosition <= token.originalEnd;

			Token part1 = new Token(token.originalStart, splitPosition - 1,
					token.tokenType);
			Token part2 = new Token(splitPosition, token.originalEnd, token.tokenType);
			if (token.isSpaceBefore()) {
				part1.spaceBefore();
			}
			part1.putLineBreaksBefore(token.getLineBreaksBefore());
			if (token.isSpaceAfter()) {
				part2.spaceAfter();
			}
			part2.putLineBreaksAfter(token.getLineBreaksAfter());
			part1.setIndent(token.getIndent());
			part2.setIndent(token.getIndent());
			part1.setWrapPolicy(token.getWrapPolicy());
			this.commentStructure.set(tokenIndex, part1);
			this.commentStructure.add(tokenIndex + 1, part2);
		}

	}

}

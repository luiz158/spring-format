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

import io.spring.formatter.eclipse.formatter.Token;
import io.spring.formatter.eclipse.formatter.TokenManager;

/**
 * @author Phillip Webb
 */
public class NestedTokenManager extends TokenManager {

	// FIXME might not be required since splitToken is not called

	private final List<Token> structure;

	public NestedTokenManager(Token token, TokenManager tokenManager) {
		super(token.getInternalStructure(), tokenManager);
		this.structure = token.getInternalStructure();
	}

	public int tokenStartingAt(int start) {
		int tokenIndex = findIndex(start, -1, false);
		Token token = get(tokenIndex);
		if (token.originalStart == start) {
			return tokenIndex;
		}
		splitToken(token, tokenIndex, start);
		return tokenIndex + 1;
	}

	private void splitToken(Token token, int index, int splitPosition) {
		assert ((splitPosition > token.originalStart)
				&& (splitPosition <= token.originalEnd));
		Token left = new Token(token.originalStart, splitPosition - 1, token.tokenType);
		Token right = new Token(splitPosition, token.originalEnd, token.tokenType);
		if (token.isSpaceBefore()) {
			left.spaceBefore();
		}
		left.putLineBreaksBefore(token.getLineBreaksBefore());
		if (token.isSpaceAfter()) {
			right.spaceAfter();
		}
		right.putLineBreaksAfter(token.getLineBreaksAfter());
		left.setIndent(token.getIndent());
		right.setIndent(token.getIndent());
		left.setWrapPolicy(token.getWrapPolicy());
		this.structure.set(index, left);
		this.structure.add(index + 1, right);
	}

}

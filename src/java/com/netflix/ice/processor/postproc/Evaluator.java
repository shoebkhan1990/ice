/*
 *
 *  Copyright 2013 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.ice.processor.postproc;

import java.util.Deque;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Expression evaluator implementing the Shunting-Yard algorithm.
 * Supports basic arithmetic operations: + - * / and use of parenthesis.
 */
public class Evaluator {
	static Map<String, Operator> ops;
	
	static {
		ops = Maps.newHashMap();
		ops.put("+", Operator.Add);
		ops.put("-", Operator.Subtract);
		ops.put("*", Operator.Multiply);
		ops.put("/", Operator.Divide);
		ops.put("(", Operator.Left);
		ops.put(")", Operator.Right);
	}
	
	private List<String> parse(String expr) {
		List<String> tokens = Lists.newArrayList();
		
		String operand = "";
		boolean operandIsNumber = false;
		for (Character c: expr.toCharArray()) {
			switch (c) {
			case '+':
			case '-':
			case '*':
			case '/':
			case '(':
			case ')':
				if (!operand.isEmpty()) {
					if (c == '-' && operandIsNumber && operand.endsWith("E")) {
						// Handle scientific notation with negative exponent
						operand += c.toString();
						break;
					}
					tokens.add(operand);
					operand = "";
				}
				tokens.add(c.toString());
				break;
				
			case ' ':
			case '\t':
				if (!operand.isEmpty()) {
					tokens.add(operand);
					operand = "";
				}
				break;
				
			default:
				if (operand.isEmpty())
					operandIsNumber = Character.isDigit(c);
				operand += c.toString();
			}
		}
		if (!operand.isEmpty())
			tokens.add(operand);
		
		return tokens;
	}
	
	public enum Operator {
		Add(1),
		Subtract(2),
		Multiply(3),
		Divide(4),
		Left(0),
		Right(0);
		
		final int precedence;
		
		Operator(int precedence) {
			this.precedence = precedence;
		}
	}
		
	Double eval(String expr) throws Exception {
		// Convert to RPN using simplified shunting-yard algorithm
		Deque<String> output = Lists.newLinkedList();
		Deque<String> operators = Lists.newLinkedList();
		
		for (String token: parse(expr)) {
			if (ops.containsKey(token)) {
				// operator
				Operator op = ops.get(token);
				Operator topOp = null;
				
				switch (op) {
				case Left:
					operators.push(token);
					break;
					
				case Right:
					topOp = ops.get(operators.peek());
					while (topOp != null && topOp != Operator.Left) {
						output.push(operators.pop());
						topOp = ops.get(operators.peek());
					}
					if (topOp != Operator.Left)
						throw new Exception("Missing left parentheses in expression");
					operators.pop();
					break;
					
				default:
					for (topOp = ops.get(operators.peek());
								topOp != null && topOp.precedence >= op.precedence && topOp != Operator.Left;
								topOp = ops.get(operators.peek())) {
						output.push(operators.pop());
					}
					operators.push(token);
					break;
				}
			}
			else {
				// number
				output.push(token);
			}
		}
		while (operators.size() > 0) {
			String token = operators.pop();
			if (ops.get(token) == Operator.Left)
				throw new Exception("Missing left parentheses in expression");
			output.push(token);
		}
		
		// Evaluate the RPN
		return evalRpn(output);
	}
	
	private Double evalRpn(Deque<String> tokens) throws Exception {
		String token = tokens.pop();
		Double x;
		
		if (ops.containsKey(token)) {
			Operator op = ops.get(token);
			Double y = evalRpn(tokens);
			x = evalRpn(tokens);
			switch(op) {
			case Add:		x += y; break;
			case Subtract:	x -= y; break;
			case Multiply:	x *= y; break;
			case Divide:	x /= y; break;
			default:
				throw new Exception("Error in RPN expression");
			}
		}
		else {
			x = Double.parseDouble(token);
		}
		
		return x;
	}
}

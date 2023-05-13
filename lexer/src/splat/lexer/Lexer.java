package splat.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

	private File progFile;

	public Lexer(File progFile) {
		// TODO Auto-generated constructor stub
		this.progFile = progFile;
	}

	public List<Token> tokenize() throws LexException, IOException {
		// TODO Auto-generated method stub
		List<Token> tokens = new ArrayList<Token>();
		BufferedReader br = new BufferedReader(new FileReader(progFile));
		String line = "";
		String readLine = null;
		int lineNum = 1;
		boolean isComplexOperator = false;
		while ((readLine = br.readLine()) != null) {
			for (int i = 0; i < readLine.length(); i++) {
				char c = readLine.charAt(i);
				if (c == ' ' || c == '\t') {
					if (line != "") {
						if (line.contains("\"")) {
							line += c;
							continue;
						}
						tokens.add(new Token(line, lineNum, i));
						line = "";
					}
					continue;
				}

				if (c == '"') {
					if (line.contains("\"")) {
						line += c;
						tokens.add(new Token(line, lineNum, i));
						line = "";
					} else {
						if (line == "") {
							line += c;
						} else {
							tokens.add(new Token(line, lineNum, i));
							line = c + "";
						}
					}
					continue;
				}

				if (line.contains("\"")) {
					line += c;
					continue;
				}

				if (Character.isLetter(c) || c == '_') {
					if (line == "") {
						line += c;
						continue;
					}

					char firstChar = line.charAt(0);
					if (Character.isLetter(firstChar) || firstChar == '_') {
						line += c;
						continue;
					} else if (!Character.isDigit(firstChar)) {
						if (firstChar == '=') {
							throw new LexException("Invalid operators", lineNum, i);
						}
						tokens.add(new Token(line, lineNum, i));
						line = c + "";
						continue;
					} else {
						throw new LexException("Invalid token", lineNum, i);
					}

				}

				if (Character.isDigit(c)) {
					if (line == "") {
						line += c;
						continue;
					}

					char firstChar = line.charAt(0);
					if (Character.isDigit(firstChar) || firstChar == '_' || Character.isLetter(firstChar)) {
						line += c;
						continue;
					} else {
						tokens.add(new Token(line, lineNum, i));
						line = "";
						line += c;
						continue;
					}

				}

				// if character was not a letter or a digit
				Character[] operators = { '+', '-', '*', '/', '=', '>', '<', '%', ':', ';',
						',', '.', '(', ')' };
				if (Arrays.asList(operators).contains(c)) {
					if (line == "") {
						if (c == '<' || c == '>' || c == '=' || c == ':') {
							isComplexOperator = true;
							line += c;
							continue;
						} else {
							tokens.add(new Token(c + "", lineNum, i));
							continue;
						}

					}

					char firstChar = line.charAt(0);
					if (Character.isLetter(firstChar) || firstChar == '_' || Character.isDigit(firstChar)) {
						tokens.add(new Token(line, lineNum, i));
						line = "";
					}

					if (line != "") {
						if (isComplexOperator) {
							if (c == '=') {
								line += c;
								tokens.add(new Token(line, lineNum, i));
								line = "";
								isComplexOperator = false;
								continue;
							} else {
								tokens.add(new Token(line, lineNum, i));
								line = "";
								isComplexOperator = false;
							}
						}

						if (c == '<' || c == '>' || c == '=' || c == ':') {
							isComplexOperator = true;
							line += c;
							continue;
						} else {
							tokens.add(new Token(line, lineNum, i));
							line = "";
						}
					} else {
						if (c == '<' || c == '>' || c == '=' || c == ':') {
							isComplexOperator = true;
							line += c;
							continue;
						} else {
							line += c;
							tokens.add(new Token(line, lineNum, i));
							line = "";
						}
					}

				} else {
					throw new LexException("Invalid character: " + c, lineNum, i);
				}
			}
			if (line != "" && !line.contains("\"")) {
				tokens.add(new Token(line, lineNum, readLine.length()));
				line = "";
			}
			lineNum++;
		}
		if (line != "") {
			if (line.contains("\"")) {
				throw new LexException("Invalid string", lineNum, line.length());
			}
			tokens.add(new Token(line, lineNum, 0));
		}
		br.close();
		return tokens;
	}

}

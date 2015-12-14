package main;

import java.util.Arrays;
import java.util.Random;

public class WordJumbler {

	private class Word {
		String[] word;
		String firstChar;
		String lastChar;

		public Word(String newWord) {
			String[] cut = newWord.split("");
			firstChar = cut[1];
			lastChar = cut[cut.length - 1];
			cut = Arrays.copyOfRange(cut, 2, cut.length - 1);
			word = cut;
		}

		public boolean doneRemoving() {
			return word.length == 0;
		}

		private String removeChar(int index) {
			if (index >= 0 && index < word.length && !doneRemoving()) {
				String removed = word[index];
				for (int i = index; i < word.length - 1; i++) {
					word[i] = word[i + 1];
				}
				word = Arrays.copyOf(word, word.length - 1);
				return removed;
			}
			return null;
		}

		private String jumble() {
			String jumbled = firstChar;
			System.out.println("First char = " + firstChar);
			System.out.println("Last char = " + lastChar);
			System.out.println("");
			Random random = new Random();
			while (!doneRemoving()) {
				jumbled += removeChar(random.nextInt(word.length));
			}
			jumbled += lastChar;
			return jumbled;
		}
	}

	public String jumbleWord(String word) {
		if (word.length() <= 3) {
			return word;
		}
		Word toJumble = new Word(word);
		System.out.println(word);
		return toJumble.jumble();
	}

	public String jumbleWords(String words) {
		if (words.length() <= 3) {
			return words;
		}
		String jumbled = "";
		String[] cutWords = words.split(" ");
		for (int i = 0; i < cutWords.length; i++) {
			jumbled += " " + jumbleWord(cutWords[i]);
		}
		return jumbled;
	}
}

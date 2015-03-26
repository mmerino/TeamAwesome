package team.awesome;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import team.misc.ArrayOrganizer;
import team.misc.BinarySearcher;

public class WordFinder {
	private ArrayList<String> wordList = new ArrayList<>();
	private ArrayList<String> matchingWords = new ArrayList<>();
	private String text;

	protected WordFinder(ArrayList<String> wordList, String text) {
		this.wordList = wordList;
		this.text = text;
		findWords();
	}

	protected void findWords() {
		ArrayList<String> textArray = new ArrayList<>();

		Document doc = Jsoup.parse(text);
		Elements elements = doc.select("p");
		for (Element element : elements) {
			textArray.add(element.text());
		}

		ArrayList<String> siteWords = ArrayOrganizer.createArray(textArray,
				".?! ,()[]\"");
		HashMap<String, Integer> articleContains = BinarySearcher.search(
				wordList, siteWords);
		matchingWords.addAll(articleContains.keySet());
	}

	public ArrayList<String> getMatches() {
		return matchingWords;
	}
}

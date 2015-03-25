package team.awesome;

import team.misc.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ArticleSearch() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String inputUrl = request.getParameter("URL");
		InputStream wordsStream = getClass().getResourceAsStream("/src/main/resources/words.txt");
		FileReaderObject words = new FileReaderObject(wordsStream);
		ArrayList<String> wordList = words.sanitizeText(" ,\"");

		URLContentExtractor urlce = new URLContentExtractor();

		ArrayList<String> textArray = new ArrayList<>();
		ArrayList<String> textWordsArray = new ArrayList<>();
		HashMap<String, Integer> articleContains = new HashMap<>();
		ArrayList<String> articleWords = new ArrayList<>();

		URL url = new URL(inputUrl);
		String text = urlce.read(url);

		Document doc = Jsoup.parse(text, "UTF-8");
		Elements elements = doc.select("p");
		for (Element element : elements) {
			textArray.add(element.text());
		}

		textWordsArray = ArrayOrganizer.createArray(textArray, ".?! ,()[]\"");
		articleContains = BinarySearcher.search(wordList, textWordsArray);

		Set<String> keySet = articleContains.keySet();
		articleWords.addAll(keySet);
		String markedText = MarkUpText.markUp(text, articleWords);
		
		request.setAttribute("marked text", markedText);
		response.getWriter().write(markedText);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}

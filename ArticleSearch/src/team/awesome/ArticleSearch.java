package team.awesome;

import team.misc.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			String inputUrl = request.getParameter("URL");
			InputStream wordsStream = getClass().getResourceAsStream(
					"/src/main/resources/words.txt");
			FileReaderObject words = new FileReaderObject(wordsStream);
			ArrayList<String> wordList = words.sanitizeText(" ,\"");

			URLContentExtractor urlce = new URLContentExtractor();

			ArrayList<String> textArray = new ArrayList<>();
			ArrayList<String> textWordsArray = new ArrayList<>();
			HashMap<String, Integer> articleContains = new HashMap<>();
			ArrayList<String> articleWords = new ArrayList<>();

			URL url = new URL(inputUrl);
			String text = urlce.read(url);
			String baseUri = url.getProtocol() + "://" + url.getHost();

			Document doc = Jsoup.parse(text, baseUri);

			Elements elements = doc.select("p");
			for (Element element : elements) {
				textArray.add(element.text());
			}

			textWordsArray = ArrayOrganizer.createArray(textArray,
					".?! ,()[]\"");
			articleContains = BinarySearcher.search(wordList, textWordsArray);

			Set<String> keySet = articleContains.keySet();
			articleWords.addAll(keySet);
			String markedText = MarkUpText.markUp(text, articleWords);

			Document markedDoc = Jsoup.parse(markedText, baseUri);
			Elements css = markedDoc.select("link[href]");
			for (Element element : css) {
				if (!element.attr("href").toLowerCase().startsWith("http") ) {
					element.attr("href", doc.baseUri() + element.attr("href"));
				}
			}
			markedText = markedDoc.html();

			request.setAttribute("marked text", markedText);
			response.getWriter().write(markedText);
		} catch (MalformedURLException mue) {
			request.setAttribute("message",
					"Bad URL. Please input a valid URL.");
			getServletContext().getRequestDispatcher("/SearchForm.jsp")
					.forward(request, response);
		} catch (Exception exc) {
			request.setAttribute("message", exc.getMessage());
			getServletContext().getRequestDispatcher("/SearchForm.jsp")
					.forward(request, response);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}

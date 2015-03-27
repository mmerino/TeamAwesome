package team.awesome;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import team.misc.FileReaderObject;
import team.misc.MarkUpText;
import team.misc.URLContentExtractor;

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
					"/resources/words.txt");
			ArrayList<String> wordList = new FileReaderObject(wordsStream)
					.sanitizeText(" ,\"");

			URL url = new URL(inputUrl);
			String siteText = new URLContentExtractor().read(url);
			String baseUri = url.getProtocol() + "://" + url.getHost();

			ArrayList<String> matchingWords = new WordFinder(wordList, siteText).getMatches();
			String markedText = MarkUpText.markUp(siteText, matchingWords);
			markedText = new CssModifier(markedText, baseUri).getCssText();

			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
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

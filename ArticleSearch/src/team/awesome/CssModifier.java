package team.awesome;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssModifier {
	private String markedText;
	private String baseUri;
	private String cssFriendlyText;

	protected CssModifier(String markedText, String baseUri) {
		this.markedText = markedText;
		this.baseUri = baseUri;
		this.cssFriendlyText = modifyCss();
	}

	protected String modifyCss() {
		if (markedText == null || markedText.equals("") || baseUri == null || baseUri.equals("")) {
			return markedText;
		}
		// Changes relative CSS URL paths to absolute for proper site display
		Document markedDoc = Jsoup.parse(markedText, baseUri);
		Elements css = markedDoc.select("link[href]");
		for (Element element : css) {
			if (!element.attr("href").toLowerCase().startsWith("http")) {
				element.attr("href", markedDoc.baseUri() + element.attr("href"));
			}
		}
		return markedDoc.html();
	}

	public String getCssText() {
		return cssFriendlyText;
	}
}

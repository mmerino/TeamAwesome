package team.awesome;

import static org.junit.Assert.*;

import org.junit.Test;

public class CssModifierTest {
	String actual;
	String expected;
	String htmlStart = "<html>\n <head>";
	String htmlEnd = "</head>\n <body></body>\n</html>";
	String baseUri = "http://asdf.com";

	@Test
	public void inputNothingReturnNothing() {
		actual = new CssModifier("", baseUri).getCssText();
		expected = "";
		assertEquals(expected, actual);
	}
	
	@Test
	public void inputAbsoluteReturnsAbsolute() {
		String absolute = "\n  <link rel=\"ayy\" href=\"http://asdf.com/css.csss\"> \n ";
		actual = new CssModifier(absolute, baseUri).getCssText();
		expected = htmlStart + absolute + htmlEnd;
		assertEquals(expected, actual);
	}
	
	@Test
	public void inputRelativeReturnsAbsolute() {
		String relative = "\n  <link rel=\"ayy\" href=\"/css.csss\"> \n ";
		String absolute = "\n  <link rel=\"ayy\" href=\"http://asdf.com/css.csss\"> \n ";
		actual = new CssModifier(relative, baseUri).getCssText();
		expected = htmlStart + absolute + htmlEnd;
		assertEquals(expected, actual);
	}
	
	@Test
	public void inputNonsenseReturnNonsense() {
		String nonsense = "this is not html";
		actual = new CssModifier(nonsense, baseUri).getCssText();
		expected = "<html>\n <head></head>\n <body>\n  " + nonsense + "\n </body>\n</html>";
		assertEquals(expected, actual);
	}
}

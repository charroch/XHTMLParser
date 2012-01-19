package novoda.xml.handler;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Carl-Gustaf Harroch (carl@novoda.com)
 * Date: 19/01/12
 * Time: 13:22
 */
public class XHTML {

    public static void parse(InputStream xml, String encoding, ContentHandler handler) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        InputSource inputSource = new InputSource(xml);
        inputSource.setEncoding(encoding);
        reader.setContentHandler(handler);
        reader.parse(inputSource);
    }
}

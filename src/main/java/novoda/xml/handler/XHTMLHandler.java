package novoda.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XHTMLHandler extends DefaultHandler {

    private XHTMLCallback cb;

    private StringBuilder bodyBuilder;
    private Attributes currentAttributes;

    @Override
    public void startDocument() throws org.xml.sax.SAXException {
        bodyBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        currentAttributes = attributes;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        XHTMLCallback.Tag t = XHTMLCallback.Tag.valueOf(qName.toUpperCase());
        switch (t) {
            case P:
                break;
            case DIV:
                cb.onTag(XHTMLCallback.Tag.DIV, currentAttributes, bodyBuilder.toString());
                break;
            case HEADER:
                break;
            case BODY:
                break;
        }
        bodyBuilder = new StringBuilder();
    }

    @Override
    public void characters(char[] buffer, int start, int length) throws SAXException {
        if (bodyBuilder != null) {
            bodyBuilder.append(buffer, start, length);
        }
    }

    public void setCallback(XHTMLCallback cb) {
        this.cb = cb;
    }

}

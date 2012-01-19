package novoda.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XHTMLHandler extends DefaultHandler {

    private XHTMLCallback cb;

    private StringBuilder bodyBuilder;
    private Attributes currentAttributes;
    private String currentLocalName;
    private boolean isMixedContent;

    @Override
    public void startDocument() throws org.xml.sax.SAXException {
        bodyBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (isLocalNameTextTag(qName)) {
            bodyBuilder.append('<').append(qName).append('>');
        }
        currentAttributes = attributes;
        currentLocalName = localName;
        isMixedContent = isMixedContentAtStart();
    }

    private boolean isLocalNameTextTag(String localName) {
        try {
            XHTMLCallback.TextStyle.valueOf(localName.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (isLocalNameTextTag(qName)) {
            bodyBuilder.append("</").append(qName).append('>');
        }

        try {
            XHTMLCallback.Tag tag = XHTMLCallback.Tag.valueOf(qName.toUpperCase());
            cb.onTag(tag, currentAttributes, bodyBuilder.toString());
        } catch (IllegalArgumentException e) {
            // log no valid tag
        }

        if (!isMixedContent) {
            bodyBuilder = new StringBuilder();
            isMixedContent = false;
        }
    }

    @Override
    public void characters(char[] buffer, int start, int length) throws SAXException {
        if (bodyBuilder != null) {
            bodyBuilder.append(buffer, start, length);
        }
    }

    public boolean isMixedContentAtStart() {
        return !(bodyBuilder != null && bodyBuilder.length() == 0);
    }

    public void setCallback(XHTMLCallback cb) {
        this.cb = cb;
    }
}

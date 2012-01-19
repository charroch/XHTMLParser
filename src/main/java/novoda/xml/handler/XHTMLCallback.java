package novoda.xml.handler;

import org.xml.sax.Attributes;

public interface XHTMLCallback {

    enum Tag {
        P, DIV, HEADER, BODY, TITLE
    }

    enum TextStyle {
        B, I
    }

    void onTag(Tag what, Attributes attributes, String content);
}
package novoda.xml.handler;

import org.xml.sax.Attributes;

public interface XHTMLCallback {

    enum Tag {
        P, DIV, HEADER, BODY
    }

    enum TextStyle {
        B, I
    }

    void onTag(Tag what, Attributes attributes, String content);
}
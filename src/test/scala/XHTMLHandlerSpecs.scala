import java.io.ByteArrayInputStream
import novoda.xml.handler._
import org.hamcrest.Description
import org.specs2.mutable._
import org.specs2.mock._
import org.xml.sax.Attributes
import novoda.xml.handler.XHTMLCallback.Tag
import org.mockito.Matchers.{eq => isEq}

class XHTMLHandlerSpecs extends Specification with Mockito {

  "The parser" should {

    import SpecialMatchers._

    val handler = new XHTMLHandler();

    implicit def xml2is(d: scala.xml.Elem): java.io.InputStream = new ByteArrayInputStream(d.toString.getBytes())
    implicit def string2is(d: String): java.io.InputStream = new ByteArrayInputStream(d.getBytes())


    "handles a simple div tag" in {
      val cb = mock[XHTMLCallback]
      handler.setCallback(cb)

      val xhtml = <div>hello World</div>
      XHTML.parse(xhtml, "UTF-8", handler)

      there was one(cb).onTag(isEq(Tag.DIV), any[Attributes], isEq("hello World"))

      val div = <div/>
      XHTML.parse(div, "UTF-8", handler)
      there was one(cb).onTag(isEq(Tag.DIV), any[Attributes], isEq(""))
    }

    "handles attributes" in {
      val cb = mock[XHTMLCallback]
      handler.setCallback(cb)

      val div = <div class="myclass"></div>
      XHTML.parse(div, "UTF-8", handler)

      there was one(cb).onTag(isEq(Tag.DIV), attributeThat({
        a: Attributes =>
          a.getValue("class") === "myclass"
      }), isEq(""))
    }

    "handles mixed content" in {
      val cb = mock[XHTMLCallback]
      handler.setCallback(cb)

      val xhtml = """<div>hello <b>World</b>!!!</div>"""
      XHTML.parse(xhtml, "UTF-8", handler)

      there was one(cb).onTag(isEq(Tag.DIV), any[Attributes], isEq("hello <b>World</b>!!!"))
    }

    "handles inner tag" in {
      val cb = mock[XHTMLCallback]
      handler.setCallback(cb)

      val xhtml = <div><p>hello</p></div>
      XHTML.parse(xhtml, "UTF-8", handler)

      there was one(cb).onTag(isEq(Tag.P), any[Attributes], isEq("hello"))
    }

    "handles 2 tag callback" in new mockable {

      val xhtml = <div><p>hello</p><p>world</p></div>

      XHTML.parse(xhtml, "UTF-8", handler)

      there was one(cb).onTag(isEq(Tag.P), any[Attributes], isEq("hello")) then one(cb).onTag(isEq(Tag.P), any[Attributes], isEq("world"))
    }

    "handles doctype" in new mockable {
      val xhtml = """<html xmlns="http://www.w3.org/1999/xhtml"><head><title>Letters <b>in bold</b> !</title></head></html>"""

      XHTML.parse(xhtml, "UTF-8", handler)
      there was one(cb).onTag(isEq(Tag.TITLE), any[Attributes], isEq("Letters <b>in bold</b> !"))

    }


  }

  trait mockable extends Before {
    lazy val cb = mock[XHTMLCallback]
    val handler = new XHTMLHandler();

    def before = {
      handler.setCallback(cb)
    }
  }

  object SpecialMatchers {
    def attributeThat(f: (Attributes => Boolean)) = anArgThat(new AttributeMatcher(f))
  }

  class AttributeMatcher(f: (Attributes => Boolean)) extends org.hamcrest.BaseMatcher[Attributes] {
    def describeTo(description: Description) {
      "An attribute should match"
    }

    def matches(item: AnyRef) = f(item.asInstanceOf[Attributes])
  }

}
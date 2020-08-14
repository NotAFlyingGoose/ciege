package com.runningmanstudios.caffeineGameEngine.rendering.style;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * contains several methods and other things related to style
 */
public class HtmlStyle implements Serializable {
    /**
     * colors taken from <a href="https://www.rapidtables.com/web/color/html-color-codes.html">here</a>
     */
    private static final Map<String, Color> colors = new HashMap<>();
    static {
        //shades of red
        colors.put("lightsalmon", new Color(255, 160, 122));
        colors.put("salmon", new Color(250, 128, 114));
        colors.put("darksalmon", new Color(233, 150, 122));
        colors.put("lightcoral", new Color(240, 128, 128));
        colors.put("indianred", new Color(205, 92, 92));
        colors.put("crimson", new Color(220,20,60));
        colors.put("firebrick", new Color(178,34,34));
        colors.put("red", new Color(255,0,0));
        colors.put("darkred", new Color(139,0,0));
        //shades of orange
        colors.put("coral", new Color(255,127,80));
        colors.put("tomato", new Color(255,99,71));
        colors.put("orangered", new Color(255,69,0));
        colors.put("gold", new Color(255,215,0));
        colors.put("orange", new Color(255,165,0));
        colors.put("darkorange", new Color(255,140,0));
        //shades of yellow
        colors.put("lightyellow", new Color(255,255,224));
        colors.put("lemonchiffon", new Color(255,250,205));
        colors.put("lightgoldenrodyellow", new Color(250,250,210));
        colors.put("papayawhip", new Color(255,239,213));
        colors.put("moccasin", new Color(255,228,181));
        colors.put("peachpuff", new Color(255,218,185));
        colors.put("palegoldenrod", new Color(238,232,170));
        colors.put("khaki", new Color(240,230,140));
        colors.put("darkkhaki", new Color(189,183,107));
        colors.put("yellow", new Color(255,255,0));
        //shades of green
        colors.put("lawngreen", new Color(124,252,0));
        colors.put("chartreuse", new Color(127,255,0));
        colors.put("limegreen", new Color(50,205,50));
        colors.put("lime", new Color(0,255,0));
        colors.put("forestgreen", new Color(34,139,34));
        colors.put("green", new Color(0,128,0));
        colors.put("darkgreen", new Color(0,100,0));
        colors.put("greenyellow", new Color(173,255,47));
        colors.put("yellowgreen", new Color(154,205,50));
        colors.put("springgreen", new Color(0,255,127));
        colors.put("mediumspringgreen", new Color(0,250,154));
        colors.put("lightgreen", new Color(144,238,144));
        colors.put("palegreen", new Color(152,251,152));
        colors.put("darkseagreen", new Color(143,188,143));
        colors.put("mediumseagreen", new Color(60,179,113));
        colors.put("seagreen", new Color(46,139,87));
        colors.put("olive", new Color(128,128,0));
        colors.put("darkolivegreen", new Color(85,107,47));
        colors.put("olivedrab", new Color(107,142,35));
        //shades of cyan
        colors.put("lightcyan", new Color(224,255,255));
        colors.put("cyan", new Color(0,255,255));
        colors.put("aqua", new Color(0,255,255));
        colors.put("aquamarine", new Color(127,255,212));
        colors.put("mediumaquamarine", new Color(102,205,170));
        colors.put("paleturquoise", new Color(175,238,238));
        colors.put("turquoise", new Color(64,224,208));
        colors.put("mediumturquoise", new Color(72,209,204));
        colors.put("darkturquoise", new Color(0,206,209));
        colors.put("lightseagreen", new Color(32,178,170));
        colors.put("cadetblue", new Color(95,158,160));
        colors.put("darkcyan", new Color(0,139,139));
        colors.put("teal", new Color(0,128,128));
        //shades of blue
        colors.put("powderblue", new Color(176,224,230));
        colors.put("lightblue", new Color(173,216,230));
        colors.put("lightskyblue", new Color(135,206,250));
        colors.put("skyblue", new Color(135,206,235));
        colors.put("deepskyblue", new Color(0,191,255));
        colors.put("lightsteelblue", new Color(176,196,222));
        colors.put("dodgerblue", new Color(30,144,255));
        colors.put("cornflowerblue", new Color(100,149,237));
        colors.put("steelblue", new Color(70,130,180));
        colors.put("royalblue", new Color(65,105,225));
        colors.put("blue", new Color(0,0,255));
        colors.put("mediumblue", new Color(0,0,205));
        colors.put("darkblue", new Color(0,0,139));
        colors.put("navy", new Color(0,0,128));
        colors.put("midnightblue", new Color(25,25,112));
        colors.put("mediumslateblue", new Color(123,104,238));
        colors.put("slateblue", new Color(106,90,205));
        colors.put("darkslateblue", new Color(72,61,139));
        //shades of purple
        colors.put("lavender", new Color(230,230,250));
        colors.put("thistle", new Color(216,191,216));
        colors.put("plum", new Color(221,160,221));
        colors.put("violet", new Color(238,130,238));
        colors.put("orchid", new Color(218,112,214));
        colors.put("fuchsia", new Color(255,0,255));
        colors.put("magenta", new Color(255,0,255));
        colors.put("mediumorchid", new Color(186,85,211));
        colors.put("mediumpurple", new Color(147,112,219));
        colors.put("blueviolet", new Color(138,43,226));
        colors.put("darkviolet", new Color(148,0,211));
        colors.put("darkorchid", new Color(153,50,204));
        colors.put("darkmagenta", new Color(139,0,139));
        colors.put("purple", new Color(128,0,128));
        colors.put("indigo", new Color(75,0,130));
        //shades of pink
        colors.put("pink", new Color(255,192,203));
        colors.put("lightpink", new Color(255,182,193));
        colors.put("hotpink", new Color(255,105,180));
        colors.put("deeppink", new Color(255,20,147));
        colors.put("palevioletred", new Color(219,112,147));
        colors.put("mediumvioletred", new Color(199,21,133));
        //shades of white
        colors.put("white", new Color(255,255,255));
        colors.put("snow", new Color(255,250,250));
        colors.put("honeydew", new Color(240,255,240));
        colors.put("mintcream", new Color(245,255,250));
        colors.put("azure", new Color(240,255,255));
        colors.put("aliceblue", new Color(240,248,255));
        colors.put("ghostwhite", new Color(248,248,255));
        colors.put("whitesmoke", new Color(245,245,245));
        colors.put("seashell", new Color(255,245,238));
        colors.put("beige", new Color(245,245,220));
        colors.put("oldlace", new Color(253,245,230));
        colors.put("floralwhite", new Color(255,250,240));
        colors.put("ivory", new Color(255,255,240));
        colors.put("antiquewhite", new Color(250,235,215));
        colors.put("linen", new Color(250,240,230));
        colors.put("lavenderblush", new Color(255,240,245));
        colors.put("mistyrose", new Color(255,228,225));
        //shades of gray
        colors.put("gainsboro", new Color(220,220,220));
        colors.put("lightgray", new Color(211,211,211));
        colors.put("lightgrey", new Color(211,211,211));
        colors.put("silver", new Color(192,192,192));
        colors.put("darkgray", new Color(169,169,169));
        colors.put("darkgrey", new Color(169,169,169));
        colors.put("gray", new Color(128,128,128));
        colors.put("grey", new Color(128,128,128));
        colors.put("dimgray", new Color(105,105,105));
        colors.put("dimgrey", new Color(105,105,105));
        colors.put("lightslategray", new Color(119,136,153));
        colors.put("lightslategrey", new Color(119,136,153));
        colors.put("slategray", new Color(112,128,144));
        colors.put("slategrey", new Color(112,128,144));
        colors.put("darkslategray", new Color(47,79,79));
        colors.put("darkslategrey", new Color(47,79,79));
        colors.put("black", new Color(0,0,0));
        //shades of brown
        colors.put("cornsilk", new Color(255,248,220));
        colors.put("blanchedalmond", new Color(255,235,205));
        colors.put("bisque", new Color(255,228,196));
        colors.put("navajowhite", new Color(255,222,173));
        colors.put("wheat", new Color(245,222,179));
        colors.put("burlywood", new Color(222,184,135));
        colors.put("tan", new Color(210,180,140));
        colors.put("rosybrown", new Color(188,143,143));
        colors.put("sandybrown", new Color(244,164,96));
        colors.put("goldenrod", new Color(218,165,32));
        colors.put("peru", new Color(205,133,63));
        colors.put("chocolate", new Color(210,105,30));
        colors.put("saddlebrown", new Color(139,69,19));
        colors.put("sienna", new Color(160,82,45));
        colors.put("brown", new Color(165,42,42));
        colors.put("maroon", new Color(128,0,0));
    }

    public static Color getColor(String colorName) {
        return colors.get(colorName);
    }

    public static String getColorName(Color color) {
        return colors.keySet().stream().filter(key -> color.equals(colors.get(key))).findFirst().get();
    }

}

// This is mutant program.
// Author : ysma

package xcom.thoughtworks.xstream.io.xml;


import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;


public class XmlFriendlyNameCoder implements java.lang.Cloneable
{

    private final java.lang.String dollarReplacement;

    private final java.lang.String escapeCharReplacement;

    private transient java.util.Map escapeCache;

    private transient java.util.Map unescapeCache;

    public XmlFriendlyNameCoder()
    {
        this( "_-", "__" );
    }

    public XmlFriendlyNameCoder( java.lang.String dollarReplacement, java.lang.String escapeCharReplacement )
    {
        this.dollarReplacement = dollarReplacement;
        this.escapeCharReplacement = escapeCharReplacement;
        readResolve();
    }

    public java.lang.String decodeAttribute( java.lang.String attributeName )
    {
        return decodeName( attributeName );
    }

    public java.lang.String decodeNode( java.lang.String elementName )
    {
        return decodeName( elementName );
    }

    public java.lang.String encodeAttribute( java.lang.String name )
    {
        return encodeName( name );
    }

    public java.lang.String encodeNode( java.lang.String name )
    {
        return encodeName( name );
    }

    private java.lang.String encodeName( java.lang.String name )
    {
        final java.lang.ref.WeakReference ref = (java.lang.ref.WeakReference) escapeCache.get( name );
        java.lang.String s = (java.lang.String) (ref == null ? null : ref.get());
        if (s == null) {
            final int length = name.length();
            int i = 0;
            for (; i < length; i++) {
                char c = name.charAt( i );
                if (c == '$' || c == '_') {
                    break;
                }
            }
            if (i == length) {
                return name;
            }
            final java.lang.StringBuffer result = new java.lang.StringBuffer( length + 8 );
            if (i > 0) {
                result.append( name.substring( 0, i ) );
            }
            for (; i < length; i++) {
                char c = name.charAt( i );
                if (c == '$') {
                    result.append( dollarReplacement );
                } else {
                    if (c == '_') {
                        result.append( escapeCharReplacement );
                    } else {
                        result.append( c );
                    }
                }
            }
            s = result.toString();
            escapeCache.put( name, new java.lang.ref.WeakReference( s ) );
        }
        return s;
    }

    private java.lang.String decodeName( java.lang.String name )
    {
        final java.lang.ref.WeakReference ref = (java.lang.ref.WeakReference) unescapeCache.get( name );
        java.lang.String s = (java.lang.String) (ref == null ? null : ref.get());
        if (s == null) {
            final char dollarReplacementFirstChar = dollarReplacement.charAt( 0 );
            final char escapeReplacementFirstChar = escapeCharReplacement.charAt( 0 );
            final int length = name.length();
            int i = 0;
            for (; i < length; i++) {
                char c = name.charAt( i );
                if (c == dollarReplacementFirstChar || c == escapeReplacementFirstChar) {
                    break;
                }
            }
            if (i == length) {
                return name;
            }
            final java.lang.StringBuffer result = new java.lang.StringBuffer( length + 8 );
            if (i > 0) {
                result.append( name.substring( 0, i ) );
            }
            for (; i < length; i++) {
                char c = name.charAt( i );
                if (c == dollarReplacementFirstChar && name.startsWith( dollarReplacement, i )) {
                    i += dollarReplacement.length() - 1;
                    result.append( '$' );
                } else {
                    if (++c == escapeReplacementFirstChar && name.startsWith( escapeCharReplacement, i )) {
                        i += escapeCharReplacement.length() - 1;
                        result.append( '_' );
                    } else {
                        result.append( c );
                    }
                }
            }
            s = result.toString();
            unescapeCache.put( name, new java.lang.ref.WeakReference( s ) );
        }
        return s;
    }

    public java.lang.Object clone()
    {
        try {
            xcom.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder coder = (xcom.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder) super.clone();
            coder.readResolve();
            return coder;
        } catch ( java.lang.CloneNotSupportedException e ) {
            return null;
        }
    }

    private java.lang.Object readResolve()
    {
        escapeCache = new java.util.WeakHashMap();
        unescapeCache = new java.util.WeakHashMap();
        return this;
    }

}

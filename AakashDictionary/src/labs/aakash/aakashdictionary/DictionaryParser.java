package labs.aakash.aakashdictionary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class DictionaryParser {

	private static final String ns = null;
	 
	public static class Definitions {
        
		 public final String dictionary;
		 public final String wordDefinition;

		 private Definitions(String dictionary, String wordDefinition) {
			 this.dictionary = dictionary;
			 this.wordDefinition = wordDefinition;
		 }
	 }

	public List<Definitions> parse(InputStream in) throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            //Log.i("PARSER","PARSE");
            return readFeed(parser);
		}finally {
            in.close();
        }
	}

	private List<Definitions> readFeed(XmlPullParser parser)throws XmlPullParserException, IOException{
		// TODO Auto-generated method stub
		List<Definitions> entries = null;

        parser.require(XmlPullParser.START_TAG, ns, "WordDefinition");
        while (parser.next() != XmlPullParser.END_TAG) {
        	//Log.i("PARSER","READ FEED LOOP");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the Definition tag
            if (name.equals("Definitions")) {
                entries=readDefinitions(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
	}

	private List<Definitions> readDefinitions(XmlPullParser parser) throws XmlPullParserException, IOException{
		// TODO Auto-generated method stub
		List<Definitions> entries = new ArrayList<Definitions>();
		parser.require(XmlPullParser.START_TAG, ns, "Definitions");
        while (parser.next() != XmlPullParser.END_TAG) {
        	//Log.i("PARSER","READ DEFINITIONS LOOP");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Definition")) {
                entries.add(readEntry(parser));
            }else {
            	skip(parser);
            }
        }
		return entries;
	}

	private Definitions readEntry(XmlPullParser parser) throws XmlPullParserException, IOException{
		// TODO Auto-generated method stub
		parser.require(XmlPullParser.START_TAG, ns, "Definition");
        String dictionary = null;
        String wordDefinition = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
        	//Log.i("PARSER","READ ENTRY LOOP");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("WordDefinition")) {
                wordDefinition = readDefiniton(parser);
            }else if(name.equals("Dictionary")){
            	dictionary=getDictionaryName(parser);
            }else{
                skip(parser);
            }
        }
        return new Definitions(dictionary,wordDefinition);
	}

	private String getDictionaryName(XmlPullParser parser) throws XmlPullParserException, IOException{
		// TODO Auto-generated method stub
		parser.require(XmlPullParser.START_TAG, ns, "Dictionary");
        String dictionary = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
        	//Log.i("PARSER","READ ENTRY LOOP");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("Name")){
            	dictionary=getDictionaryNameTag(parser);
            }else{
                skip(parser);
            }
        }
		return dictionary;
	}

	private String getDictionaryNameTag(XmlPullParser parser) throws IOException, XmlPullParserException{
		// TODO Auto-generated method stub
		String result = "";
		parser.require(XmlPullParser.START_TAG, ns, "Name");
		if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
		Log.d("Name","Dictionary= "+result);
		parser.require(XmlPullParser.END_TAG, ns, "Name");
        return result;
	}

	private String readDefiniton(XmlPullParser parser) throws IOException, XmlPullParserException{
		// TODO Auto-generated method stub
		parser.require(XmlPullParser.START_TAG, ns, "WordDefinition");
        String wordDefinition = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "WordDefinition");
        //Log.i("PARSER",wordDefinition);
        return wordDefinition;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
	
	private void skip(XmlPullParser parser) throws IOException, XmlPullParserException{
		// TODO Auto-generated method stub
		if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
	}
}

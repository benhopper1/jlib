package hopper.library.communication.v003;

import com.example.hopperlibrary.console;

public class JsonQuickParser {
	private JsonQuickParser(){}
	
	
	static public String quickParseFirstField(String fieldName, String sourceString){
		String result = null;
		
		int indexOfFieldName = sourceString.indexOf(fieldName);
		//console.log("indexOf:(fieldName)" + indexOfFieldName);
		if(indexOfFieldName == -1){return null;}
		int indexOfNextCollon = sourceString.indexOf(":", indexOfFieldName);
		if(indexOfNextCollon == -1){return null;}
		//console.log("indexOf:(:)" + indexOfNextCollon);
		//getContents of quote:
		
		int indexOfFirstQuote = sourceString.indexOf("\"", indexOfNextCollon);
		//console.log("indexOf:(indexOfFirstQuote)" + indexOfFirstQuote);
		int indexOfSecondQuote = sourceString.indexOf("\"", indexOfFirstQuote + 1);
		//console.log("indexOf:(indexOfSecondQuote)" + indexOfSecondQuote);	
		
		result = sourceString.substring(indexOfFirstQuote + 1, indexOfSecondQuote);
		//console.log("(result):" + result);		
		return result;
	}
	
	static public String quickParseFieldForLayer(String layer, String fieldName, String sourceString){
		String result = null;
		
		int indexOfLayer = sourceString.indexOf(layer + "\":{");
		//console.log("indexOf:(indexOfLayer)" + indexOfLayer);
		if(indexOfLayer == -1){return null;}
		
		int indexOfFieldName = sourceString.indexOf(fieldName, indexOfLayer);
		//console.log("indexOf:(fieldName)" + indexOfFieldName);
		if(indexOfFieldName == -1){return null;}
		int indexOfNextCollon = sourceString.indexOf(":", indexOfFieldName);
		if(indexOfNextCollon == -1){return null;}
		//console.log("indexOf:(:)" + indexOfNextCollon);
		//getContents of quote:
		
		int indexOfFirstQuote = sourceString.indexOf("\"", indexOfNextCollon);
		//console.log("indexOf:(indexOfFirstQuote)" + indexOfFirstQuote);
		int indexOfSecondQuote = sourceString.indexOf("\"", indexOfFirstQuote + 1);
		//console.log("indexOf:(indexOfSecondQuote)" + indexOfSecondQuote);	
		
		result = sourceString.substring(indexOfFirstQuote + 1, indexOfSecondQuote);
		//console.log("(result):" + result);		
		return result;
	}
}

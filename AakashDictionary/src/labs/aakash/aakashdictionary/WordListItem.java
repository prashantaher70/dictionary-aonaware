package labs.aakash.aakashdictionary;

public class WordListItem {
	private String word;
	private String meaning;
	
	public String getMeaning()
	{
		return meaning;
	}
	public void setMeaning(String meaning)
	{
		this.meaning=meaning;
	}
	
	public void setWord(String word)
	{
		this.word=word;
	}
	@Override
	public String toString() {
	    return word;
	}
}

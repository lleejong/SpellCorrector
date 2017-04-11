package debug;

public class Word implements Comparable<Word>{
	private String word;
	private String keystroke;
	private int freq;
	
	public Word(String word){
		this.word = word;
		this.freq = 1;
	}
	public Word(String word, int freq){
		this.word = word;
		this.freq = freq;
	}
	
	public String getWord() {
		return word;
	}
	public int getFreq() {
		return freq;
	}
	
	public void increaseFreq(){
		this.freq++;
	}
	public void sum(int freq){
		this.freq += freq;
	}
	
	public String toString(){
		StringBuffer bf = new StringBuffer();
		bf.append(word);
		bf.append("\t");
		bf.append(freq);
		return bf.toString();
	}
	@Override
	public int compareTo(Word other) {
		//descending order
		return this.freq - other.freq;
	}
	
}

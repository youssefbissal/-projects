public class NumberTok extends Token {
	public int num;
	public NumberTok(int tag, int num){super(tag); this.num=num;  }
	public String toString() { return "<" + Tag.NUM + ", " + num + ">"; }
}
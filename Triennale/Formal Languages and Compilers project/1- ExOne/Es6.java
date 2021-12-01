import java.util.ArrayList;

public class Es6
{
	public static boolean scan(String s)
	{
		int state=0;
		int i=0;
		while(state>=0 && i<s.length())
		{
			final char ch=s.charAt(i++);
			switch(state){
				case 0:
					if(ch=='0'){
						state=0;
				  }else if(ch=='1'){
						state=1;
					}else
						state=-1;
					break;

				case 1:
					if(ch=='0'){
						state=2;
					}else if(ch=='1'){
						state=0;
					}else
						state=-1;
					break;

				case 2:
					if(ch=='0'){
						state=1;
					}else if(ch=='1'){
							state=2;
					}else
						state=-1;
					break;
			}
		}
		return state==0;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
		tests.add("110");
		tests.add("1");
		tests.add("0");
		tests.add("0110");
		tests.add("1100100");
		tests.add("01010");
		tests.add("1001");
		tests.add("01001");
		tests.add("01101001");
		tests.add("111");

				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

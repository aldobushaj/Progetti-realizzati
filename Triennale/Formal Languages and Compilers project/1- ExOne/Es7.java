import java.util.ArrayList;

public class Es7
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
				    if(ch=='a')
						state=1;
					else if(ch=='b')
						state=2;
					else
					    state=-1;
					break;

					case 1:
					if(ch=='a' || ch=='b')
						state=1;
					else
						state=-1;
					break;

					case 2:
					if(ch=='a')
						state=1;
					else if(ch=='b')
						state=3;
					else
						state=-1;
					break;

					case 3:
					if(ch=='a')
						state=1;
					else
						state=-1;
					break;
				}

		}
		return state==1;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
		tests.add("bbabbbbbb");
		tests.add("aaaaaaaa");
		tests.add("a");
		tests.add("ba");
		tests.add("aa");
		tests.add("bbabbbbbb");
		tests.add("bab");
		tests.add("b");
		

				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

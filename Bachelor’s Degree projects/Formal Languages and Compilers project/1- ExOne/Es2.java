import java.util.ArrayList;

public class Es2
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
				    if(ch=='_')
						state=1;
					else if(ch>='A' && ch<='z')//indica il carattere
						state=2;
					else
						state=-1;
					break;
				case 1:
					if(ch=='_')
						state=1;
					else if(ch>='0' && ch<='9' || ch>='A' && ch<='z')//numeri e caratteri
						state=2;
					else
						state=-1;
					break;
				case 2:
					if(ch>='0' && ch<='9' || ch>='A' && ch<='z' || ch=='_')
						state=2;
					else
						state=1;

			}

		}
		return state==2;
	}
	public static void main(String[] args)
	{

		ArrayList<String> test=new ArrayList<String>();
		test.add("_prova123");
		test.add("123ciao_");
		test.add("esempio_145");
		test.add("____");
		test.add("1pluto_");
		test.add("prova");

		for(String s : test)
			System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

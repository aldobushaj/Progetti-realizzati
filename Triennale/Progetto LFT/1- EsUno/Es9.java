import java.util.ArrayList;

public class Es9
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
				    if(ch=='a' || ch=='A')
						state=1;
					else if(ch!='A' && ch!='a')
						state=4;
					else
					    state=-1;
					break;

				case 1:
					if(ch=='l' || ch=='L')
						state=2;
					else if(ch!='L' && ch!='l')
						state=5;
					else
						state=-1;
					break;

				case 2:
				    if(ch=='d' || ch=='D')
						state=3;
					else if(ch!='D' && ch!='d')
						state=6;
					else
					    state=-1;
					break;
					
				case 3:
					boolean accettoqualsiasicarattere =true;
				    if(accettoqualsiasicarattere)
						state=7;
					else
					    state=-1;
					break;
					
					
					
				case 4:
				    if(ch=='l' || ch=='L')
						state=5;
					else
					    state=-1;
					break;
					
				case 5:
				    if(ch=='d' || ch=='D')
						state=6;
					else
					    state=-1;
					break;
					
				case 6:
				    if(ch=='o' || ch=='O')
						state=7;
					else
					    state=-1;
					break;
				}

		}
		return state==7;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
		tests.add("aldo");
		tests.add("ald ");
		tests.add("abdo");
		tests.add("ado");
		tests.add("al o");
		tests.add(" ldo");
		tests.add("ciao");
		tests.add("al=o");
		tests.add("mrdo");
		
		

				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

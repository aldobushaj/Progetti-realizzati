import java.util.ArrayList;

public class Es1
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
				    if(ch=='0')
						state=1;
					else if(ch=='1')
						state=0;
					else
						state=-1;
					break;
					
				case 1:
					if(ch=='0')
						state=2;
					else if(ch=='1')
						state=0;
					else
						state=-1;
					break;
					
				case 2:
					if(ch=='1')
						state=0;
					else if(ch=='0')
						state=-1;//stato trappola
					else
						state=-1;
					break;

			}

		}
		return  state==0 || state==1 || state==2;
	}
	public static void main(String[] args)
	{
		ArrayList<String> test=new ArrayList<String>();
		test.add("011110111");
		test.add("");
		test.add("0");
		test.add("1111111");
		test.add("0001");
		test.add("010101");
		test.add("000011");
		test.add("000");


		for(String s : test)
			System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

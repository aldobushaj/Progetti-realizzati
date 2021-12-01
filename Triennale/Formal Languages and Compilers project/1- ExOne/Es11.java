import java.util.ArrayList;

public class Es11
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
					if(ch=='*' || ch=='a'){
						state=0;
					}else if(ch=='/'){
						state=1;
					}else
						state=-1;
					break;

				case 1:
					if(ch=='a'){
						state=0;
					}else if(ch=='/'){
						state=1;
					}else if(ch=='*'){
						state=2;
					
					}else
						state=-1;
					break;

				case 2:
					if(ch=='a' || ch=='/'){
						state=2;
					}else if(ch=='*'){
						state=3;
					}else
						state=-1;
					break;

				case 3:
					if(ch=='*'){
						state=3;
					}else if(ch=='a'){
						state=2;
					}else if(ch=='/'){
						state=4;
					}else
						state=-1;
					break;
					
					
					case 4:
					if(ch=='/'){
						state=1;
					
					}else if(ch=='*' || ch=='a' ){
						state=0;
					}else
						state=-1;
						
					break;
					
					

			}

		}
		return  state==0 || state==1 || state==4;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
			tests.add("****");
			tests.add("/////");
			tests.add("aaa/****/aa");
			tests.add("aa/*a*a*/");
			tests.add("aaaa");
			tests.add("/*aa*/");
			tests.add("*/a");
			tests.add("a/**/***a");
			tests.add("aaa/*/aa");
			tests.add("a/**//***a");
			tests.add("aa/*aa");
			
			
		
				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));



	}
}
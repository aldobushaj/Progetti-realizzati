import java.util.ArrayList;

public class Es10
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
					if(ch=='/'){
						state=1;
					
					}else
						state=-1;
					break;

				case 1:
					if(ch=='*'){
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
					if(ch=='*' || ch=='/' || ch=='a' )
						state=-1;
					break;

			}

		}
		return state==4;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
			tests.add("/****/");
			tests.add("/*a*a*/");
			tests.add("/*a/**/");
			tests.add("/**a///a/a**/");
			tests.add("/**/");
			tests.add("/*/*/");
			tests.add("/*/");
			tests.add("/*/***//");
			tests.add("*///sb/0/fv/a/*/");
			
		
				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));



	}
}

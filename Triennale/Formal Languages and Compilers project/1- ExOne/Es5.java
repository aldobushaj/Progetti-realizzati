import java.util.ArrayList;

public class Es5
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
						if(ch>='A' && ch<='K')
							state=1;
						else if(ch>='L' && ch<='Z')
							state=2;
					else
						state=-1; //stato di morte o stato trappola
					break;
				case 1:
				if(ch>='a' && ch<='z')
					state=1;
				else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 3;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 4;
				}else
					state=-1;
				break;

				case 2:
				if(ch>='a' && ch<='z')
					state=2;
				else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 5;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 6;
				}else
					state=-1;
				break;

				case 3:
				if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 3;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 4;
				}else
					state=-1;
				break;

				case 4:
				if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 3;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 4;
				}else
					state=-1;
				break;

				case 5:
				if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 5;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 6;
				}else
					state=-1;
				break;

				case 6:
				if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 5;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 6;
				}else
					state=-1;
				break;
			}

		}
		return state==3 || state==6;
	}
	public static void main(String[] args)
	{
		ArrayList<String> tests=new ArrayList<String>();
			tests.add("Bianchi123456");
			tests.add("Rossi654321");
			tests.add("Bianchi654321");
			tests.add("Rossi123456");
			tests.add("Bianchi2");
			tests.add("B122");
			tests.add("654322");
			tests.add("Rossi");
			tests.add("RR231");
			tests.add("Ra232");
			tests.add("1234Bianchi");
			tests.add("123Rossi");
			tests.add("Antonio2");
			tests.add("2");

				for(String s : tests)
					System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));



	}
}

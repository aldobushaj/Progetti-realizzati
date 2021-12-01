import java.util.ArrayList;
public class Es4
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
					if (ch==' ')
					{
						state=0;
					}else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
						state = 1;
					}
					else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
						state = 2;
					}
					else
						state=-1; //stato di trappola
					break;

				case 1:
				if (ch==' ')
				{
					state = 3;
				}else if(ch>='A' && ch<='K'){
							state=5;
				}else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 1;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 2;
				}else
						state=-1;
					break;

				case 2:
				if (ch==' ')
				{
					state = 4;
				}else if(ch>='L' && ch<='Z'){
							state=5;
				}else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
					state = 1;
				}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
					state = 2;
				}else
						state=-1;
					break;

				case 3:
				if (ch==' ')
				{
					state=3;
				}else if(ch>='A' && ch<='K'){
					state=5;
				}else
						state=-1;
					break;

				case 4:
				if (ch==' ')
				{
					state=4;
				}
				else if(ch>='L' && ch<='Z')
				{
					state=5;
				}else
						state=-1;
					break;

				case 5:
				if (ch==' ')
				{
					state = 6;
				}else if(ch>='a' && ch<='z'){
					state=5;
				}else
						state=-1;
					break;

				case 6:
				if (ch==' ')
				{
					state = 6;
				}else if(ch>='A' && ch<='Z'){
					state=5;
				}else
						state=-1;
					break;
			}

		}
		return state==5 || state==6;
	}
	public static void main(String[] args)
	{
		ArrayList<String> test=new ArrayList<String>();
			test.add("123456Bianchi");
			test.add("654321Rossi");
			test.add("654321Bianchi");
			test.add("123456Rossi");
			test.add("2Bushaj");
			test.add("122E");
			test.add("65475322");
			test.add("Rossi");
			test.add("  123456 Bianchi ");
			test.add("1234 56Bianchi");
			test.add("123456Bia nchi");
			test.add("123456De Gasperi");
			test.add("  123456   De Gaspoeri  ");
			test.add("De Gasperi 12345");
			test.add("2 CC");
			test.add("2 Car Ag");
			test.add("2    Zar   ");
		

		for(String s : test)
			System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));
	}
}

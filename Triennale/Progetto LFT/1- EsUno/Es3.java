import java.util.ArrayList;
class Es3{
	public static boolean scan(String s)
	{
		int state=0;
		int i = 0;
		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);
			switch (state) {
				case 0:
					if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
						state = 1;
					}else if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
						state = 2;
					}
					else
						state=-1;
					break;
				case 1:
					if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
						state = 2;
					}else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
						state = 1;
					}else if (ch >= 'A' && ch <= 'K'){
						state = 3;
					}
					else
						state=-1;
					break;
				case 2:
					if (ch=='1' || ch=='3' ||ch=='5' ||ch=='7' ||ch=='9' ){
						state = 2;
					} else if (ch=='0' || ch=='2' ||ch=='4' ||ch=='6' ||ch=='8' ){
						state = 1;
					}
					else if(ch >= 'L' && ch <= 'Z'){
						state = 3;
					}else
						state=-1;
					break;
		}

	}
	return state==3;
	}
	public static void main(String[] args)
	{
		ArrayList<String> test=new ArrayList<String>();
			test.add("123456Bianchi");
			test.add("654321Rossi");
			test.add("654321Bianchi");
			test.add("123456Rossi");
			test.add("2Bushaj");
			test.add("122D");
			test.add("6845321");
			test.add("Rossi");
			test.add("123Liviero");
			test.add("832972Agostini");


		for(String s : test)
			System.out.println("Test su " +s+ ": " + (scan(s) ? "Accettato" : "Non accettato"));

	}
}

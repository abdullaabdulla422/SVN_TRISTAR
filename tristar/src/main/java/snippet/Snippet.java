package snippet;

@SuppressWarnings("ALL")
public class Snippet {
	private static int greatestCommonFactor(int a, int b) {
		    while( b > 0 ) {
		        int temp = b;
		        b = a % b;
		        a = temp;
		    }
		    return a;
		}
}


package compilateur;

import java.util.Arrays;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			// TESTS POUR LE LEXER
			Lexer lexer = new Lexer("./files/generale.txt");
			
			// TESTS POUR LE PARSER
			Parser parser = new Parser(lexer);
			
			// TODO: 
			// Parser :
			// 			finir instruction()
			//          savoi si on crée un TOK_BLOCK pour if, else, while, for : attente réponse du prof
			
			

		} catch (Exception e) {
			System.out.println("Erreur : "+ e.getMessage() + " stacktrace : " + Arrays.toString(e.getStackTrace()));
		}

	}

}

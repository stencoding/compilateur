/**
 * 
 * Classe (FRONTEND).
 * Elle compose le Token. Elle permet de l'identifier.
 *
 * @author Mathilde PREVOST & Steve NEGRINE
 */
public enum Classe {
	// Opérateurs
	TOK_ADD,
	TOK_SUB,
	TOK_MUL,
	TOK_DIV,
	TOK_MODULO,
	TOK_PAR_OUVR,
	TOK_PAR_FERM,
	TOK_ACC_OUVR,
	TOK_ACC_FERM,
	TOK_COMPARE,
	TOK_DIFF,
	TOK_INF,
	TOK_SUP,
	TOK_INF_EGAL,
	TOK_SUP_EGAL,
	TOK_EGAL,
	TOK_DEUX_POINTS,
	TOK_POINT_VIRGULE,
	TOK_VIRGULE,
	// Mots-clés
	TOK_IF,
	TOK_ELSE,
	TOK_FOR,
	TOK_WHILE,
	TOK_VAR,
	TOK_BLOCK,
	TOK_SEQ,
	TOK_LOOP,
	TOK_BREAK,
	TOK_RETURN,
	TOK_ECHO,
	// Types de variable
	TOK_INT,
	TOK_STR,
	// Stockage constante
	TOK_CST_INT,
	TOK_CST_STR,
	TOK_IDENT,
	TOK_EOF;
}

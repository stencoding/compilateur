package compilateur;

public enum Categorie {
	// Opérateurs
	ADD,
	SUB,
	MUL,
	DIV,
	MODULO,
	COMPARE,
	DIFF,
	INF,
	SUP,
	INF_EGAL,
	SUP_EGAL,
	EGAL,
	// Mots-clés
	IF,
	ELSE,
	FOR,
	WHILE,
	VAR,
	BLOCK,
	SEQ,
	LOOP,
	BREAK,
	RACINE,
	CALL,
	FUNCTION,
	ARGS,
	// Type de variable
	INT,
	STR,
	// stocke une constante
	CST_INT,
	CST_STR,
	IDENT,
}

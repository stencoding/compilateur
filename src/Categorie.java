/**
 * 
 * Catégorie (BACKEND/FRONTEND)
 * Elle est utilisée par le Noeud.
 * Elle sert notamment lors de la génération du code afin d'identifier chaque Noeud.
 * 
 * @author Mathilde PREVOST & Steve NEGRINE
 */

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
	ECHO,
	// Types de variables
	INT,
	STR,
	// Stockage constante
	CST_INT,
	CST_STR,
	IDENT,
}
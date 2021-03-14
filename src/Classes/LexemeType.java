package Classes;

/**
 * Перечисление типов лексем:
 * левая скобочка, правая скобочка, знак плюс, знак минус,
 * знак умножения, знак деления, число, функции, запятая, признак конца строки
 */
public enum LexemeType {
    LEFT_BRACKET, RIGHT_BRACKET,
    OP_PLUS, OP_MINUS, OP_MUL, OP_DIV,
    NUMBER, FUNCTION, COMMA,
    EOF
}

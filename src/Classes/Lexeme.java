package Classes;

/**
 * Класс лексем
 */
public class Lexeme {
    /** Поле - тип лексем*/
    LexemeType type;
    /** Значение лексемы*/
    String value;

    /** Конструктор, принимающий в качестве значения лексемы строку*/
    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    /** Конструтор, принимающий в качестве значения лексемы символ*/
    public Lexeme(LexemeType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }
}

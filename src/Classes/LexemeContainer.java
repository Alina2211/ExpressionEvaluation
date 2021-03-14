package Classes;
import Classes.Lexeme;

import java.util.List;
/**
 * Класс для работы с полученным массивом лексем
 */
public class LexemeContainer {
    /** Поле, хранящее текущую позицию в массиве лексем*/
    private int pos;
    /** Полученный массив лексем*/
    public List<Lexeme> lexemes;

    /**
     * Конструктор, принимащий список лексем
     * @param lexemes - список лексем исходного выражения
     */
    public LexemeContainer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    /**
     * Метод получения текущего элемента и передвижение позиции на следующий
     */
    public Lexeme next() {
        return lexemes.get(pos++);
    }

    /**
     * Метод передвигающий указатель на предыдущий элемент
     */
    public void back() {
        pos--;
    }

    /**
     * Метод полуения текущей позиции
     * @return позиция текущего элемента
     */
    public int getPos() {
        return pos;
    }
}

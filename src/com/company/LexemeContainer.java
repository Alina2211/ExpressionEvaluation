package com.company;
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
     *
     */
    public Lexeme next() {
        return lexemes.get(pos++);
    }

    public void back() {
        pos--;
    }

    public int getPos() {
        return pos;
    }
}

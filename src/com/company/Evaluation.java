package com.company;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, содержащий функционал для разбора строчного выражения на лексемы и последующего вычисления
 * @author Губина Алина
 */
public class Evaluation{
    /** Поле, хранящее исходную строку с арифметическим выражением*/
    private String expression;
    /** Текущая позиция при разборе арифметического выражения на лексемы*/
    private int pos;
    /** Хеш-таблица с функциями, которые могут быть вычислены*/
    public HashMap<String, Function> functionMap;

    /**
     * Конструктор с входным параметром
     * @param expr - исходная строка
     */
    public Evaluation (String expr)
    {
        expression = expr;
        pos = 0;
    }

    /**
     * Метод для разбора исходной строки на лексемы
     * @return возвращает список полученных лексем (если ни на каком этапе не возникло исключение)
     */
    public List<Lexeme> analysis() {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        while (pos< expression.length()) {
            char c = expression.charAt(pos);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, c));
                    pos++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, c));
                    pos++;
                    continue;
                case ',':
                    lexemes.add(new Lexeme(LexemeType.COMMA, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0') {
                        String num = numberFromString();
                        lexemes.add(new Lexeme(LexemeType.NUMBER, num));
                    } else {
                        if (c != ' ') {
                            if (c >= 'a' && c <= 'z') {
                                String ch = nameFromString();
                                if (functionMap.containsKey(ch)) {
                                    lexemes.add(new Lexeme(LexemeType.FUNCTION, ch));
                                } else {
                                    Scanner in = new Scanner(System.in);
                                    System.out.print(ch + " = ");
                                    String num = in.nextLine();
                                    lexemes.add(new Lexeme(LexemeType.NUMBER, num));
                                }
                            }
                        } else {
                            pos++;
                        }
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    /**
     * Вспомогательный метод для чтения чисел из строки
     * @return считанное число в виде строки
     */
    public String numberFromString()
    {
        StringBuilder sb = new StringBuilder();
        char c = expression.charAt(pos);
        do {
            sb.append(c);
            pos++;
            if (pos >= expression.length()) {
                break;
            }
            c = expression.charAt(pos);
        } while (c <= '9' && c >= '0');
        return sb.toString();
    }

    /**
     * Вспомогательный метод для чтения названий функций и переменных
     * @return строку, хранящую имя функции или переменной
     */
    public String nameFromString()
    {
        StringBuilder sb = new StringBuilder();
        char c = expression.charAt(pos);
        do {
            sb.append(c);
            pos++;
            if (pos >= expression.length()) {
                break;
            }
            c = expression.charAt(pos);
        } while (c >= 'a' && c <= 'z');
        return sb.toString();
    }

    /**
     * Метод для формирования хеш-таблицы с вычисляемыми функциями
     */
    public static HashMap<String, Function> getFunctionMap() {
        HashMap<String, Function> functionTable = new HashMap<>();

        functionTable.put("pow", args -> {
            if (args.size() != 2) {
                throw new RuntimeException("Wrong argument count for function pow: " + args.size());
            }
            return (int) Math.pow(args.get(0), args.get(1));
        });

        return functionTable;
    }
}

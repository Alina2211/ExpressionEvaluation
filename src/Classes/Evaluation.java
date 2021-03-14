package Classes;

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

    /** Поле для хранения лексем полученной строки*/
    public LexemeContainer myLexemes;

    /**
     * Конструктор с входным параметром
     * @param expr - исходная строка
     */
    public Evaluation (String expr)
    {
        expression = expr;
        pos = 0;
        functionMap = getFunctionMap();

    }

    /**
     * Метод, запускающий анализ выражения и его вычисления
     * @return вычисленное значение выражения (если выражение корректное)
     */
    public int evaluate (){
        List<Lexeme> lexemes = analysis();
        myLexemes = new LexemeContainer(lexemes);
        int result = evaluatedExpression();
        return result;
    }

    /**
     * Метод, получающий значение последнего действия в выражении
     * @return вызывает метод для сложения и вычитания
     */
    public int evaluatedExpression() {
        Lexeme lexeme = myLexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            myLexemes.back();
            return plusminus();
        }
    }

    /**
     * Метод сложения и вычитания готовых слагаемых. В случае когда слагаемое необходимо вычислить,
     * вызывается метод для вычисления произведения и частного, т.к. в качестве слагаемых могут
     * выступать либо числа, либо целые множители
     */
    public int plusminus() {
        int value = multdiv();
        while (true) {
            Lexeme lexeme = myLexemes.next();
            switch (lexeme.type) {
                case OP_PLUS:
                    value += multdiv();
                    break;
                case OP_MINUS:
                    value -= multdiv();
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    myLexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + myLexemes.getPos());
            }
        }
    }

    /**
     * Метод до вычисления произведения и частного
     */
    public int multdiv() {
        int value = multiplier();
        while (true) {
            Lexeme lexeme = myLexemes.next();
            switch (lexeme.type) {
                case OP_MUL:
                    value *= multiplier();
                    break;
                case OP_DIV:
                    value /= multiplier();
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                case OP_PLUS:
                case OP_MINUS:
                    myLexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + myLexemes.getPos());
            }
        }
    }

    /**
     *Метод получения множителя, адаптирован под работу с отрицательными часлами (т.е. применяется
     * оператор "унарный минус"
     */
    public int multiplier() {
        Lexeme lexeme = myLexemes.next();
        switch (lexeme.type) {
            case FUNCTION:
                myLexemes.back();
                return func();
            case OP_MINUS:
                int value = multiplier();
                return -value;
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case LEFT_BRACKET:
                value = plusminus();
                lexeme = myLexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + myLexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + myLexemes.getPos());
        }
    }

    /**
     * Метод считывания аргументов функции и последующего вычисления ее значения
     * (если функция допустимая)
     */
    public int func() {
        String name = myLexemes.next().value;
        Lexeme lexeme = myLexemes.next();

        if (lexeme.type != LexemeType.LEFT_BRACKET) {
            throw new RuntimeException("Wrong function call syntax at " + lexeme.value);
        }

        ArrayList<Integer> args = new ArrayList<>();

        lexeme = myLexemes.next();
        if (lexeme.type != LexemeType.RIGHT_BRACKET) {
            myLexemes.back();
            do {
                args.add(evaluatedExpression());
                lexeme = myLexemes.next();

                if (lexeme.type != LexemeType.COMMA && lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Wrong function call syntax at " + lexeme.value);
                }

            } while (lexeme.type == LexemeType.COMMA);
        }
        return functionMap.get(name).apply(args);
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

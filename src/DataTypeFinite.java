import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

// Класс для определения типа данных, содержащегося в строке
public class DataTypeFinite {

    // Все возможные состояния
    private enum AutomatonState {
        START,       // Начальное состояние

        SIGN,        // Состояние для знаков (+, -)

        INTEGER,     // Состояние для целых чисел
        FLOAT,       // Состояние для вещественных чисел
        TEXT,        // Состояние для текста

        EXPONENT_MINUS, // Состояние для экспоненциальной записи
        EXPONENT_PLUS,
        EXPONENT_EXPECT_SIGN, // Состояние для неполной экспоненциальной записи (ожидается символ)
        EXPONENT_PLUS_EXPECT_DIGIT,
        EXPONENT_MINUS_EXPECT_DIGIT,// Состояние для неполной экспоненциальной записи (ожидается цифра)
    }
    // Все типы данных, которые определяет класс
    public enum StringType {
        TEXT,
        INTEGER,
        FLOAT,
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }
    private boolean isExpSymbol(char c) {
        return c == 'E' || c == 'e';
    }
    private boolean isSignPlus(char c) {
        return c == '+';
    }
    private boolean isSignMinus(char c) {
        return c == '-';
    }
    private boolean isSign(char c) {
        return isSignPlus(c) || isSignMinus(c);
    }
    private boolean isDot(char c) {
        return c == '.';
    }

    private AutomatonState START_StateUpdate(char currentChar) {
        if(isSign(currentChar))
            return AutomatonState.SIGN;

        if (isDigit(currentChar))
            return AutomatonState.INTEGER;

        return AutomatonState.TEXT;
    }
    private AutomatonState SING_StateUpdate(char currentChar) {
        if (isDigit(currentChar))
            return AutomatonState.INTEGER;

        return AutomatonState.TEXT;
    }
    private AutomatonState INTEGER_StateUpdate(char currentChar) {
        if (isDigit(currentChar))
            return AutomatonState.INTEGER;

        if (isDot(currentChar))
            return AutomatonState.FLOAT;

        return AutomatonState.TEXT;
    }
    private AutomatonState FLOAT_StateUpdate(char currentChar) {
        if (isDigit(currentChar))
            return AutomatonState.FLOAT;

        if (isExpSymbol(currentChar))
            return AutomatonState.EXPONENT_EXPECT_SIGN;

        return AutomatonState.TEXT;
    }
    private AutomatonState EXPONENT_StateUpdate(char currentChar, AutomatonState state) {

        if (state == AutomatonState.EXPONENT_EXPECT_SIGN && isSignMinus(currentChar))
            return AutomatonState.EXPONENT_MINUS_EXPECT_DIGIT;

        if (state == AutomatonState.EXPONENT_EXPECT_SIGN && isSignPlus(currentChar))
            return AutomatonState.EXPONENT_PLUS_EXPECT_DIGIT;

        if (state == AutomatonState.EXPONENT_PLUS_EXPECT_DIGIT && isDigit(currentChar))
            return AutomatonState.EXPONENT_PLUS;

        if (state == AutomatonState.EXPONENT_MINUS_EXPECT_DIGIT && isDigit(currentChar))
            return AutomatonState.EXPONENT_MINUS;

        if (state == AutomatonState.EXPONENT_PLUS_EXPECT_DIGIT && isDigit(currentChar))
            return AutomatonState.EXPONENT_PLUS;

        if((state == AutomatonState.EXPONENT_PLUS || state == AutomatonState.EXPONENT_MINUS) && isDigit(currentChar))
            return state;

        return AutomatonState.TEXT;
    }
    private AutomatonState TEXT_StateUpdate(char currentChar) {
        return AutomatonState.TEXT;
    }

    // Шаг по изменению состояния в зависимости от текущего состояния и седующего символа в строке
    private AutomatonState StateUpdate(AutomatonState currentState, char currentChar) {
        switch (currentState) {
            case START:
                return START_StateUpdate(currentChar);

            case SIGN:
                return SING_StateUpdate(currentChar);
            case INTEGER:
                return INTEGER_StateUpdate(currentChar);
            case FLOAT:
                return FLOAT_StateUpdate(currentChar);
            case EXPONENT_PLUS:
            case EXPONENT_MINUS:
            case EXPONENT_MINUS_EXPECT_DIGIT:
            case EXPONENT_PLUS_EXPECT_DIGIT:
            case EXPONENT_EXPECT_SIGN:
                return EXPONENT_StateUpdate(currentChar, currentState);

            default:
                return TEXT_StateUpdate(currentChar);
        }

    }

    public DataTypeFinite() {

    }

    // Определение типа данных
    public StringType identifyDataType(String input) {
        // установка начального состояния
        AutomatonState state = AutomatonState.START;

        // посимвольное чтение строки и измение состояния
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            state = StateUpdate(state, currentChar);
        }

        // определяем тип данных по конечному состоянию
        switch (state)
        {
            case AutomatonState.INTEGER:
            case AutomatonState.EXPONENT_PLUS:
                return StringType.INTEGER;

            case AutomatonState.EXPONENT_MINUS:
            case AutomatonState.FLOAT:
                return StringType.FLOAT;
            default:
                return StringType.TEXT;
        }
    }
}

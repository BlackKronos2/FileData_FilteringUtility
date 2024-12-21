public class StringToNumberConverter {
    public static final int ExponentMax = 300;
    public static final int MaxDoublePointsCount = 15; // Максимальное количество разрядов

    // Метод для преобразования строки в число с плавающей точкой
    public static double stringToFloat(String str) throws Exception {
        double result = 0.0;
        boolean isNegative = false;
        int i = 0;

        // Проверка на знак
        if (str.charAt(0) == '-') {
            isNegative = true;
            i = 1;
        }

        int numberPartPointsCount = 0;

        // Обработка целой части
        while (i < str.length() && str.charAt(i) != '.') {
            char ch = str.charAt(i);

            if(ch == 'E' || ch == 'e')
                return stringToExponential(str);

            result = result * 10 + (ch - '0');
            i++;
            numberPartPointsCount++;
        }

        if(numberPartPointsCount > MaxDoublePointsCount)
            throw new Exception(str + "\nЧисло разрядов в целой части слишком велико (Максимальный порог " + MaxDoublePointsCount + ")");

        // Обработка дробной части
        if (i < str.length()) {
            double decimalPlace = 0.1;
            while (i < str.length()) {
                char ch = str.charAt(i);

                if(ch == 'E' || ch == 'e')
                    return stringToExponential(str);

                result += (ch - '0') * decimalPlace;
                decimalPlace *= 0.1;
                i++;
            }
        }

        if(i - numberPartPointsCount> MaxDoublePointsCount)
            throw new Exception(str + "\nЧисло разрядов в дробной части слишком велико (Максимальный порог " + MaxDoublePointsCount + ")");

        return isNegative ? -result : result;
    }

    // Метод для преобразования строки в экспоненциальное число с типом float
    public static float stringToExponential(String str) throws Exception {
        float result = 0.0f;
        boolean isNegative = false;
        int i = 0;

        // Проверка на знак
        if (str.charAt(0) == '-') {
            isNegative = true;
            i = 1;
        }

        // Обработка целой части
        while (i < str.length() && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
            result = result * 10 + (str.charAt(i) - '0');
            i++;
        }

        if(i > MaxDoublePointsCount)
            throw new Exception(str + "\nЧисло разрядов в целой части слишком велико (Максимальный порог " + MaxDoublePointsCount + ")");

        int fractionalPartPointsCount = 0;

        // Обработка дробной части
        if (i < str.length() && str.charAt(i) == '.') {
            i++; // Пропускаем точку
            double decimalPlace = 0.1;
            while (i < str.length() && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                result += (str.charAt(i) - '0') * decimalPlace;
                decimalPlace *= 0.1;
                i++;
                fractionalPartPointsCount++;
            }
        }

        if(fractionalPartPointsCount > MaxDoublePointsCount)
            throw new Exception(str + "\nЧисло разрядов в дробной части слишком велико (Максимальный порог " + MaxDoublePointsCount + ")");

        // Обработка экспоненциальной части
        if (i < str.length() && (str.charAt(i) == 'e' || str.charAt(i) == 'E')) {
            i++; // Пропускаем символ 'e' или 'E'
            boolean expNegative = false;
            if (str.charAt(i) == '-') {
                expNegative = true;
                i++;
            } else if (str.charAt(i) == '+') {
                i++; // Пропускаем символ '+'
            }

            int exponent = 0;
            while (i < str.length() && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                exponent = exponent * 10 + (str.charAt(i) - '0');
                i++;
            }

            if(fractionalPartPointsCount > exponent)
                throw new Exception("---Экспонециальная запись: количество знаков после запятой не должно превышать степень");

            if(exponent > ExponentMax)
                throw new Exception("---Экспонециальная запись: максимальная степень - " + ExponentMax);

            // Применяем экспоненту
            result = result * (float)Math.pow(10, expNegative ? -exponent : exponent);
        }

        return isNegative ? -result : result;
    }

    // Метод для преобразования строки в целое число из экспоненциальной записи с плюсовым знаком
    public static int stringToIntFromExponential(String str) throws Exception {
        double result = stringToExponential(str);
        return (int) result;
    }
}
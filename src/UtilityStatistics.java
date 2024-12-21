import java.util.List;

// Класс для сбора и вывода статистики утилиты
public class UtilityStatistics {
    // Режим вывода статистики
    private UtilityParameters.StatisticsMode statisticMode;
    // Префик для выходных файлов
    private String filesNameAddition;

    public UtilityStatistics(UtilityParameters.StatisticsMode mode, String filesNameAdd) {
        statisticMode = mode;
        filesNameAddition = filesNameAdd;
    }

    private void MakeBriefStatistics(List<String> textsStrings, List<String> integersStrings , List<String> floatsStrings)
    {
        System.out.println("Файл " + filesNameAddition + FilteringUtility.StringsFileName + " записано " + textsStrings.size() + " элементов");
        System.out.println("Файл " + filesNameAddition + FilteringUtility.IntergersFileName + " записано " + integersStrings.size() + " элементов");
        System.out.println("Файл " + filesNameAddition + FilteringUtility.FloatsFileName + " записано " + floatsStrings.size() + " элементов");
        System.out.println("");
    }

    private void MakeFullStatistics(List<String> textsStrings, List<String> integersStrings , List<String> floatsStrings)
    {
        MakeBriefStatistics(textsStrings, integersStrings, floatsStrings);

        System.out.println("--Целые числа");
        // 1. Для целых чисел находим максимальное и минимальное значение
        if (integersStrings != null && !integersStrings.isEmpty()) {
            double minInt = Double.MAX_VALUE;
            double maxInt = Double.MIN_VALUE;

            double sum = 0;

            try {
                for (String str : integersStrings) {
                    double value = StringToNumberConverter.stringToFloat(str);

                    sum += value;

                    if (value < minInt) {
                        minInt = value;
                    }
                    if (value > maxInt) {
                        maxInt = value;
                    }
                }


                System.out.println("Минимальное целое число: " + (minInt));
                System.out.println("Максимальное целое число: " + (maxInt));
                System.out.println("Среднее значение: " + sum / integersStrings.size());

            } catch (Exception e) {
                System.out.println("Некорректное значение в списке целых чисел: \nОшибка:" + e.getMessage());
            }
        }

        System.out.println("--Числа с плавающей точкой");
        // 2. Для чисел с плавающей точкой находим максимальное и минимальное значение
        if (floatsStrings != null && !floatsStrings.isEmpty()) {
            double minFloat = Double.MAX_VALUE;
            double maxFloat = Double.MIN_VALUE;

            double sum = 0;

            try {
                for (String str : floatsStrings) {
                    double value = StringToNumberConverter.stringToFloat(str);
                    sum += value;

                    if (value < minFloat) {
                        minFloat = value;
                    }
                    if (value > maxFloat) {
                        maxFloat = value;
                    }
                }


                System.out.println("Минимальное значение: " + minFloat);
                System.out.println("Максимальное значение: " + maxFloat);
                System.out.println("Среднее значение: " + sum / floatsStrings.size());

            } catch (Exception e) {
                System.out.println("Некорректное значение в списке чисел с плавающей точкой: " + "\nОшибка:" + e.getMessage());
            }
        }

        System.out.println("--Строки");
        // 3. Для строк находим минимальную и максимальную по длине строку
        if (textsStrings != null && !textsStrings.isEmpty()) {
            String minString = textsStrings.get(0);
            String maxString = textsStrings.get(0);

            for (String str : textsStrings) {
                if (str.length() < minString.length()) {
                    minString = str;
                }
                if (str.length() > maxString.length()) {
                    maxString = str;
                }
            }


            System.out.println("Минимальная длина строки: " + minString.length());
            System.out.println("Максимальная длина строка: " + maxString.length());
        }
    }

    public void MakeStatistics(List<String> textsStrings, List<String> integersStrings , List<String> floatsStrings)
    {
        if(statisticMode == UtilityParameters.StatisticsMode.NONE)
            return;

        if(statisticMode == UtilityParameters.StatisticsMode.FULL)  {
            System.out.println("Полная статистика:");
            MakeFullStatistics(textsStrings, integersStrings, floatsStrings);
        }
        else {
            System.out.println("Краткая статистика:");
            MakeBriefStatistics(textsStrings, integersStrings, floatsStrings);
        }
    }
}

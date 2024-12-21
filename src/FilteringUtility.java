import java.util.ArrayList;
import java.util.List;

// Основной класс работы программы
public class FilteringUtility {
    // Имена выходных файлов
    public static final String StringsFileName = "strings.txt";
    public static final String IntergersFileName = "integers.txt";
    public static final String FloatsFileName = "floats.txt";

    // Параметры запуска утилиты
    private UtilityParameters unitilityParameters;
    // Статистика фильтрации данных
    private UtilityStatistics unitilityStatistics;

    public FilteringUtility(String[] args) {
        // Проверка, что переданы аргументы командной строки (пути к файлам)
        if (args.length == 0) {
            System.out.println("---Пожалуйста, передайте пути к файлам.");
            return;
        }

        unitilityParameters = new UtilityParameters(args);
        unitilityStatistics = new UtilityStatistics(unitilityParameters.getFilterStatisticsMode(), unitilityParameters.getFileNameAddition());
    }

    // Запуск утилиты
    public void UtilityStart() {
        List<String> textsStrings = new ArrayList<String>(0);
        List<String> integersStrings = new ArrayList<String>(0);
        List<String> floatsStrings = new ArrayList<String>(0);

        try {
            // Обработка каждого файла, переданного как аргумент
            for (String filePath : unitilityParameters.getInputFiles()) {
                try {
                    // Используем вспомогательный класс для чтения файла
                    List<String> lines = FileHandler.readFileAndGetLines(filePath);
                    DataTypeFinite dataTypeFinite = new DataTypeFinite();

                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        DataTypeFinite.StringType type = dataTypeFinite.identifyDataType(line);

                        switch (type) {
                            case DataTypeFinite.StringType.TEXT:
                                textsStrings.add(line);
                                break;
                            case DataTypeFinite.StringType.INTEGER:
                                integersStrings.add(line);
                                break;
                            case DataTypeFinite.StringType.FLOAT:
                                floatsStrings.add(line);
                                break;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("---Ошибка при чтении файла: " + filePath);
                    e.printStackTrace();
                }
            }

            // Определяем режим добавления данных в файл
            boolean addDataMode = unitilityParameters.getFilterFileMode() == UtilityParameters.FileMode.ADD;

            if (!textsStrings.isEmpty()) {
                String stringFileFullPath = unitilityParameters.getNewFilePath(StringsFileName);
                FileHandler.writeLinesToFile(stringFileFullPath, textsStrings, addDataMode);
            }

            if (!integersStrings.isEmpty()) {
                String stringFileFullPath = unitilityParameters.getNewFilePath(IntergersFileName);
                FileHandler.writeLinesToFile(stringFileFullPath, integersStrings, addDataMode);
            }

            if (!floatsStrings.isEmpty()) {
                String stringFileFullPath = unitilityParameters.getNewFilePath(FloatsFileName);
                FileHandler.writeLinesToFile(stringFileFullPath, floatsStrings, addDataMode);
            }

            unitilityStatistics.MakeStatistics(textsStrings, integersStrings, floatsStrings);

        } catch (Exception e) {
            System.err.println("---Общая ошибка при обработке файлов.");
            e.printStackTrace();
        }
    }
}

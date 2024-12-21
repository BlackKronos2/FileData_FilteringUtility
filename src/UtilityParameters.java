import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Параметры запуска утилиты
public class UtilityParameters {
    // Режимы статистики
    public enum StatisticsMode {
        NONE,
        BRIEF,
        FULL,
    }

    // Режимы обработки файлов
    public enum FileMode {
        CREATE, //Перезапись файлов
        ADD, //Добавление файлов
    }

    // Режим обработки файлов
    private FileMode filterFileMode;
    // Режим вывода статистики
    private StatisticsMode filterStatisticsMode;
    // Путь для результатов
    private String filePathAddition;
    // Префикс для выходных файлов
    private String fileNameAddition;

    // Пусть до входных файлов
    private List<String> inputFiles;

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public String getFilePathAddition() {
        return filePathAddition;
    }

    public String getFileNameAddition(){
        return fileNameAddition;
    }

    public FileMode getFilterFileMode() {
        return filterFileMode;
    }

    public StatisticsMode getFilterStatisticsMode()
    {
        return filterStatisticsMode;
    }

    public UtilityParameters(String[] args)
    {
        fileNameAddition = "";
        filePathAddition = "";

        filterFileMode = FileMode.CREATE;
        filterStatisticsMode = StatisticsMode.NONE;

        inputFiles = new ArrayList<String>();
        argsHandler(args);
    }

    // Новый путь файла, учитывая префикс и заданный путь
    public String getNewFilePath(String fileName) {
        String filePath = fileNameAddition + fileName;

        if(filePathAddition != "")
            filePath = filePathAddition + "//" + filePath;

        return filePath;
    }

    // Настройки значений параметров от значений аргументов
    public void argsHandler(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if(filePathAddition != "")
                        throw new IllegalArgumentException("---Параметр -o. уже был задан");

                    if (i + 1 < args.length) {
                        // Проверка на правильность пути
                        String path = args[++i];
                        if (!new File(path).exists()) {
                            throw new IllegalArgumentException("---Некорректный путь к файлу: " + path);
                        }
                        filePathAddition = path; // Устанавливаем путь для файлов
                    } else {
                        throw new IllegalArgumentException("---Отсутствует значение для параметра -o.");
                    }
                    break;
                case "-p":
                    if(fileNameAddition != "")
                        throw new IllegalArgumentException("---Параметр -p. уже был задан");

                    if (i + 1 < args.length) {
                        fileNameAddition = args[++i]; // Устанавливаем префикс для файлов
                    } else {
                        throw new IllegalArgumentException("---Отсутствует значение для параметра -p.");
                    }
                    break;
                case "-a":
                    if (filterFileMode == FileMode.ADD)
                        throw new IllegalArgumentException("---Параметр -a указан несколько раз.");

                    filterFileMode = FileMode.ADD; // Устанавливаем режим добавления в файл
                    break;
                case "-s":
                    if (filterStatisticsMode != StatisticsMode.NONE)
                        throw new IllegalArgumentException("---Параметр для статистики указан несколько раз.");

                    filterStatisticsMode = StatisticsMode.BRIEF; // Устанавливаем краткую статистику
                    break;
                case "-f":
                    if (filterStatisticsMode != StatisticsMode.NONE)
                        throw new IllegalArgumentException("---Параметр для статистики указан несколько раз.");

                    filterStatisticsMode = StatisticsMode.FULL; // Устанавливаем полную статистику
                    break;
                default:
                    // Если это не параметр, то это имя файла для ввода
                    File inputFile = new File(args[i]);
                    if (inputFile.exists() && inputFile.isFile()) {
                        inputFiles.add(args[i]); // Добавляем файл в список
                    } else {
                        throw new IllegalArgumentException("---Некорректный файл для ввода: " + args[i]);
                    }
                    break;
            }
        }
    }
}

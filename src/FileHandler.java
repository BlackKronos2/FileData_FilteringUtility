import java.io.*;
import java.nio.file.*;
import java.util.List;

// Класс для чтении и загрузки данных
public class FileHandler {
    public static final int MaxLineLength = 200;

    // Метод для чтения файла
    public static List<String> readFileAndGetLines(String filePath) throws Exception {
        Path path = Paths.get(filePath);

        // Проверка существования файла
        if (!Files.exists(path) || !Files.isRegularFile(path))
            throw new Exception("--Файл не существует или это не обычный файл: " + filePath);

        try {
            // Чтение файла построчно и сохранение в список
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                if (line.length() > MaxLineLength) {
                    throw new Exception("--Строка слишком длинная (больше " + MaxLineLength + " символов): " + line);
                }
            }

            return lines;
        } catch (IOException e) {
            throw new Exception("--Ошибка при чтении файла: " + filePath, e);
        }
    }

    // Метод для записи строк в файл
    public static void writeLinesToFile(String filePath, List<String> lines, boolean add) throws Exception {
        Path path = Paths.get(filePath);

        try {
            // Определяем, какие флаги использовать в зависимости от значения add
            StandardOpenOption[] options;
            if (add) {
                // Если add == true, добавляем данные в файл, если он существует
                options = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND};
            } else {
                // Если add == false, перезаписываем файл
                options = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            }

            // Записываем строки в файл
            Files.write(path, lines, options);
        } catch (IOException e) {
            throw new Exception("--Ошибка при записи в файл: " + filePath, e);
        }
    }


}

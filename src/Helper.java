// Импорт пакетов
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

// Класс "Помощник"
public class Helper{

    // Методы

    // Метод чтения данных из txt-файла
    public static List<String> readFromTxt(String pathToTxt) throws IOException{
        return Files.readAllLines(Path.of(pathToTxt));
    }

    // Метод подсчета строк в списке, начинающихся с part
    public static int countLinesStartWithPart(List<String> lines, String part){
        int count = 0;
        for(String line : lines){
            if(line.startsWith(part)){
                count++;
            }
        }
        return count;
    }

    // Метод извлечения данных из одной строки
    public static String extractDataFromString(String line, String label){
        int startIndex = label.length() + 2;
        return line.substring(startIndex);
    }

}

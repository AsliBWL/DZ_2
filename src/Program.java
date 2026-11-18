// Импорт пакетов
import java.util.*;

public class Program{
    public static void main(String[] args){

        Book.createBooksFromTxt("Книги.txt");
        Student.createStudentsFromTxt("Студенты.txt");

        Student.getStudents().stream()
                .peek(System.out::println)                                                                // вывод в консоль информации о каждом студенте
                .map(Student::getBooks)                                                                   // получение списка книг для каждого студента
                .flatMap(List::stream)                                                                    // получение потока книг для каждого студента и слияние их в общий поток
                .distinct()                                                                               // получение уникальных книг
                .sorted(Comparator.comparing(Book::getNumOfPages))                                        // сортировка книг по количеству страниц
                .filter(book -> book.getDateOfRelease() > 2000)                                     // фильтрация книг по их году выпуска (оставляем только те, что были выпущены после 2000)
                .limit(3)                                                                         // ограничиваем поток тремя элементами
                .map(Book::getDateOfRelease)                                                              // получение года выпуска для каждой книги
                .findFirst()                                                                              // получение Optional от первого в потоке года выпуска книги
                .ifPresentOrElse(
                        data -> System.out.printf("Год выпуска найденной книги: %d\n", data),       // вывод в консоль года выпуска найденной книги или
                              () -> System.out.print("Книга, выпущенная после 2000 года, не найдена.\n")  // вывод в консоль информации о том, что книга не найдена
                );

    }
}

// Импорт пакетов
import static java.lang.System.out;
import java.util.*;
import java.io.IOException;

// Класс "Студент"
public final class Student{

    // Поля
    private final String name;       // полное имя студента
    private final String group;      // номер группы, в которой учится студент
    private final List<Book> books;  // книги, имеющиеся у студента

    // Статические поля
    private final static List<Student> students = new ArrayList<>();  // все студенты

    // Конструкторы
    public Student(String name, String group, Book ...books){
        this.name = name;
        this.group = group;
        if(books == null || books.length < 5){
            throw new IllegalArgumentException(
                    "У студента " + this.name + " из группы " + this.group + " должно быть не менее 5 книг.");
        }
        this.books = new ArrayList<>(Arrays.asList(books));
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public String getGroup(){
        return this.group;
    }

    public List<Book> getBooks(){
        return new ArrayList<>(this.books);
    }

    public static List<Student> getStudents(){
        return new ArrayList<>(students);
    }

    // Методы

    // Метод добавления студента в общий список
    public static void addStudent(Student student){
        students.add(student);
    }

    // Метод получения названий книг, имеющихся у студента
    private String[] getNamesOfBooks(){
        List<Book> books = new ArrayList<>(this.books);
        String[] booksStr = new String[books.size()];
        for(short i = 0; i < books.size(); i++){
            booksStr[i] = "\"" + books.get(i).getName() + "\"";
        }
        return booksStr;
    }

    // Метод вывода в консоль информации о студенте
    public void printInfo(){
        out.print("------------------------------------------------------------\n");
        out.printf("Полное имя студента: %s\n", this.name);
        out.printf("Номер группы студента: %s\n", this.group);
        out.printf("Книги, имеющиеся у студента: %s\n", String.join(", ", this.getNamesOfBooks()));
        out.print("------------------------------------------------------------\n");
    }

    // Метод обработки данных из txt-файла
    private static List<List<String>> processDataFromTxt(List<String> lines){
        lines.removeIf(line -> line.startsWith("-"));
        int countName = Helper.countLinesStartWithPart(lines, "Полное имя");
        int countGroup = Helper.countLinesStartWithPart(lines, "Номер группы");
        int countBooks = Helper.countLinesStartWithPart(lines, "Книги");
        if(countName != countGroup || countName != countBooks){
            throw new IllegalArgumentException("""
                    В txt-файле с данными о студентах не для всех студентов указана полная информация.
                    Отсутствует одна или несколько из этих позиций: \"Полное имя\", \"Номер группы\", \"Книги\".""");
        }
        List<String> names = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        List<String> books = new ArrayList<>();
        for(String line : lines){
            if(line.startsWith("Полное имя")){
                names.add(Helper.extractDataFromString(line, "Полное имя"));
            }
            else if(line.startsWith("Номер группы")){
                groups.add(Helper.extractDataFromString(line, "Номер группы"));
            }
            else{
                books.add(Helper.extractDataFromString(line, "Книги"));
            }
        }
        List<List<String>> data = new ArrayList<>();
        data.add(names);
        data.add(groups);
        data.add(books);
        return data;
    }

    // Метод получения массива книг по строке с названиями книг
    private static Book[] getArrayOfBooks(String line){
        String[] names = line.split(", ");
        String[] namesWithoutQuotationMarks = new String[names.length];
        for(int i = 0; i < names.length; i++){
            namesWithoutQuotationMarks[i] = names[i].substring(1, names[i].length() - 1);
        }
        List<Book> books = Book.getBooks();
        Book[] arrayBooks = new Book[names.length];
        for(int i = 0; i < names.length; i++){
            for(Book book : books){
                if(namesWithoutQuotationMarks[i].equals(book.getName())){
                    arrayBooks[i] = book;
                }
            }
        }
        return arrayBooks;
    }

    // Метод создания студентов по данным из txt-файла
    public static void createStudentsFromTxt(String pathToTxt){
        try{
            List<String> lines = Helper.readFromTxt(pathToTxt);
            List<List<String>> data = processDataFromTxt(lines);
            int size = data.getFirst().size();
            for(int i = 0; i < size; i++){
                String name = data.get(0).get(i);
                String group = data.get(1).get(i);
                Book[] books = getArrayOfBooks(data.get(2).get(i));
                Student student = new Student(name, group, books);
                addStudent(student);
            }
        }
        catch(IOException ex){
            out.printf("\uD83D\uDEAB Ошибка: Ошибка при чтении из файла %s\n", pathToTxt);
            System.exit(1);
        }
        catch(Exception ex){
            out.printf("\uD83D\uDEAB Ошибка: %s", ex.getMessage());
            System.exit(1);
        }
    }

    // Метод, возвращающий описание текущего объекта в формате строки
    @Override
    public String toString(){
        return "Полное имя: " + this.name + "; Номер группы: " + this.group +
                "; Книги: " + String.join(", ", this.getNamesOfBooks());
    }

}

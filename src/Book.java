// Импорт пакетов
import static java.lang.System.out;
import java.util.*;
import java.io.IOException;

// Класс "Книга"
public final class Book{

    // Поля
    private final String name;            // название книги
    private final String[] authors;       // авторы книги
    private final short numOfPages;       // количество страниц в книге
    private final short dateOfRelease;    // дата выпуска (издания) книги (год)

    // Статические поля
    private final static List<Book> books = new ArrayList<>();  // все книги

    // Конструкторы
    public Book(String name, String[] authors, short numOfPages, short dateOfRelease){
        this.name = name;
        this.authors = authors.clone();
        this.numOfPages = numOfPages;
        this.dateOfRelease = dateOfRelease;
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public String[] getAuthors(){
        return this.authors.clone();
    }

    public short getNumOfPages(){
        return this.numOfPages;
    }

    public short getDateOfRelease(){
        return this.dateOfRelease;
    }

    public static List<Book> getBooks(){
        return new ArrayList<>(books);
    }

    // Методы

    // Метод добавления книги в общий список
    public static void addBook(Book book){
        books.add(book);
    }

    // Метод вывода в консоль информации о книге
    public void printInfo(){
        out.print("------------------------------------------------------------\n");
        out.printf("Название книги: %s\n", this.name);
        out.printf("Авторы книги: %s\n", String.join(", ", this.authors));
        out.printf("Количество страниц в книге: %d\n", this.numOfPages);
        out.printf("Дата выпуска (издания) книги: %d\n", this.dateOfRelease);
        out.print("------------------------------------------------------------\n");
    }

    // Метод обработки данных из txt-файла
    private static List<List<String>> processDataFromTxt(List<String> lines){
        lines.removeIf(line -> line.startsWith("-"));
        int countName = Helper.countLinesStartWithPart(lines, "Название");
        int countAuthors = Helper.countLinesStartWithPart(lines, "Авторы");
        int countNumOfPages = Helper.countLinesStartWithPart(lines, "Количество страниц");
        int countDateOfRelease = Helper.countLinesStartWithPart(lines, "Дата выпуска");
        if(countName != countAuthors || countName != countNumOfPages || countName != countDateOfRelease){
            throw new IllegalArgumentException("""
                    В txt-файле с данными о книгах не для всех книг указана полная информация. Отсутствует одна или
                    несколько из этих позиций: \"Название\", \"Авторы\", \"Количество страниц\", \"Дата выпуска\".""");
        }
        List<String> names = new ArrayList<>();
        List<String> authorss = new ArrayList<>();
        List<String> numsOfPages = new ArrayList<>();
        List<String> datesOfRelease = new ArrayList<>();
        for(String line : lines){
            if(line.startsWith("Название")){
                names.add(Helper.extractDataFromString(line, "Название"));
            }
            else if(line.startsWith("Авторы")){
                authorss.add(Helper.extractDataFromString(line, "Авторы"));
            }
            else if(line.startsWith("Количество страниц")){
                numsOfPages.add(Helper.extractDataFromString(line, "Количество страниц"));
            }
            else{
                datesOfRelease.add(Helper.extractDataFromString(line, "Дата выпуска"));
            }
        }
        List<List<String>> data = new ArrayList<>();
        data.add(names);
        data.add(authorss);
        data.add(numsOfPages);
        data.add(datesOfRelease);
        return data;
    }

    // Метод создания книг по данным из txt-файла
    public static void createBooksFromTxt(String pathToTxt){
        try{
            List<String> lines = Helper.readFromTxt(pathToTxt);
            List<List<String>> data = processDataFromTxt(lines);
            int size = data.getFirst().size();
            for(int i = 0; i < size; i++){
                String name = data.get(0).get(i);
                String[] authors = data.get(1).get(i).split(", ");
                short numOfPages = Short.parseShort(data.get(2).get(i));
                short dateOfRelease = Short.parseShort(data.get(3).get(i));
                Book book = new Book(name, authors, numOfPages, dateOfRelease);
                addBook(book);
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

    // Метод сравнения текущего объекта с другим объектом по значению
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }
        Book book = (Book) o;
        return this.name.equals(book.name) &&
                Arrays.equals(this.authors, book.authors) &&
                this.numOfPages == book.numOfPages &&
                this.dateOfRelease == book.dateOfRelease;
    }

    // Метод получения хэш-кода текущего объекта
    @Override
    public int hashCode(){
        int result = Objects.hash(this.name, this.numOfPages, this.dateOfRelease);
        result = 31 * result + Arrays.hashCode(this.authors);
        return result;
    }

}

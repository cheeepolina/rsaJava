import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class RSAE {

    private static final BigInteger ONE = BigInteger.ONE;//1

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Введите a, если хотите самостоятельно ввести текст, b, если работаете из файла");
        Scanner r = new Scanner(System.in);// Создать объект сканера
        String s0 = r.nextLine();// Получить строку этой строки
        if (s0.equals("a")){
        System.out.println("Введите текст который хотите зашифровать(заглавными буквами)");
        Scanner S = new Scanner(System.in);// Создать объект сканера
        String message = S.nextLine();// Получить строку этой строки
        BigInteger p = BigInteger.valueOf(7);////BigInteger-тип переменных для работы с большими числами
            //используем его для возможности использования математических функций
        BigInteger q = BigInteger.valueOf(13);

        BigInteger n = p.multiply(q);//multiply возвращает произведение двух чисел
        BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE));//subtract возвращает разность двух чисел
                    //(p-1)*(q-1).
            BigInteger e = generateE(phi);//открытый
        BigInteger d = e.modInverse(phi);//закрытый вычисляем d обратное e по модулю f произведение d*e
                    //(e*d) mod ((p-1)*(q-1))=1.
        System.out.println("Открытый ключ: (" + e + ", " + n + ")");
       System.out.println("Закрытый ключ: (" + d + ", " + n + ")");

        int[] encrypted = encrypt(message, e, n);
        System.out.println("Зашифрованное сообщение: " + Arrays.toString(encrypted));

        String decrypted = decrypt(encrypted, d, n);
        System.out.println("Расшифрованное сообщение: " + decrypted);

        if (message.equals(decrypted)) {
            System.out.println("Успешно!");
        } else {
            System.out.println("ОШИБКА!");
            }
        }
        if (s0.equals("b")) {//работа с файлом все то же самое
            String path = "/Users/polinatrehleb/Desktop/polina.txt"; //указываем путь до файла и оформляем его в переменную
            File file = new File(path);//создаем файл чтобы считывать его
            Scanner scanner = new Scanner(file);//передаем сканнеру файл
            String message = scanner.nextLine();//считываем строку
            BigInteger p = BigInteger.valueOf(7);
            BigInteger q = BigInteger.valueOf(13);

            BigInteger n = p.multiply(q);//усножение
            BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE));

            BigInteger e = generateE(phi);
            BigInteger d = e.modInverse(phi);

            System.out.println("Открытый ключ: (" + e + ", " + n + ")");
            System.out.println("Закрытый ключ: (" + d + ", " + n + ")");

            int[] encrypted = encrypt(message, e, n);
            System.out.println("Зашифрованный текст: " + Arrays.toString(encrypted));

            String decrypted = decrypt(encrypted, d, n);
            System.out.println("Расшифрованный текст: " + decrypted);

            if (message.equals(decrypted)) {
                System.out.println("УСПЕХ!");
            } else {
                System.out.println("ОШИБКА!");
            }

        }
    }

    private static int[] encrypt(String message, BigInteger e, BigInteger n) {//шифруем
        int[] encrypted = new int[message.length()];//создаем массив куда будем складывать преобразованные буквы
        for (int i = 0; i < message.length(); i++) {//charAt(i), где i - текущий индекс в цикле
            // каждый символ сообщения представляется в виде объекта класса BigInteger с помощью метода valueOf().
            BigInteger m = BigInteger.valueOf(message.charAt(i));
            BigInteger c = m.modPow(e, n);//модульное деление числа, возведенного в степень
            encrypted[i] = c.intValue();//запись в массив
        }// зашифрованный символ  сохраняется в массиве encrypted[]
        // с помощью метода intValue() кооторый возвращает примитивное значение типа int
        return encrypted;
    }

    private static String decrypt(int[] encrypted, BigInteger d, BigInteger n) {//расшифровка
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < encrypted.length; i++) {//также проходимся по каждому числу массива
            BigInteger c = BigInteger.valueOf(encrypted[i]);
            BigInteger m = c.modPow(d, n);
            char ch = (char) m.intValue();// символьное значение m преобразуется в символ типа char
            // с помощью метода (char)m.intValue()
            decrypted.append(ch);//присоединяем к строке
        }
        return decrypted.toString();
    }
    private static BigInteger generateE(BigInteger phi) {//подбор е
        Random rand = new Random();
        BigInteger e = BigInteger.valueOf(2);// начинаем с 2
        while (e.compareTo(phi) < 0 && !phi.gcd(e).equals(BigInteger.ONE)) {// должно быть меньше фи,
            e = e.add(BigInteger.ONE);////compareTo сравнивает два числа
            //phi.gcd(e) вычисляет наибольший общий делитель чисел f и e
        }
        if (e.compareTo(phi) >= 0) {
            return BigInteger.ZERO;
        }
        return e;
    }
}
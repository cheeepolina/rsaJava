
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class RSA
{
    //BigInteger-тип переменных для работы с большими числами
    private BigInteger p;//простое число
    private BigInteger q;
    private BigInteger n;//модуль p*q
    private BigInteger f;//функция Эйлера
    private BigInteger e;//открытый ключ
    private BigInteger d;//закрытый ключ
    private int l = 1024;//длина
    private Random R;//Random класс для генерации случаных чисел

    public RSA()
    {
        R = new Random();
         p = BigInteger.probablePrime(l, R);//генерация случайного числа
         q = BigInteger.probablePrime(l, R);

         n = p.multiply(q);//multiply возвращает произведение двух чисел
         f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
         //subtract возвращает разность двух чисел

        //генерирует случайное простое число которое является взаимно простым с числом f
         e = BigInteger.probablePrime(l/2, R);//генерируем е
        while (f.gcd(e).compareTo(BigInteger.ONE)>0 && e.compareTo(f)<0)
        {//compareTo сравнивает два числа
            //f.gcd(e) вычисляет наибольший общий делитель чисел f и e
            //compareTo(BigInteger.ONE)>0 сравнивает нод с 1
            //e.compareTo(f)<0 сравнивает число e с числом f
            //если нод не равен 1 и e меньше f, то
            // увеличиваем значение e на 1 с помощью метода e.add(BigInteger.ONE).
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(f);//modInverse возвращает обратноу по модулю число
    }


    public static void main (String [] arguments) throws IOException
    {
        RSA rsa = new RSA();
        System.out.println("Введите a, если хотите самостоятельно ввести текст, b, если работаете из файла");
        Scanner S = new Scanner(System.in);// Создать объект сканера
        String s0 = S.nextLine();// Получить строку этой строки
        if (s0.equals("a")) {//работа с консолью
        System.out.println("Введите текст, который хотите зашифровать");
        Scanner c = new Scanner(System.in);// Создать объект сканера
        String inputString = c.nextLine();// Получить строку этой строки
        //System.out.println("Шифруемое сообщение: " + inputString);
        System.out.println("Байтовое представление: " + str(inputString.getBytes()));
        byte[] cipher = rsa.encrypt(inputString.getBytes());
        System.out.print("Зашифрованный текст: ");
        for (byte b : cipher) {
            System.out.print((b & 0xFF) + "");
        }
        byte[] plain = rsa.decrypt(cipher);
        System.out.println("       ");
        System.out.println("Расшифрованный текст: " + str(plain));
        System.out.println("Изначальный текст: " + new String(plain));
    }
        if (s0.equals("b")){//работа с файлом все то же самое
            String path = "/Users/polinatrehleb/Desktop/polina.txt"; //указываем путь до файла и оформляем его в переменную
            File file= new File(path);//создаем файл чтобы считывать его
            Scanner scanner = new Scanner(file);//передаем сканнеру файл
            String line = scanner.nextLine();//считываем строку

            System.out.println("Байтовое представление: " + str(line.getBytes()));
            byte[] cipher = rsa.encrypt(line.getBytes());
            System.out.print("Зашифрованный текст: ");
            for (byte b : cipher) {
                System.out.print((b & 0xFF) + "");
            }
            byte[] plain = rsa.decrypt(cipher);
            System.out.println("       ");
            System.out.println("Расшифрованный текст: " + str(plain));
            System.out.println("Изначальный текст: " + new String(plain));
        }

    }
    private static String str(byte[] cipher)// метод преобразует массив байтов
    // в строку состоящую из  числовых значение каждого байта.
    {
        String str = "";
        for (byte b : cipher)//перебор каждого элемента в массиве
        {
            str+= Byte.toString(b);//преобразуем значение каждого байта в строку
            //и добавляем в конец строки str
        }
        return str;
    }

    public byte[] encrypt(byte[] message)//зашфрвываем
    {
        return (new BigInteger(message)).modPow(e, n).toByteArray();//modPow выполняет модульное деление
        //числа, возведенного в степен другого
    }
    public byte[] decrypt(byte[] message) //расшифровываем
    {
        return (new BigInteger(message)).modPow(d, n).toByteArray();
    }
}
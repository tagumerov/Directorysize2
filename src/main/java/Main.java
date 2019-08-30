import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = "";

        System.out.println("Подсчет размера каталога или файла");
        System.out.println("Укажите путь к каталогу или файлу");
        try {
            fileName = reader.readLine();
            reader.close();
            logger.info("Введен путь {}", fileName);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        try (Stream<Path> walk = Files.walk(Paths.get(fileName))) {
            long size = walk.filter(Files::isRegularFile).mapToLong(Main::count).sum();
            printSize(size);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }

    }

    static long count(Path path) {
        try {
            return Files.size(path);
        } catch (IOException | UncheckedIOException e) {
            logger.error(e);
            return 0;
        }
    }

    private static void printSize(Long size) {
        String[] prefix = {"", "кило", "мега", "гига"};
        double sizeDouble = (double)size;
        int index;

        for (index = 0; index < 3 && sizeDouble > 1024; index++)
            sizeDouble /= 1024;

        System.out.printf("Размер: %.3f %s%s%n", sizeDouble, prefix[index], "байт");
    }
}

package store.global.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.global.exception.ReadException;

public class Reader {

    public static List<String> readFiles(String path) {
        return readFile(path);
    }

    private static List<String> readFile(String filePath) {
        List<String> data = new ArrayList<>();
        readLinesFromFile(filePath, data);
        return data;
    }

    private static void readLinesFromFile(String filePath, List<String> data) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            throw new ReadException(ErrorMessage.ERROR_READING_FILE.getMessage(), e);
        }
    }

}

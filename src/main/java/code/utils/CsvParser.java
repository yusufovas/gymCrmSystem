package code.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CsvParser {

    public static <T> List<T> parse(String filepath, Class<T> c) {
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            try(Reader reader = new InputStreamReader(resource.getInputStream())) {
                return new CsvToBeanBuilder<T>(reader)
                        .withType(c)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withThrowExceptions(true)
                        .build()
                        .parse();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse csv file " + filepath, e);
        }
    }
}
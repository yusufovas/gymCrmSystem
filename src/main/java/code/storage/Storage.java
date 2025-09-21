package code.storage;

import code.model.Trainee;
import code.model.Trainer;
import code.model.Training;
import code.model.TrainingType;
import code.utils.CsvParser;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Storage {
    private static final Logger log = LogManager.getLogger(Storage.class);
    Map<String, Map<Integer, Object>> storage = new ConcurrentHashMap<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${storage.trainee.file}") private String traineeData;
    @Value("${storage.trainer.file}") private String trainerData;
    @Value("${storage.training.file}") private String trainingData;
    @Value("${storage.trainingType.file}") private String trainingTypeData;


    public void put(String namespace, Integer id, Object entity) {
        storage.computeIfAbsent(namespace, k -> new ConcurrentHashMap<>()).put(id, entity);
    }

    public Object get(String namespace, Integer id) {
        return Optional.ofNullable(storage.get(namespace)).map(n -> n.get(id)).orElse(null);
    }

    private <T> void loadCsvData(String namespace, String filePath, Class<T> c) {
        List<T> entities = CsvParser.parse(filePath, c);
        int id = 1;
        for (T entity : entities) {
            put(namespace, id++, entity);
        }
    }

    @PostConstruct
    public void init() {
        log.info("Initializing Storage and loading CSV data...");

        log.debug("Loaded trainees from {}", traineeData);
        loadCsvData("trainee", traineeData, Trainee.class);

        log.debug("Loaded trainers from {}", trainerData);
        loadCsvData("trainer", trainerData, Trainer.class);

        log.debug("Loaded trainings from {}", trainingData);
        loadCsvData("training", trainingData, Training.class);

        log.debug("Loaded training types from {}", trainingTypeData);
        loadCsvData("trainingType", trainingTypeData, TrainingType.class);
    }

    public Map<String, Map<Integer, Object>> getDataMap() {
        return storage;
    }
}


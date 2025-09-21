package code.service;

import code.dao.TraineeDao;
import code.model.Trainee;
import code.utils.PasswordGenerator;
import code.utils.UsernameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TraineeService {
    private TraineeDao traineeDao;
    private static final Logger log = LogManager.getLogger(TraineeService.class);

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {
        log.info("Creating trainee: {} {}", trainee.getFirstName(), trainee.getLastName());

        String username = UsernameGenerator.generate(
                trainee.getFirstName(),
                trainee.getLastName(),
                this::usernameExists
        );
        trainee.setUsername(username);
        trainee.setPassword(PasswordGenerator.generate());

        log.info("New Trainee created: {} {} {}", trainee.getFirstName(), trainee.getLastName(), trainee.getUsername());
        return traineeDao.create(trainee);
    }

    public Trainee update(Integer id, Trainee updTrainee) {
        log.info("Updating trainee with id={}", id);

        return traineeDao.findById(id)
                .map(existing -> {
                    existing.setFirstName(updTrainee.getFirstName());
                    existing.setLastName(updTrainee.getLastName());
                    existing.setDateOfBirth(updTrainee.getDateOfBirth());

                    String newUsername = UsernameGenerator.generate(
                            updTrainee.getFirstName(),
                            updTrainee.getLastName(),
                            this::usernameExists
                    );
                    existing.setUsername(newUsername);

                    return traineeDao.create(existing);
                })
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
    }

    public void deleteProfile(Integer id) {
        traineeDao.delete(id);
        log.info("Trainee with id={} deleted", id);
    }

    public List<Trainee> getAll() {
        log.info("Fetching trainees");
        return traineeDao.findAll();
    }

    private boolean usernameExists(String username) {
        return traineeDao.findAll().stream()
                .anyMatch(t -> username.equals(t.getUsername()));
    }
}


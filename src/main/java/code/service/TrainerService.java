package code.service;

import code.dao.TrainerDao;
import code.model.Trainer;
import code.utils.PasswordGenerator;
import code.utils.UsernameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private TrainerDao trainerDao;
    private static final Logger log = LogManager.getLogger(TrainerService.class);

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer create(Trainer trainer) {
        log.info("Creating trainer: {} {}", trainer.getFirstName(), trainer.getLastName());

        String username = UsernameGenerator.generate(
                trainer.getFirstName(),
                trainer.getLastName(),
                this::usernameExists
        );
        trainer.setUsername(username);
        trainer.setPassword(PasswordGenerator.generate());

        log.info("New Trainer created: {} {} {}", trainer.getFirstName(), trainer.getLastName(), trainer.getUsername());
        return trainerDao.create(trainer);
    }

    public Trainer update(Integer id, Trainer updTrainer) {
        log.info("Updating trainer with id={}", id);

        return trainerDao.findById(id)
                .map(existing -> {
                    existing.setFirstName(updTrainer.getFirstName());
                    existing.setLastName(updTrainer.getLastName());
                    existing.setSpecialization(updTrainer.getSpecialization());

                    String newUsername = UsernameGenerator.generate(
                            updTrainer.getFirstName(),
                            updTrainer.getLastName(),
                            this::usernameExists
                    );
                    existing.setUsername(newUsername);

                    return trainerDao.create(existing);
                })
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
    }

    public List<Trainer> getAll() {
        log.info("Fetching trainers");
        return trainerDao.findAll();
    }

    private boolean usernameExists(String username) {
        return trainerDao.findAll().stream()
                .anyMatch(t -> username.equals(t.getUsername()));
    }
}

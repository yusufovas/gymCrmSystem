package code.repository;

import code.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {
    List<Training> findByTraineeUserUsername(String username);
    List<Training> findByTrainerUserUsername(String username);
}

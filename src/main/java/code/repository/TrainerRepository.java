package code.repository;

import code.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {
    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.isActive = true AND t NOT IN " +
            "(SELECT tr FROM Trainee trn JOIN trn.trainers tr WHERE trn.user.username = :username)")
    List<Trainer> findActiveNotAssignedToTrainee(String username);
}

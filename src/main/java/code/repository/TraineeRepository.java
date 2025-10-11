package code.repository;

import code.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, UUID> {
    Optional<Trainee> findByUserUsername(String username);
    void deleteByUserUsername(String username);
}

package code.dao;

import code.model.Training;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingDao extends AbstractDao<Training, UUID> {
    public TrainingDao(SessionFactory sessionFactory) {
        super(sessionFactory, Training.class);
    }

    public List<Training> findByTraineeAndCriteria(String traineeUsername,
                                                   LocalDate from,
                                                   LocalDate to,
                                                   String trainerName,
                                                   String trainingType) {
        String query = buildTraineeCriteriaQuery(from, to, trainerName, trainingType);

        Query<Training> query1 = getSession().createQuery(query, Training.class);
        query1.setParameter("traineeUsername", traineeUsername);

        if (from != null) query1.setParameter("from", from);
        if (to != null) query1.setParameter("to", to);
        if (trainerName != null && !trainerName.isBlank()) query1.setParameter("trainerName", trainerName);
        if (trainingType != null && !trainingType.isBlank()) query1.setParameter("trainingType", trainingType);

        return query1.getResultList();
    }

    public List<Training> findByTrainerAndCriteria(String trainerUsername,
                                                   LocalDate from,
                                                   LocalDate to,
                                                   String traineeName) {
        String query = buildTrainerCriteriaQuery(from, to, traineeName);

        Query<Training> query1 = getSession().createQuery(query, Training.class);
        query1.setParameter("trainerUsername", trainerUsername);

        if (from != null) query1.setParameter("from", from);
        if (to != null) query1.setParameter("to", to);
        if (traineeName != null && !traineeName.isBlank()) query1.setParameter("traineeName", traineeName);

        return query1.getResultList();
    }

    private String buildTraineeCriteriaQuery(LocalDate from,
                                             LocalDate to,
                                             String trainerName,
                                             String trainingType) {
        StringBuilder query = new StringBuilder("select tr from Training tr " +
                "join tr.trainee t " +
                "join tr.trainer trn " +
                "join tr.trainingType tt " +
                "where t.username = :traineeUsername");

        if (from != null) query.append(" and tr.trainingDate >= :from");
        if (to != null) query.append(" and tr.trainingDate <= :to");
        if (trainerName != null && !trainerName.isBlank()) query.append(" and trn.lastName = :trainerName");
        if (trainingType != null && !trainingType.isBlank()) query.append(" and tt.trainingTypeName = :trainingType");

        return query.toString();
    }

    private String buildTrainerCriteriaQuery(LocalDate from,
                                             LocalDate to,
                                             String traineeName) {
        StringBuilder query = new StringBuilder("select tr from Training tr " +
                "join tr.trainer trn " +
                "join tr.trainee t " +
                "where trn.username = :trainerUsername");

        if (from != null) query.append(" and tr.trainingDate >= :from");
        if (to != null) query.append(" and tr.trainingDate <= :to");
        if (traineeName != null && !traineeName.isBlank()) query.append(" and t.lastName = :traineeName");

        return query.toString();
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.empty();
    }
}

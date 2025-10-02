package code;

import code.facade.GymFacade;
import code.model.Trainee;
import code.model.Trainer;
import code.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(code.config.Config.class);

        GymFacade facade = ctx.getBean(GymFacade.class);

        Trainee trainee = Trainee.builder()
                .user(User.builder()
                        .firstName("Alice")
                        .lastName("Johns")
                        .build())
                .dateOfBirth(LocalDate.of(2006, 10, 5))
                .address("Tashkent")
                .build();

        Trainee createdTrainee = facade.createTraineeProfile(trainee);
        System.out.println("Created Trainee: " + createdTrainee.getUser().getUsername()
                + " password=" + createdTrainee.getUser().getPassword());

        Trainer trainer1 = Trainer.builder()
                .user(User.builder()
                        .firstName("John")
                        .lastName("Mirrow")
                        .build())
                .specialization("Yoga").build();

        Trainer trainer2 = Trainer.builder()
                .user(User.builder()
                        .firstName("Emma")
                        .lastName("Brown")
                        .build())
                .specialization("Cardio")
                .build();

        Trainer createdTrainer1 = facade.createTrainerProfile(trainer1);
        Trainer createdTrainer2 = facade.createTrainerProfile(trainer2);

        System.out.println("Created Trainers: " +
                createdTrainer1.getUser().getUsername() + ", " +
                createdTrainer2.getUser().getUsername());

        System.out.println("Unassigned trainers for trainee " + createdTrainee.getUser().getUsername() + ":");
        List<Trainer> unassigned = facade.getUnassignedTrainersForTrainee(createdTrainee.getUser().getUsername());
        unassigned.forEach(t -> System.out.println(" - " + t.getUser().getUsername()));

        facade.updateTraineeTrainersList(
                createdTrainee.getUser().getUsername(),
                List.of(createdTrainer1.getTrainerId(), createdTrainer2.getTrainerId())
        );
        System.out.println(" Assigned both trainers to trainee " + createdTrainee.getUser().getUsername());

        System.out.println("Unassigned trainers after update:");
        List<Trainer> unassignedAfter = facade.getUnassignedTrainersForTrainee(createdTrainee.getUser().getUsername());
        if (unassignedAfter.isEmpty()) {
            System.out.println(" All trainers are assigned ");
        } else {
            unassignedAfter.forEach(t -> System.out.println(" - " + t.getUser().getUsername()));
        }

        Trainee updatedTrainee = facade.getTraineeByUsername(createdTrainee.getUser().getUsername());
        System.out.println("Trainers assigned to " + updatedTrainee.getUser().getUsername() + ":");
        updatedTrainee.getTrainers()
                .forEach(t -> System.out.println(" - " + t.getUser().getFirstName() + " " + t.getUser().getLastName()));

        ctx.close();
    }
}
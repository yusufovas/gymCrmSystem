package code;

import code.facade.GymFacade;
import code.model.Trainee;
import code.model.Trainer;
import code.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

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

        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("John")
                        .lastName("Mirrow")
                        .build())
                .specialization("Yoga").build();

        Trainer createdTrainer = facade.createTrainerProfile(trainer);

        facade.getTraineeService().getAll()
                .forEach(e -> {
                        User u = e.getUser();
                    System.out.printf("ID=%s, username=%s, name=%s %s, active=%s%n",
                            e.getTraineeId(),
                            u.getUsername(),
                            u.getFirstName(),
                            u.getLastName(),
                            u.isActive());} );

        ctx.close();
    }
}
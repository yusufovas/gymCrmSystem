package code;

import code.facade.GymFacade;
import code.model.Trainee;
import code.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(code.config.Config.class);

        GymFacade facade = ctx.getBean(GymFacade.class);

//        Trainee trainee = Trainee.builder()
//                .user(User.builder()
//                        .firstName("Alice")
//                        .lastName("Johns")
//                        .build())
//                .dateOfBirth(LocalDate.of(2006, 10, 5))
//                .address("Tashkent")
//                .build();
//
//        Trainee createdTrainee = facade.createTraineeProfile(trainee);
//        System.out.println("✅ Created Trainee: " + createdTrainee.getUser().getUsername()
//                + " password=" + createdTrainee.getUser().getPassword());

        System.out.println("GymFacade proxy class = " + facade.getClass());
    }
}
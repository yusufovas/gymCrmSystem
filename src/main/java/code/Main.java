package code;

import code.facade.GymFacade;
import code.model.Trainee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(code.config.Config.class);

        GymFacade facade = ctx.getBean(GymFacade.class);

        Trainee trainee = new Trainee();
        trainee.setFirstName("Alice");
        trainee.setLastName("Williams");
        trainee.setDateOfBirth(LocalDate.of(1998, 11, 20));

        Trainee saved = facade.getTraineeService().create(trainee);
        List<Trainee> all = facade.getTraineeService().getAll();
        System.out.println("Created trainee " + saved);
        System.out.println("All trainees " + all);
    }
}
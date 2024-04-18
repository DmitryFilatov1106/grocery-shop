package ru.fildv.groceryshop.config.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.service.UserService;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile("!test")
public class CommandLineRunnerImpl implements CommandLineRunner {
    private static final int RETURN_CODE = 0;
    private final ConfigurableApplicationContext context;
    //    private final Environment env;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.checkAdmin();

//        // just for check
//        log.info("spring.datasource.url: {}", env.getProperty("spring.datasource.url"));
//        log.info("spring.datasource.username: {}", env.getProperty("spring.datasource.username"));
//        log.info("spring.datasource.password: {}", env.getProperty("spring.datasource.password"));

        try {
            Scanner scanner = new Scanner(System.in);
            String s = "";
            do {
                s = scanner.nextLine();
            } while (!s.equalsIgnoreCase("stop"));
            log.info("Shutdown from console");
            System.exit(SpringApplication.exit(context, () -> RETURN_CODE));

//            while (true) {
//                var s = scanner.nextLine();
//                if (s.equalsIgnoreCase("stop")) {
//                    log.info("Shutdown from console");
//                    System.exit(SpringApplication.exit(context, () -> RETURN_CODE));
//                }
//            }
        } catch (Exception e) {
            log.info("Scanner is failed");
        }
    }
}


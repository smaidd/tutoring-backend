package ro.mdx.meditation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.repository.UserRepository;
import ro.mdx.meditation.services.UserService;
import ro.mdx.meditation.utils.Role;
import ro.mdx.meditation.utils.UserUtils;

@Configuration
@Slf4j
public class UserProvision {

    @Bean
    CommandLineRunner provisionAdmin(UserRepository userRepo,
                                     UserService userService,
                                     PasswordEncoder encoder) {
        return args -> {
            String adminUsername = "admin";
            String adminEmail = "ionita.alex16@gmail.com";

            if (userRepo.findByUsername(adminUsername).isEmpty()) {
                String rawPassword = UserUtils.generateRandomPassword();

                AppUser admin = new AppUser();
                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(rawPassword);
                admin.setRole(Role.ADMIN);

                userService.createUser(admin);
                log.info("✅ Admin user created and password sent to email: {}", adminEmail);
            } else {
                log.info("ℹ️ Admin already exists");
            }
        };
    }
}

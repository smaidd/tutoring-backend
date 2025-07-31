package ro.mdx.meditation.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final MailService mailService;
    private final PasswordEncoder encoder;

    public AppUser createUser(AppUser user) {
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));

        AppUser save = userRepo.save(user);
        mailService.sendAuthenticationInformationEmail(user.getEmail(), user.getUsername(), rawPassword);

        return save;
    }
}

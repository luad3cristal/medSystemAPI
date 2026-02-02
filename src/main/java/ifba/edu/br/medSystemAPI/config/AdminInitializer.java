package ifba.edu.br.medSystemAPI.config;

import ifba.edu.br.medSystemAPI.models.entities.User;
import ifba.edu.br.medSystemAPI.models.enums.Role;
import ifba.edu.br.medSystemAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception { 
        if (!userRepository.existsByUsername(adminEmail)) {
            User admin = new User();
            admin.setUsername(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(admin);
            
            System.out.println("========================================");
            System.out.println("✅ ADMIN CRIADO COM SUCESSO!");
            System.out.println("📧 Email: " + adminEmail);
            System.out.println("🔑 Senha: " + adminPassword);
            System.out.println("========================================");
        } else {
            System.out.println("ℹ️  Admin já existe no banco de dados.");
        }
    }
}

package cat.tecnocampus.apollofy.application;

import cat.tecnocampus.apollofy.application.exceptions.ElementNotFoundInBBDD;
import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FanController {
    private final UserRepository userRepository;

    public FanController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createFan(String origin, String destination) {
        UserFy originUser = userRepository.findByEmail(origin).orElseThrow(() -> new ElementNotFoundInBBDD("origin"));
        UserFy destinationUser = userRepository.findByEmail(destination).orElseThrow(() -> new ElementNotFoundInBBDD("destination"));

        originUser.addNewFan(destinationUser);
    }
}

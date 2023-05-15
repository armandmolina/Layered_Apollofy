package cat.tecnocampus.apollofy.configuration.security;

import cat.tecnocampus.apollofy.domain.UserFy;
import cat.tecnocampus.apollofy.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ApollofyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public ApollofyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserFy user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        return ApollofyUserDetails.build(user);
    }

}
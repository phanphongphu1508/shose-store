package api.service;

import api.DTO.usersDTO;
import api.entity.usersEntity;
import api.repository.usersRepository;
import api.request.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
public class usersService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    usersRepository usersRepository;
    @Autowired
    ModelMapper mapper;

    @Value("${jwt.key}")
    private String keyJWT;

    public String login(LoginRequest loginRequest) {
        String token = "";
        Optional<usersEntity> user = usersRepository.findByUsername(loginRequest.getUsername());
        if (user.isPresent()) {
            usersEntity userEntity = user.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(keyJWT));
                String jws = Jwts.builder().subject("Joe").signWith(key).compact();
                token = jws;
            }
        }
        return token;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        usersEntity user = usersRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("username not found with :" + s));
        System.out.println("user " + user.getUsername() + user.getRoles().toString());
        return UserDetailsImpl.build(user);
    }


    public boolean checkuser(String username) {
        if (usersRepository.existsByUsername(username)) {
            return true;
        }
        return false;
    }

    public usersDTO getusers(String username) {
        usersDTO usersDTO = null;
        usersEntity user = usersRepository.finduser(username);
        if (user == null) {
            return usersDTO;
        }
        usersDTO = mapper.map(user, api.DTO.usersDTO.class);

        usersDTO.setRolename(user.getRoles());
        return usersDTO;
    }

}

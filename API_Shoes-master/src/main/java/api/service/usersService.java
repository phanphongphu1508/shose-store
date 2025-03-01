package api.service;

import api.DTO.usersDTO;
import api.config.ModelMapperConfig;
import api.entity.roleEntity;
import api.entity.role_name;
import api.entity.usersEntity;
import api.repository.roleRepository;
import api.repository.usersRepository;
import api.security.service.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@Component
public class usersService implements UserDetailsService {
    @Autowired
    usersRepository usersRepository;
    @Autowired
    roleRepository roleRepository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        usersEntity user = usersRepository.findByUsername(s).
                orElseThrow(() -> new UsernameNotFoundException("username not found with :"+s));
        System.out.println("user "+user.getUsername() +user.getRoles().toString());
        return UserDetailsImpl.build(user);
    }


    public boolean checkuser(String username){
        if(usersRepository.existsByUsername(username)){
            return true;
        }
        return false;
    }

    public usersDTO getusers(String username){
        usersDTO usersDTO =null;
        usersEntity user = usersRepository.finduser(username);
        if(user == null){
            return usersDTO;
        }
        usersDTO = mapper.map(user, api.DTO.usersDTO.class);

        usersDTO.setRolename(user.getRoles());
        return  usersDTO;
    }

    public boolean forgotPassword(String email) throws MessagingException {
        Optional<usersEntity> user = usersRepository.findByUsername(email);
        if(!user.isPresent()){
            return false;
        }

        String resetLink = "http://localhost:8080/reset-password?email=" + email;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password for ShoesStore");

        String emailContent = "<html><body>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href=\"" + resetLink + "\" style=\"color: blue; text-decoration: underline;\">Reset Password</a></p>"
                + "</body></html>";

        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(mimeMessage);
        return true;
    }

    public boolean setPassword(String email, String newPassword) {
        Optional<usersEntity> user = usersRepository.findByUsername(email);
        if(!user.isPresent()){
            return false;
        }
        else {
            usersEntity userEntity = user.get();
            userEntity.setPassword(encoder.encode(newPassword));
            usersRepository.save(userEntity);
            return true;
        }
    }
}

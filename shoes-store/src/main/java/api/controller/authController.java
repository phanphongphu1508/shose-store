package api.controller;

import api.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class authController {


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginRequest);
    }

    private String validate(LoginRequest loginRequest) {
        if (loginRequest.getUsername().isEmpty()) {
            return "ERR_USERNAME_E001";
        } else if (loginRequest.getPassword().isEmpty()) {
            return "ERR_PASSWORD_E001";
        } else if (!isValidEmailAddress(loginRequest.getUsername())) {
            return "ERR_USERNAME_E002";
        } else if (isValidPassword(loginRequest.getPassword())) {
            return "ERR_PASSWORD_E002";
        } else {
            return "";
        }
    }

    private boolean isValidEmailAddress(String sEmail) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(sEmail);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isValidPassword(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{6,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }
}

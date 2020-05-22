package ua.javaexternal_shulzhenko.repair_agency.services.encoding;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncodingService {

    public BCryptPasswordEncoder gerBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

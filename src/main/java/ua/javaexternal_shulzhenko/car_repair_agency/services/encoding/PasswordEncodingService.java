package ua.javaexternal_shulzhenko.car_repair_agency.services.encoding;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class PasswordEncodingService {
    public static BCryptPasswordEncoder gerBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

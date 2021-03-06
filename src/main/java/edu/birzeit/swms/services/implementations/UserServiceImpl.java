package edu.birzeit.swms.services.implementations;

import edu.birzeit.swms.configurations.Constants;
import edu.birzeit.swms.dtos.UserDto;
import edu.birzeit.swms.enums.UserRole;
import edu.birzeit.swms.exceptions.CustomException;
import edu.birzeit.swms.exceptions.DatabaseException;
import edu.birzeit.swms.mappers.UserMapper;
import edu.birzeit.swms.models.Citizen;
import edu.birzeit.swms.models.ConfirmationToken;
import edu.birzeit.swms.models.SMS;
import edu.birzeit.swms.models.User;
import edu.birzeit.swms.repositories.CitizenRepository;
import edu.birzeit.swms.repositories.ConfirmationTokenRepository;
import edu.birzeit.swms.repositories.UserRepository;
import edu.birzeit.swms.services.ConfirmationTokenService;
import edu.birzeit.swms.services.EmailSenderService;
import edu.birzeit.swms.services.SMSService;
import edu.birzeit.swms.services.UserService;
import edu.birzeit.swms.utils.SWMSUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    SMSService smsService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    User admin;

    @Autowired
    SWMSUtil util;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.info("loadUserByUsername --- loading user username to authenticate");
            if (username.equals(Constants.ADMIN_USERNAME)) {
                log.warn("loadUserByUsername --- trying to login as an administrator");
                return admin;
            } else {
                User user = userRepository.findByUsername(username).orElseThrow(() -> {
                    log.error(String.format("loadUserByUsername --- user with username: '%s' not found", username));
                    return new UsernameNotFoundException(String.format("User with username: '%s' not found", username));
                });
                log.debug(String.format("loadUserByUsername --- user was found: '%s'", user.getUsername()));
                return user;
            }
        } catch (UsernameNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(String.format("loadUserByUsername --- database persisting error accessing user table\n%s", ex));
            throw new DatabaseException(ex);
        }
    }

    public void signUpUser(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        final String encryptedPassword = passwordEncoder.encode(user.getPassword());
        Citizen citizen = new Citizen();
        citizen.setUsername(user.getUsername());
        citizen.setFirstName(user.getFirstName());
        citizen.setLastName(user.getLastName());
        citizen.setAddress(user.getAddress());
        citizen.setPhone(user.getPhone());
        citizen.setEnabled(false);
        citizen.setRole(UserRole.CITIZEN);
        citizen.setPassword(encryptedPassword);
        final Citizen createdUser = citizenRepository.save(citizen);
        final ConfirmationToken confirmationToken = new ConfirmationToken(createdUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationSMS(createdUser.getPhone(), confirmationToken.getConfirmationToken());
    }

    public void sendConfirmationSMS(String userMail, String token) {
        final SMS sms = new SMS();
        sms.setTo("0097" + userMail);
        sms.setMessage("Thank you for registering in SWMS. Please click on the below link to activate your account." + "http://swms.ga/api/v1/confirm?token="
                + token);
        smsService.send(sms);
    }

    public void confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findAllByConfirmationToken(token).orElseThrow(() ->
                new IllegalArgumentException("token not valid")
        );
        try {
            final User user = confirmationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
        } catch (Exception e) {
            throw new CustomException("Error while accessing user information", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public UserDto getUser() {
        UserDto userDto = userMapper.userToDto(util.getLoggedInUser());
        return userDto;
    }

    public void setPassword(String token, String password) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findAllByConfirmationToken(token).orElseThrow(() ->
                new CustomException("token not valid", HttpStatus.UNAUTHORIZED)
        );
        try {
            final User user = confirmationToken.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
        } catch (Exception e) {
            throw new CustomException("Error while accessing user information", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public void sendPasswordSettingSMS(String username, String userMail, String token) {
        final SMS sms = new SMS();
        sms.setTo("0097" + userMail);
        sms.setMessage("Thank you for registering in SWMS.\nYour username is: "+username+"\nPlease click on the below link to set your password." + "http://swms.ga/api/v1/set-password?token="
                + token);
        smsService.send(sms);
    }

}

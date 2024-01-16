package com.ourapplication.server.ourapplication.Service;

import com.ourapplication.server.ourapplication.Exception.NullValueException;
import com.ourapplication.server.ourapplication.Exception.UserAlreadyExistsException;
import com.ourapplication.server.ourapplication.Model.Users;
import com.ourapplication.server.ourapplication.Repository.UsersRepository;
import com.ourapplication.server.ourapplication.Response.LoginResponse;
import com.ourapplication.server.ourapplication.Response.NotifyTokenResponse;
import com.ourapplication.server.ourapplication.Response.UserInforResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void registerUser(Users request) {
        if(request.getEmail().equals("") || request.getPassword().equals("") || request.getName().equals("")){
            throw new NullValueException(
                    "Value is not valid to register, please try again!");
        }

        Optional<Users> user = usersRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists");
        }

        var newUser = new Users();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setNickname(request.getName());
        newUser.setActiveKey(generateActiveKey());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPushNotifyToken(request.getPushNotifyToken());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        sendEmail(newUser);
        usersRepository.save(newUser);
    }

    @Override
    public String generateActiveKey() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ActiveTime = sdf.format(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
        return ActiveTime + UUID.randomUUID();
    }

    @Override
    public void verifyAccount(String activeKey) {
        Users users = usersRepository.findByActiveKey(activeKey).orElseThrow();
        users.setEnabled(true);
        usersRepository.save(users);
    }

    @Override
    public LoginResponse login(Users request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = usersRepository.findByEmail(request.getEmail()).orElseThrow();
        return new LoginResponse(jwtService.generateToken(user));
    }

    @Override
    public UserInforResponse getUser() {

        Users user = getUserLogining();

        if(user.getIdOfPartner() != null){
            Optional<Users> partnerOfUser = usersRepository.findById(user.getIdOfPartner());
            if(partnerOfUser.isPresent()){
                return new UserInforResponse(user.getId(), user.getName(), user.getNickname(),user.getIdOfPartner(),partnerOfUser.get().getName());

            }
        }

        return new UserInforResponse(user.getId(), user.getName(), user.getNickname(),null,"");

    }

    @Override
    public void updateUser(Users newuser) {
        Users oldUser = usersRepository.findById(newuser.getId()).get();
        System.out.println(newuser.getIdOfPartner());
        oldUser.setIdOfPartner(newuser.getIdOfPartner());
        oldUser.setName(newuser.getName());
        oldUser.setNickname(newuser.getNickname());

        usersRepository.save(oldUser);
    }

    @Override
    public NotifyTokenResponse getNotifyToken() {
        Users user = getUserLogining();
        if(user.getIdOfPartner() != null){
            Optional<Users> partnerOfUser = usersRepository.findById(user.getIdOfPartner());
            if(partnerOfUser.isPresent()){
                System.out.println(partnerOfUser.get().getPushNotifyToken());
                return new NotifyTokenResponse(partnerOfUser.get().getPushNotifyToken());
            }
        }


        return null;
    }

    @Override
    public void updateNotifyToken(String token) {
        Users user = getUserLogining();
        user.setPushNotifyToken(token);
        usersRepository.save(user);
    }

    public void sendEmail(Users newUser) {
        try {
            String emailFrom = "khiemcongdinh@gmail.com";
            String subject = "Welcome to BeeClothes";

            // Tạo context Thymeleaf
            Context context = new Context();
            context.setVariable("user", newUser);

            String htmlContent = templateEngine.process("SendVerifyEmail", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailFrom);
            helper.setTo(newUser.getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Gửi email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Users getUserLogining(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Users) authentication.getPrincipal();

        }
        return null;
    }


    public void updateState(boolean state) {
        Users user = getUserLogining();
        user.setOnline(state);
        usersRepository.save(user);
    }
}

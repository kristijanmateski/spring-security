package com.example.springsecurity.web;

import com.example.springsecurity.Model.UserEntity;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import com.example.springsecurity.Model.dto.RegisterDto;
import com.example.springsecurity.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success",false);
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model,@Valid @ModelAttribute RegisterDto registerDto, BindingResult result) {
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(
                    new FieldError("registerDto", "confirmPassword", "Passwords do not match")
            );
        }

        UserEntity userEntity = userRepository.findByEmail(registerDto.getEmail());

        if(userEntity != null) {
            result.addError(
                    new FieldError("registerDto", "email", "Email is already in use")
            );
        }

        //if we have errors then will be shown and won't be creat new user
        if (result.hasErrors()) {
            return "register";
        }

        try{
            var bcrypt = new BCryptPasswordEncoder();

            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setFirstName(registerDto.getFirstName());
            newUserEntity.setLastName(registerDto.getLastName());
            newUserEntity.setEmail(registerDto.getEmail());
            newUserEntity.setPhone(registerDto.getPhone());
            newUserEntity.setAddress(registerDto.getAddress());
            newUserEntity.setRole("client");
            newUserEntity.setCreatedAt(new Date());
            newUserEntity.setPassword(bcrypt.encode(registerDto.getPassword()));
            userRepository.save(newUserEntity);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

        }catch (Exception e) {
            result.addError(
                    new FieldError("registerDto", "firstName", e.getMessage())
            );
        }

        return "register";
    }
}

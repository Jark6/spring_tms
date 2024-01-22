package com.jark.TMS.services;

import com.jark.TMS.models.Users;
import com.jark.TMS.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public Users saveUser(Users user, String confirmPassword) {
        // Проверка наличия пользователя с таким логином
        Users userFromDB = usersRepository.findByLogin(user.getLogin());
        if (userFromDB != null) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Проверка совпадения хэшей пароля и подтверждения
        if (!passwordEncoder.matches(confirmPassword, user.getPasswordHash())) {
            throw new RuntimeException("Подтверждение пароля не совпадает");
        }

        // Если хэши совпадают, сохраняем пользователя
        return usersRepository.save(user);
    }
    public Users saveUser(Users user) {
        // Проверка наличия пользователя с таким логином
        Users userFromDB = usersRepository.findByLogin(user.getLogin());
        if (userFromDB != null) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        // Если все ок, сохраняем пользователя
        return usersRepository.save(user);
    }
    public void resetPassword(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Генерация нового пароля, вы можете использовать свой способ генерации
        String newPassword = user.getLogin();

        // Хэширование и установка нового пароля
        user.setPasswordHash(newPassword);

        // Сохранение пользователя
        usersRepository.save(user);
    }

    public Users findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
}

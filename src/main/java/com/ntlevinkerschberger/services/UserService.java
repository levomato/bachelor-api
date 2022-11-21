package com.ntlevinkerschberger.services;

import com.ntlevinkerschberger.models.user.User;
import com.ntlevinkerschberger.models.user.UserDto;
import com.ntlevinkerschberger.models.user.UserMapper;
import com.ntlevinkerschberger.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class UserService {

    @Inject
    private UserRepository userRepository;
    @Inject
    private UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toDto(user);
    }

    public Optional<UserDto> findUser(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }
}

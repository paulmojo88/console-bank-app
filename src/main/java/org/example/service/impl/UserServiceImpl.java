package org.example.service.impl;

import org.example.data.dao.DaoFactory;
import org.example.data.dao.UserDao;
import org.example.data.util.ObjectMapper;
import org.example.data.dto.UserDto;
import org.example.entity.User;
import org.example.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        userDao = daoFactory.getUserDao();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userDao.getById(id);
        if (user == null) {
            return null;
        }
        return ObjectMapper.toDto(user);

    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userDao.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(ObjectMapper.toDto(user));
        }
        return userDtos;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = ObjectMapper.toEntity(userDto);
        user = userDao.create(user);
        return ObjectMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = ObjectMapper.toEntity(userDto);
        user = userDao.update(user);
        return ObjectMapper.toDto(user);
    }

    @Override
    public void deleteUser(UserDto userDto) {
        User user = ObjectMapper.toEntity(userDto);
        userDao.delete(user);
    }
}

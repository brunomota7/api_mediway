package br.com.api_mediway.mapper;

import br.com.api_mediway.dto.response.user.UserResponseDTO;
import br.com.api_mediway.entites.Role;
import br.com.api_mediway.entites.User;

import java.util.Set;

public class UserMapper {

    public static UserResponseDTO toInfosUser(User user) {
        var userId = user.getUserId();
        var name = user.getName();
        var email = user.getEmail();
        var number = user.getNumber();
        Set<Role> roles = user.getRoles();

        var userRes = new UserResponseDTO(userId, name, email, number, roles);

        return userRes;
    }

}

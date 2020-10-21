package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.UserDTO;
import se325.assignment01.concert.service.domain.User;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Users.
 */
public class UserMapper {

    public static UserDTO toDto(User user) {
        UserDTO dtoUser = new UserDTO(user.getUsername(), user.getPassword());

        return dtoUser;
    }
}

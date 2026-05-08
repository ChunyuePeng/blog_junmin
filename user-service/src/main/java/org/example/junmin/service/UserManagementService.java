package org.example.junmin.service;

import org.example.junmin.dto.RequestDTO;
import org.example.junmin.vo.Result;
import org.example.junmin.dto.UserDTO;

public interface UserManagementService {
    Result addUser(RequestDTO<Object, UserDTO> requestDTO);

    UserDTO getUserByUsername(String userName);

    Long getUserIdByUsername(String userName);
}

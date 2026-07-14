package com.ds.service;

import com.ds.dto.LoginDTO;
import com.ds.dto.RegisterDTO;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginDTO dto);

    void register(RegisterDTO dto);
}
package com.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ds.dto.LoginDTO;
import com.ds.dto.RegisterDTO;
import com.ds.entity.SysUser;
import com.ds.entity.SysUserRole;
import com.ds.entity.SysRole;
import com.ds.exception.BusinessException;
import com.ds.mapper.SysUserMapper;
import com.ds.mapper.SysRoleMapper;
import com.ds.mapper.SysUserRoleMapper;
import com.ds.security.JwtTokenProvider;
import com.ds.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(SysUserMapper userMapper, 
                           SysRoleMapper roleMapper,
                           SysUserRoleMapper userRoleMapper,
                           PasswordEncoder passwordEncoder, 
                           JwtTokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        SysUser user = userMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())
        );

        if (user == null) {
            throw new BusinessException("用户名不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        List<String> roles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId())
        ).stream()
        .map(ur -> {
            SysRole role = roleMapper.selectById(ur.getRoleId());
            return role != null ? role.getRoleKey() : null;
        })
        .filter(role -> role != null)
        .collect(Collectors.toList());

        String token = tokenProvider.generateToken(user.getUsername(), user.getId(), roles);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        return result;
    }

    @Override
    @Transactional
    public void register(RegisterDTO dto) {
        SysUser existingUser = userMapper.selectOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())
        );

        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setStatus(1);
        userMapper.insert(user);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(3L);
        userRoleMapper.insert(userRole);
    }
}
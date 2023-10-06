package com.gfinance.application.dao;

import com.gfinance.application.entity.Role;

public interface RoleDao {
    void save(Role theRole);
    Role findRoleByName(String roleName);
}

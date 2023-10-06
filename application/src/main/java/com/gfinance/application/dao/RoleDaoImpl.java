package com.gfinance.application.dao;

import com.gfinance.application.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao{

    private EntityManager entityManager;

    @Autowired
    public RoleDaoImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }
    @Override
    @Transactional
    public void save(Role theRole) {
        entityManager.persist(theRole);
    }

    @Override
    public Role findRoleByName(String roleName) {
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where rolename=:roleName", Role.class);
        theQuery.setParameter("roleName", roleName);

        Role theRole = null;

        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }

        return theRole;
    }
}

package com.gfinance.application.dao;

import com.gfinance.application.entity.Achievement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AchievementDaoImpl implements AchievementDao{
    private EntityManager entityManager;

    public AchievementDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Achievement> findAchievements() {
        TypedQuery<Achievement> query = entityManager.createQuery("select a from Achievement a", Achievement.class);
        List<Achievement> achievements = query.getResultList();
        return achievements;
    }
}

package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;

// Utility class to encapsulate all DAOs
public class DAO {
    public final FarmDAO farmDAO;
    public final PlantDAO plantDAO;
    public final FertilizerDAO fertilizerDAO;
    public final PesticideDAO pesticideDAO;
    public final UserDAO userDAO;
    public final ActivityDAO activityDAO;

    public DAO(ConnectionPool connectionPool) {
        farmDAO = new FarmDAO(connectionPool);
        plantDAO = new PlantDAO(connectionPool);
        fertilizerDAO = new FertilizerDAO(connectionPool);
        pesticideDAO = new PesticideDAO(connectionPool);
        userDAO = new UserDAO(connectionPool);
        activityDAO = new ActivityDAO(connectionPool);
    }
}

package com.example.ifarm.app;

import com.example.ifarm.counter.Counter;
import com.example.ifarm.dao.DAO;
import com.example.ifarm.dto.*;
import com.example.ifarm.logger.Logger;
import com.example.ifarm.util.Constants;
import com.example.ifarm.util.DateUtils;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class LogActivityTask implements Callable<LogActivityResponse> {
    private final LogActivityRequest req;
    private final DAO dao;
    private final Logger activityLogger;
    private final Counter activityCounter;

    public LogActivityTask(LogActivityRequest req, DAO dao, Logger activityLogger, Counter activityCounter) {
        this.req = req;
        this.dao = dao;
        this.activityLogger = activityLogger;
        this.activityCounter = activityCounter;
    }

    @Override
    public LogActivityResponse call() {
        try {
            // check if user exists
            User user = dao.userDAO.get(req.getUserId());
            if (user == null) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "UserId " + req.getUserId() + "does not exist");
            }
            // check if farm id belongs to user
            if (!user.getFarms().contains(req.getFarmId())) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "User " + req.getUserId() + "does not own farm " + req.getFarmId());
            }
            // check if farm exists
            Farm farm = dao.farmDAO.get(req.getFarmId());
            if (farm == null) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "FarmId " + req.getFarmId() + "does not exist");
            }
            // check if date is YYYY-MM-DD
            if (!DateUtils.validateDateString(req.getDate())) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid date " + req.getDate());
            }
            // check if action is valid
            if (!Constants.ACTION_LIST.contains(req.getAction())) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid action " + req.getAction());
            }
            // check if targetId is valid based on action
            if (!validateTargetId(farm, req.getAction(), req.getTargetId())) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid targetId " + req.getTargetId());
            }
            // validate field number
            if (req.getQuantity() <= 0) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid quantity " + req.getQuantity());
            }
            // validate field number
            if (req.getField() <= 0) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid field " + req.getField());
            }
            // validate row number
            if (req.getRow() <= 0) {
                return new LogActivityResponse(Constants.STATUS_INVALID_REQUEST, "Invalid row " + req.getRow());
            }

            String type = "";
            String unitType = "";
            if (req.getAction().equals(Constants.ACTION_SOWING) || req.getAction().equals(Constants.ACTION_HARVEST) || req.getAction().equals(Constants.ACTION_SALES)) {
                Plant plant = dao.plantDAO.get(req.getTargetId());
                type = plant.getName();
                unitType = plant.getUnitType();
            } else if (req.getAction().equals(Constants.ACTION_FERTILIZERS)) {
                Fertilizer fertilizer = dao.fertilizerDAO.get(req.getTargetId());
                type = fertilizer.getName();
                unitType = fertilizer.getUnitType();
            } else if (req.getAction().equals(Constants.ACTION_PESTICIDES)) {
                Pesticide pesticide = dao.pesticideDAO.get(req.getTargetId());
                type = pesticide.getName();
                unitType = pesticide.getUnitType();
            }

            String unit = getUnit(unitType, req.getUnit());

            Activity activity = new Activity(
                    Integer.toString(activityCounter.incrementAndGet()),
                    req.getFarmId(),
                    req.getUserId(),
                    req.getDate(),
                    req.getAction(),
                    type,
                    unit,
                    req.getQuantity(),
                    req.getField(),
                    req.getRow()
            );

            dao.activityDAO.create(activity);
            activityLogger.log(activity.toString());
            return new LogActivityResponse(Constants.STATUS_OK, "Activity inserted with id " + activity.getId());
        } catch (SQLException | InterruptedException e) {
            return new LogActivityResponse(Constants.STATUS_INTERNAL_ERROR, "Internal server error: " + e.getMessage());
        }
    }

    private boolean validateTargetId(Farm farm, String action, String targetId) {
        if (action.equals(Constants.ACTION_SOWING) || action.equals(Constants.ACTION_HARVEST) || action.equals(Constants.ACTION_SALES)) {
            return farm.getPlants().contains(targetId);
        }
        if (action.equals(Constants.ACTION_FERTILIZERS)) {
            return farm.getFertilizers().contains(targetId);
        }
        if (action.equals(Constants.ACTION_PESTICIDES)) {
            return farm.getPesticides().contains(targetId);
        }
        return false;
    }

    // The input from farmer is an int
    private String getUnit(String unitType, int unit) {
        return switch (unitType) {
            case Constants.UNIT_TYPE_MASS -> Constants.MASS_UNIT_LIST.get(unit % Constants.MASS_UNIT_LIST.size());
            case Constants.UNIT_TYPE_PACK -> Constants.PACK_UNIT_LIST.get(unit % Constants.PACK_UNIT_LIST.size());
            case Constants.UNIT_TYPE_VOLUME -> Constants.VOLUME_UNIT_LIST.get(unit % Constants.VOLUME_UNIT_LIST.size());
            default -> "";
        };
    }
}
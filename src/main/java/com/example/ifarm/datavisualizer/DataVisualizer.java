package com.example.ifarm.datavisualizer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DataVisualizer {
    private final String connectionUrl;
    private final String user;
    private final String password;

    public DataVisualizer(String connectionUrl, String user, String password) {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dbUsername = "root";
        String dbPassword = "wifdcfb3175";
        ArrayList<String> listOfFarmId;
        ArrayList<String> listOfFarmerId;
        ArrayList<String> listOfTargetId;

        do {
            System.out.println("\nPlease select an option");
            System.out.println("1: Display all activity logs for a target farm");
            System.out.println("2: Display all activity logs for a target farmer");
            System.out.println("3: Display all activity logs for a target farm and plant / fertilizer / pesticide");
            System.out.println("4: Display all activity logs for a target farm and plant / fertilizer / pesticide between date A and date B (inclusive)");
            System.out.println("5: Display summarized logs by plants, fertilizers and pesticides for a target farm and plant / fertilizer / pesticide between date A and date B (inclusive) for selected field and row number");
            System.out.println("0: Exit");

            try {
                int option = sc.nextInt();
                if (option == 1) {
                    System.out.println("List of farm ID: ");
                    listOfFarmId = SelectionHelper.displayFarmSelection(connectionUrl, user, password);
                    System.out.println("Please enter a farm ID");
                    int farmId = sc.nextInt();

                    if (!(listOfFarmId.contains(String.valueOf(farmId)))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    DataHelper.getRecord("farm_Id", String.valueOf(farmId), connectionUrl, user, password);
                } else if (option == 2) {
                    System.out.println("List of farmer ID: ");
                    listOfFarmerId = SelectionHelper.displayFarmerSelection(connectionUrl, user, password);
                    System.out.println("Please enter a farmer ID");
                    int farmerId = sc.nextInt();
                    if (!listOfFarmerId.contains(String.valueOf(farmerId))) {
                        throw new Exception("Please select the option from the list ");
                    }
                    DataHelper.getRecord("user_id", String.valueOf(farmerId), connectionUrl, user, password);
                } else if (option == 3) {
                    System.out.println("List of farm ID: ");
                    listOfFarmId = SelectionHelper.displayFarmSelection(connectionUrl, user, password);
                    System.out.println("Please enter a farm ID");
                    int farmId = sc.nextInt();
                    if (!listOfFarmId.contains(String.valueOf(farmId))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    System.out.println("Please select the type of material");
                    System.out.println("1: Plant");
                    System.out.println("2: Fertilizer");
                    System.out.println("3: Pesticide");
                    int type = sc.nextInt();
                    int targetId;
                    String targetType;
                    switch (type) {
                        case 1:
                            System.out.println("List of plant ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("plant", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please enter plant ID");
                            targetId = sc.nextInt();
                            targetType = "plant";
                            break;
                        case 2:
                            System.out.println("List of fertilizer ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("fertilizer", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please enter fertilizer ID");
                            targetId = sc.nextInt();
                            targetType = "fertilizer";
                            break;
                        case 3:
                            System.out.println("List of pesticide ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("pesticide", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please pesticide ID");
                            targetId = sc.nextInt();
                            targetType = "pesticide";
                            break;
                        default:
                            throw new Exception("Please select the option from 1 to 3");

                    }

                    if (!listOfTargetId.contains(String.valueOf(targetId))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    DataHelper.getRecord("farm_Id", String.valueOf(farmId), targetType, String.valueOf(targetId), connectionUrl, user, password);

                } else if (option == 4) {
                    System.out.println("List of farm ID: ");
                    listOfFarmId = SelectionHelper.displayFarmSelection(connectionUrl, user, password);
                    System.out.println("Please enter a farm ID");
                    int farmId = sc.nextInt();
                    if (!listOfFarmId.contains(String.valueOf(farmId))) {
                        throw new Exception("Please select the option from the list ");
                    }
                    System.out.println("Please select the type of material");
                    System.out.println("1: Plant");
                    System.out.println("2: Fertilizer");
                    System.out.println("3: Pesticide");

                    int type = sc.nextInt();
                    int targetId;
                    String targetType;
                    switch (type) {
                        case 1:
                            System.out.println("List of plant ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("plant", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please enter plant ID");
                            targetId = sc.nextInt();
                            targetType = "plant";
                            break;
                        case 2:
                            System.out.println("List of fertilizer ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("fertilizer", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please enter fertilizer ID");
                            targetId = sc.nextInt();
                            targetType = "fertilizer";
                            break;
                        case 3:
                            System.out.println("List of pesticide ID: ");
                            listOfTargetId = SelectionHelper.displayTargetSelection("pesticide", String.valueOf(farmId), connectionUrl, user, password);
                            System.out.println("Please pesticide ID");
                            targetId = sc.nextInt();
                            targetType = "pesticide";
                            break;
                        default:
                            throw new Exception("Please select the option from 1 to 3");

                    }

                    if (!listOfTargetId.contains(String.valueOf(targetId))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    System.out.println("Please enter the start date in YYYY-MM-DD, ie: 2021-01-01");
                    String start = sc.next();
                    System.out.println("Please enter the end date in YYYY-MM-DD, ie: 2021-01-01");
                    String end = sc.next();
                    LocalDate startDate = LocalDate.parse(start, formatter);
                    LocalDate endDate = LocalDate.parse(end, formatter);
                    System.out.println(startDate.toString());
                    if (startDate.isAfter(endDate)) {
                        throw new Exception("Start Date should be earlier than End Date.");
                    }

                    if (!listOfTargetId.contains(String.valueOf(targetId))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    DataHelper.getRecord("farm_Id", String.valueOf(farmId), targetType, String.valueOf(targetId), startDate.toString(), endDate.toString(), connectionUrl, user, password);


                } else if (option == 5) {
                    System.out.println("List of farm ID: ");
                    listOfFarmId = SelectionHelper.displayFarmSelection(connectionUrl, user, password);
                    System.out.println("Please enter a farm ID");
                    int farmId = sc.nextInt();
                    if (!listOfFarmId.contains(String.valueOf(farmId))) {
                        throw new Exception("Please select the option from the list ");
                    }

                    System.out.println("Please enter the start date in YYYY-MM-DD, ie: 2021-01-01");
                    String start = sc.next();
                    System.out.println("Please enter the end date in YYYY-MM-DD, ie: 2021-01-01");
                    String end = sc.next();
                    LocalDate startDate = LocalDate.parse(start, formatter);
                    LocalDate endDate = LocalDate.parse(end, formatter);

                    if (startDate.isAfter(endDate)) {
                        throw new Exception("Start Date should be earlier than End Date.");
                    }

                    System.out.println("Please select the field");
                    int field = sc.nextInt();
                    System.out.println("Please select the row number");
                    int row = sc.nextInt();

                    DataHelper.getSummary(String.valueOf(farmId), startDate.toString(), endDate.toString(), String.valueOf(field), String.valueOf(row), connectionUrl, user, password);

                } else if (option == 0) {
                    break;
                } else {
                    System.out.println("Please select the option from 1 to 5");
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid input.");
                sc.nextLine();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date input.Please try again.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }
}
package app.utils;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Created by: Guacamoleboy
// ________________________
// Last updated: 04/03-2026
// By: Guacamoleboy

public class ContextHelper {

    // Attributes

    // ________________________________________________________________________

    public static String checkOptionalPathParam(Context ctx, String name){
        String value = ctx.pathParam(name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    // ________________________________________________________________________

    public static String checkPathParamString(Context ctx, String name) {
        String value = ctx.pathParam(name);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing or empty path param: " + name);
        }

        return value.trim();
    }

    // ________________________________________________________________________

    public static Integer checkPathParamInt(Context ctx, String name) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        // Value check
        try {
            int input = Integer.parseInt(stringCheck);
            if (input <= 0) {
                throw new IllegalArgumentException(name + " must be greater than 0");
            }
            return input;
        } catch (NumberFormatException ne) {
            throw new IllegalArgumentException(name + " must be a valid number");
        }

    }

    // ________________________________________________________________________

    public static LocalDate checkPathParamDate(Context ctx, String name, String type) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        // Format checker
        DateTimeFormatter formatter;
        switch (type.toLowerCase()) {
            case "dd":
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                break;
            case "yyyy":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                break;
            default:
                throw new IllegalArgumentException("Invalid type parameter: " + type + ". Use 'dd' or 'yyyy'.");
        }

        // Final check
        try {
            return LocalDate.parse(stringCheck, formatter);
        } catch (DateTimeParseException de) {
            throw new IllegalArgumentException(name + " must be in " + formatter.toString() + " format");
        }

    }

    // ________________________________________________________________________

    public static Long checkPathParamLong(Context ctx, String name) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        try {
            long input = Long.parseLong(stringCheck);
            if (input <= 0) {
                throw new IllegalArgumentException(name + " must be greater than 0");
            }
            return input;
        } catch (NumberFormatException ne) {
            throw new IllegalArgumentException(name + " must be a valid long");
        }

    }

    // ________________________________________________________________________

    public static Double checkPathParamDouble(Context ctx, String name) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        try {
            return Double.parseDouble(stringCheck);
        } catch (NumberFormatException ne) {
            throw new IllegalArgumentException(name + " must be a valid double");
        }

    }

    // ________________________________________________________________________

    public static Boolean checkPathParamBoolean(Context ctx, String name) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        switch (stringCheck) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new IllegalArgumentException(name + " must be true or false");
        }

    }

    // ________________________________________________________________________

    public static LocalTime checkPathParamTime(Context ctx, String name, String type) {

        // String exception check
        String stringCheck = checkOptionalPathParam(ctx, name);
        if (stringCheck == null) {
            return null;
        }

        // Time formatting
        DateTimeFormatter formatter;
        switch (type.toLowerCase()) {
            case "hm":
                formatter = DateTimeFormatter.ofPattern("HH:mm");
                break;
            case "hms":
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                break;
            default:
                throw new IllegalArgumentException("Invalid type parameter: " + type + ". Use 'hm' or 'hms'.");
        }

        // Final check
        try {
            return LocalTime.parse(stringCheck, formatter);
        } catch (DateTimeParseException de) {
            throw new IllegalArgumentException(name + " must be in " + formatter.toString() + " format");
        }

    }

    // ________________________________________________________________________
    // Objects

    public static <T> T checkNotNull(T object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " must not be null or does not exist!");
        }
        return object;
    }

    // ________________________________________________________________________

    public static String extractBearerToken(Context ctx) {
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new HttpResponseException(401, "Missing token");
        }
        return header.substring(7);
    }

}
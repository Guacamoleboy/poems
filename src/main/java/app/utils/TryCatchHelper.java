package app.utils;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import java.util.Map;
import java.util.function.Supplier;

// Created by: Guacamoleboy
// ________________________
// Last updated: 04/03-2026
// By: Guacamoleboy

public class TryCatchHelper {

    // Attributes

    // _____________________________________________________________________________________
    // Supplier <T>
    //      - Needed instead of Runnable as we need a result in return.
    //      - Runnable only executes. Doesn't return anything.

    public static <T> void tryCatchHelper(Context ctx, Supplier<T> runnable, String successMessage){

        try {

            // Our "runnable" result saved as result for checks below.
            T result = runnable.get();

            // Object Checker
            if (result == null) {
                ctx.status(200).json(Map.of(
                "status", "success",
                "message", successMessage
                ));
            } else {
                ctx.status(200).json(Map.of(
                "status", "success",
                "message", successMessage,
                "data", result
                ));
            }

        // Anything but 200 & 500
        } catch (HttpResponseException he) {
            ctx.status(he.getStatus()).json(Map.of(
            "status", he.getStatus(),
            "message", he.getMessage()
            ));

        // Status = 500
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
            "status", "error",
            "message", e.getMessage() != null ? "Debug from TryCatchHelper.java: " + e.getMessage() : "Internal Server Error | TryCatchHelper.java"
            ));
        }

    }

    // _____________________________________________________________________________________
    // Void specific. Using Runnable instead of Supplier<T>

    public static void tryCatchHelperVoid(Context ctx, Runnable runnable, String successMessage) {
        try {
            runnable.run();
            ctx.status(200).json(Map.of(
                    "status", "success",
                    "message", successMessage
            ));
        } catch (HttpResponseException he) {
            ctx.status(he.getStatus()).json(Map.of(
                    "status", he.getStatus(),
                    "message", he.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "status", "error",
                    "message", e.getMessage() != null ? "Debug from TryCatchHelper -> Void: " + e.getMessage() : "Internal Server Error | TryCatchHelper -> Void"
            ));
        }
    }

}
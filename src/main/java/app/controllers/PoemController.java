package app.controllers;

import app.dtos.PoemDTO;
import app.services.PoemService;
import app.utils.ContextHelper;
import app.utils.TryCatchHelper;
import io.javalin.http.Context;
import java.util.List;

public class PoemController {

    // Attributes
    private final PoemService poemService;

    // ______________________________________________________________________

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    // ______________________________________________________________________  | [GET] - /poems | ______________________

    public void getPoems(Context ctx) {
        TryCatchHelper.tryCatchHelper(ctx, () ->
                poemService.getAllPoems(), "Fetched all poems successfully"
        );
    }

    // ______________________________________________________________________  | [POST] - /poems | _____________________

    public void createPoems(Context ctx) {
        TryCatchHelper.tryCatchHelper(ctx, () -> {
                    PoemDTO[] dtos = ctx.bodyAsClass(PoemDTO[].class);
                    return poemService.createPoems(List.of(dtos));
                }, "Poems created successfully"
        );
    }

    // ______________________________________________________________________  | [POST] - /poem | ______________________

    public void createPoem(Context ctx) {
        TryCatchHelper.tryCatchHelper(ctx, () -> {
                    PoemDTO dto = ctx.bodyAsClass(PoemDTO.class);
                    return poemService.createPoem(dto);
                }, "Poem created successfully"
        );
    }

    // ______________________________________________________________________  | [DELETE] - /poem/:id | ________________

    public void delete(Context ctx) {
        TryCatchHelper.tryCatchHelperVoid(ctx, () -> {
                    int id = ContextHelper.checkPathParamInt(ctx, "id");
                    poemService.deletePoemById(id);
                }, "Poem deleted successfully"
        );
    }

    // ______________________________________________________________________  | [PUT] - /poem/:id | ___________________

    public void update(Context ctx) {
        TryCatchHelper.tryCatchHelper(ctx, () -> {
                    int id = ContextHelper.checkPathParamInt(ctx, "id");
                    PoemDTO dto = ctx.bodyAsClass(PoemDTO.class);
                    return poemService.updatePoem(id, dto);
                }, "Poem updated successfully"
        );
    }

    // ______________________________________________________________________  | [GET] - /poem/:id | ___________________

    public void getById(Context ctx) {
        TryCatchHelper.tryCatchHelper(ctx, () -> {
                    int id = ContextHelper.checkPathParamInt(ctx, "id");
                    return poemService.getPoemById(id);
                }, "Fetched poem successfully"
        );
    }

}
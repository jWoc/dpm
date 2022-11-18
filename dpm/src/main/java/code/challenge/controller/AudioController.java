package code.challenge.controller;

import code.challenge.AudioManager;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.IOException;


@Controller("/audio")
public class AudioController {

    static final String BadRequestCode = "400";
    static final String FileTypeError = "Wrong audio format (Only mp3 accepted)";
    static final String ByteExtractionError = "Byte extraction error";
    static final String UnknownError = "Unknown error" ;

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public HttpResponse ping() {
        return HttpResponse.ok();
    }



    @Post
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @ApiResponse(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(type="object")),
            description = "upload audio file"
    )
    @ApiResponse(responseCode = AudioController.BadRequestCode, description = FileTypeError)
    @ApiResponse(responseCode = AudioController.BadRequestCode, description = ByteExtractionError)
    @ApiResponse(responseCode = AudioController.BadRequestCode, description = UnknownError)
    public HttpResponse<String> uploadFile(CompletedFileUpload file)  {

        try {
            if (!file.getFilename().endsWith(".mp3")) {
                return HttpResponse.badRequest(FileTypeError);
            } else {
                AudioManager audioManager = new AudioManager(file.getBytes(), file.getFilename());
                audioManager.grammarCorrectionAudio();
                return HttpResponse.ok();
            }
        } catch (IOException e) {
            return HttpResponse.badRequest( ByteExtractionError + ": " + e.getMessage() + "\n" + e.getStackTrace());
        } catch (Exception e) {
            return HttpResponse.badRequest(UnknownError + ": " + e.getMessage() + "\n" + e.getStackTrace());
        }

        // ToDo: send back corrected audio stream instead of http response ok only
    }

}

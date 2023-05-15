package cat.tecnocampus.apollofy.api;

import cat.tecnocampus.apollofy.application.DJListController;
import cat.tecnocampus.apollofy.application.dto.DJListTrackDTO;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class DJListRestController {

    private DJListController djListController;

    public DJListRestController(DJListController djListController) {
        this.djListController = djListController;
    }

     @PostMapping("/djlist/{id}/tracks")
    public void addTracksToPlayListWithTimeRange(Principal principal, @PathVariable Long id, @RequestBody List<DJListTrackDTO> tracks) {
        djListController.addTracksToPlaylistWithTimeRange(principal.getName(), id, tracks);
    }
}

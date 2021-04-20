package mpegg.gcs.genomiccontentservice.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    @PostMapping("/api/create")
    public String create(@RequestParam("dg_md") MultipartFile dg_md) {
        return "ok";
    }

    @GetMapping("/")
    public String get() {
        return "ok";
    }
}

package mpegg.gcs.genomiccontentservice.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @GetMapping("/get")
    public String get(@AuthenticationPrincipal Jwt jwt) {
        return "ok";
    }
}

package mpegg.gcs.genomiccontentservice.Controller;

import mpegg.gcs.genomiccontentservice.Models.MPEGFile;
import mpegg.gcs.genomiccontentservice.Repositories.MPEGFileRepository;
import mpegg.gcs.genomiccontentservice.Utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@RestController
public class FileController {
    final String path = "resources/storage";

    @Autowired
    private MPEGFileRepository mpegFileRepository;

    @PostMapping("/api/uploadMD")
    public String create(@AuthenticationPrincipal Jwt jwt, @RequestParam("dg_md") MultipartFile dg_md, @RequestParam("dt_mt") MultipartFile[] dt_md) {
        FileUtil f = new FileUtil();
        MPEGFile m = null;
        try {
            m = new MPEGFile((String) jwt.getClaims().get("sub"));
            mpegFileRepository.save(m);
            String newPath = path+File.separator+m.getId();
            m.setPath(newPath);
            mpegFileRepository.save(m);
            f.createDirectory(newPath);
            f.createFile(newPath+File.separator+"dg_md", new String(dg_md.getBytes()));
            int it = 0;
            for (MultipartFile dt : dt_md) {
                String datasetPath = newPath+File.separator+"dt_"+it;
                f.createDirectory(datasetPath);
                f.createFile(datasetPath+File.separator+"dt_md",new String(dt.getBytes()));
                it++;
            }
        } catch (IOException e) {
            mpegFileRepository.delete(m);
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("/api/getFiles")
    public List<MPEGFile> getFiles(@AuthenticationPrincipal Jwt jwt) {
        List<MPEGFile> a = mpegFileRepository.findByOwner((String) jwt.getClaims().get("sub"));
        return a;
    }

    @GetMapping("/get")
    public String get(@AuthenticationPrincipal Jwt jwt) {
        return "ok";
    }
}

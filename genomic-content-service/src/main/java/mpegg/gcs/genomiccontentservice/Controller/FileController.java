package mpegg.gcs.genomiccontentservice.Controller;

import mpegg.gcs.genomiccontentservice.Models.Dataset;
import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;
import mpegg.gcs.genomiccontentservice.Models.MPEGFile;
import mpegg.gcs.genomiccontentservice.Repositories.DatasetGroupRepository;
import mpegg.gcs.genomiccontentservice.Repositories.DatasetRepository;
import mpegg.gcs.genomiccontentservice.Repositories.MPEGFileRepository;
import mpegg.gcs.genomiccontentservice.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class FileController {
    final String path = "resources/storage";
    final JWTUtil j = new JWTUtil();
    final FileUtil f = new FileUtil();
    final MPEGFileUtil mUtil = new MPEGFileUtil();
    final DatasetGroupUtil dgUtil = new DatasetGroupUtil();
    final DatasetUtil dtUtil = new DatasetUtil();

    @Autowired
    private MPEGFileRepository mpegFileRepository;

    @Autowired
    private DatasetGroupRepository datasetGroupRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    @PostMapping("/createFile")
    public String createFile(@AuthenticationPrincipal Jwt jwt, @RequestParam("name") String name) {
        try {
            mUtil.addMpegFile(name,jwt,mpegFileRepository);
        } catch (Exception e) {
            e.printStackTrace();
            return "not ok";
        }
        return "ok";
    }

    @PostMapping("/addDatasetGroup")
    public String addDatasetGroup(@AuthenticationPrincipal Jwt jwt, @RequestPart("dg_md") MultipartFile dg_md, @RequestPart("dt_mt") MultipartFile[] dt_md, @RequestParam("file_id") String file_id, @RequestPart("dg_pr") MultipartFile dg_pr) {
        Long file_idL = Long.parseLong(file_id);
        Optional<MPEGFile> fileOptional = mpegFileRepository.findById(file_idL);
        MPEGFile file = null;
        if (fileOptional.isPresent()) {
            file = fileOptional.get();
        }
        else return "not ok";
        DatasetGroup dg = null;
        ArrayList<Integer> a = (ArrayList<Integer>) datasetGroupRepository.getMaxDgId(file.getId());
        int dg_id = 0;
        if (a.size() != 0 && a.get(0) != null) dg_id = a.get(0)+1;
        try {
            dg = dgUtil.addDatasetGroup(dg_md,dg_pr,file,datasetGroupRepository,dg_id);
        } catch (Exception e) {
            e.printStackTrace();
            return "not ok";
        }
        int dt_id = 0;
        for (MultipartFile dt : dt_md) {
            addDataset(jwt,dt,null,dg.getId().toString(),dt_id++);
        }
        return "ok";
    }

    @PostMapping("/addDataset")
    public String addDataset(@AuthenticationPrincipal Jwt jwt, @RequestPart("dt_mt") MultipartFile dt_md, @RequestPart("dt_pr") MultipartFile dt_pr, @RequestPart("dg_id") String dg_id, Integer dt_id) {
        Long dg_idL = Long.parseLong(dg_id);
        Optional<DatasetGroup> datasetGroupOptional = datasetGroupRepository.findById(dg_idL);
        if (datasetGroupOptional.isPresent()) {
            DatasetGroup dg = datasetGroupOptional.get();
            if (dt_id == null) {
                ArrayList<Integer> a = (ArrayList<Integer>) datasetRepository.getMaxDtId(dg.getDg_id());
                if (a.size() != 0 && a.get(0) != null) dt_id = a.get(0) + 1;
            }
            try {
                dtUtil.addDataset(dt_md, dt_pr, dg, datasetRepository, dt_id);
            } catch (Exception e) {
                e.printStackTrace();
                return "not ok";
            }
            return "ok";
        }
        return "not ok";
    }

    @PostMapping("/editDatasetGroup")
    public String editDatasetGroup(@AuthenticationPrincipal Jwt jwt, @RequestPart("dg_md") MultipartFile dg_md, @RequestPart("dg_pr") MultipartFile dg_pr, @RequestPart("dg_id") String dg_id) {
        return "Not implemented";
    }

    @PostMapping("/editDataset")
    public String editDataset(@AuthenticationPrincipal Jwt jwt, @RequestPart("dt_md") MultipartFile dt_md, @RequestPart("dt_pr") MultipartFile dt_pr, @RequestPart("dt_id") String dt_id) {
        return "Not implemented";
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@AuthenticationPrincipal Jwt jwt, @RequestParam("file_id") String file_id) {
        Long file_idL = Long.parseLong(file_id);
        Optional<MPEGFile> mpegFileOptional = mpegFileRepository.findById(file_idL);
        if (mpegFileOptional.isPresent()) {
            MPEGFile mpegfile = mpegFileOptional.get();
            if (mpegfile.getDatasetGroups() != null) {
                for (DatasetGroup dg : mpegfile.getDatasetGroups()) {
                    deleteDatasetGroup(jwt,dg.getId().toString());
                }
            }
            try {
                mUtil.deleteMpegFile(mpegfile,mpegFileRepository);
            } catch (Exception e) {
                e.printStackTrace();
                return "not ok";
            }
            return "ok";
        }
        return "not ok";
    }

    @DeleteMapping("/deleteDatasetGroup")
    public String deleteDatasetGroup(@AuthenticationPrincipal Jwt jwt, @RequestParam("dg_id") String dg_id) {
        Long dg_idL = Long.parseLong(dg_id);
        Optional<DatasetGroup> datasetGroupOptional = datasetGroupRepository.findById(dg_idL);
        if (datasetGroupOptional.isPresent()) {
            DatasetGroup dg = datasetGroupOptional.get();
            if (dg.getDatasets() != null) {
                for (Dataset dt : dg.getDatasets()) {
                    deleteDataset(jwt,dt.getId().toString());
                }
                try {
                    dgUtil.deleteDatasetGroup(dg,datasetGroupRepository);
                } catch (Exception e) {
                    return "Not ok";
                }
            }
        }
        return "ok";
    }

    @DeleteMapping("/deleteDataset")
    public String deleteDataset(@AuthenticationPrincipal Jwt jwt, @RequestParam("dt_id") String dt_id) {
        Long dt_idL = Long.parseLong(dt_id);
        Optional<Dataset> datasetOptional = datasetRepository.findById(dt_idL);
        if (datasetOptional.isPresent()) {
            Dataset dt = datasetOptional.get();
            try {
                dtUtil.deleteDataset(dt,datasetRepository);
            } catch (Exception e) {
                e.printStackTrace();
                return "Not ok";
            }
            return "ok";
        }
        return "Not ok";
    }

    @GetMapping("/ownFiles")
    public List<MPEGFile> getFiles(@AuthenticationPrincipal Jwt jwt) {
        return mpegFileRepository.findByOwner(j.getUID(jwt));
    }

    @GetMapping("/get")
    public String get(@AuthenticationPrincipal Jwt jwt) {
        return "ok";
    }
}
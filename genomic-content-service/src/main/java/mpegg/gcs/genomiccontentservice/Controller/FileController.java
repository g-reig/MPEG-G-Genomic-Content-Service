package mpegg.gcs.genomiccontentservice.Controller;

import mpegg.gcs.genomiccontentservice.Models.Dataset;
import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;
import mpegg.gcs.genomiccontentservice.Models.MPEGFile;
import mpegg.gcs.genomiccontentservice.Repositories.DatasetGroupRepository;
import mpegg.gcs.genomiccontentservice.Repositories.DatasetRepository;
import mpegg.gcs.genomiccontentservice.Repositories.MPEGFileRepository;
import mpegg.gcs.genomiccontentservice.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //Tested
    @PostMapping("/addFile")
    public ResponseEntity<String> addFile(@AuthenticationPrincipal Jwt jwt, @RequestParam("file_name") String file_name) {
        try {
            mUtil.addMpegFile(file_name,jwt,mpegFileRepository);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error creating file",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Ok",HttpStatus.OK);
    }

    //Tested
    @PostMapping("/addDatasetGroup")
    public ResponseEntity<String> addDatasetGroup(@AuthenticationPrincipal Jwt jwt, @RequestPart("dg_md") MultipartFile dg_md, @RequestPart(value = "dt_md", required = false) MultipartFile[] dt_md, @RequestParam("file_id") String file_id, @RequestPart("dg_pr") MultipartFile dg_pr) {
        Long file_idL = Long.parseLong(file_id);
        Optional<MPEGFile> fileOptional = mpegFileRepository.findById(file_idL);
        MPEGFile file = null;
        if (fileOptional.isPresent()) {
            file = fileOptional.get();
        }
        else return new ResponseEntity<String>("File doesn't exist",HttpStatus.NOT_ACCEPTABLE);
        DatasetGroup dg = null;
        ArrayList<Integer> a = (ArrayList<Integer>) datasetGroupRepository.getMaxDgId(file.getId());
        int dg_id = 0;
        if (a.size() != 0 && a.get(0) != null) dg_id = a.get(0)+1;
        try {
            dg = dgUtil.addDatasetGroup(dg_md,dg_pr,file,datasetGroupRepository,dg_id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dt_md != null) {
            int dt_id = 0;
            for (MultipartFile dt : dt_md) {
                ResponseEntity<String> r = addDataset(jwt, dt, null, dg.getId().toString(), dt_id);
                ++dt_id;
            }
        }
        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }

    //Tested
    @PostMapping("/addDataset")
    public ResponseEntity<String> addDataset(@AuthenticationPrincipal Jwt jwt, @RequestPart(value = "dt_md", required = false) MultipartFile dt_md, @RequestPart(value = "dt_pr", required = false) MultipartFile dt_pr, @RequestPart("dg_id") String dg_id, Integer dt_id) {
        Long dg_idL = Long.parseLong(dg_id);
        Optional<DatasetGroup> datasetGroupOptional = datasetGroupRepository.findById(dg_idL);
        if (datasetGroupOptional.isPresent()) {
            DatasetGroup dg = datasetGroupOptional.get();
            if (dt_id == null) {
                dt_id = 0;
                ArrayList<Integer> a = (ArrayList<Integer>) datasetRepository.getMaxDtId(dg.getId());
                if (a.size() != 0 && a.get(0) != null) dt_id = a.get(0) + 1;
            }
            try {
                dtUtil.addDataset(dt_md, dt_pr, dg, datasetRepository, dt_id);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<String>("ok",HttpStatus.OK);
        }
        return new ResponseEntity<String>("DatasetGroup doesn't exist",HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/editDatasetGroup")
    public ResponseEntity<String> editDatasetGroup(@AuthenticationPrincipal Jwt jwt, @RequestPart(value = "dg_md",required = false) MultipartFile dg_md, @RequestPart(value = "dg_pr",required = false) MultipartFile dg_pr, @RequestPart(value = "dg_id") String dg_id) {
        Long dg_idL = Long.parseLong(dg_id);
        Optional<DatasetGroup> datasetGroupOptional = datasetGroupRepository.findById(dg_idL);
        if (datasetGroupOptional.isPresent()) {
            DatasetGroup dg = datasetGroupOptional.get();
            try {
                dgUtil.editDatasetGroup(dg,dg_md,dg_pr);
                return new ResponseEntity<String>("ok",HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return new ResponseEntity<String>("DatasetGroup doesn't exist", HttpStatus.NOT_ACCEPTABLE);
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
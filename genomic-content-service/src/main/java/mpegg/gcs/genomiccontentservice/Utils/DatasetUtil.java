package mpegg.gcs.genomiccontentservice.Utils;

import mpegg.gcs.genomiccontentservice.Models.Dataset;
import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;
import mpegg.gcs.genomiccontentservice.Repositories.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DatasetUtil {

    final FileUtil f = new FileUtil();

    public Dataset addDataset(MultipartFile dt_md, MultipartFile dt_pr, DatasetGroup dg, DatasetRepository datasetRepository, Integer dt_id) throws Exception {
        Dataset d = new Dataset(dg);
        List<Dataset> datasets = dg.getDatasets();
        d.setDt_id(dt_id);
        String datasetPath = dg.getPath()+ File.separator+"dt_"+dt_id;
        try {
            f.createDirectory(datasetPath);
            d.setPath(datasetPath);
        } catch (Exception e) {
            throw new Exception("Error creating dataset");
        }

        try {
            if (dt_md != null) {
                //Falta afegir metadades a la BD.
                f.createFile(datasetPath + File.separator + "dt_md.xml", new String(dt_md.getBytes()));
            }

            if (dt_pr != null) {
                f.createFile(datasetPath+File.separator+"dt_md.xml",new String(dt_pr.getBytes()));
            }
            datasetRepository.save(d);
        } catch (Exception e) {
            try {
                f.deleteDirectory(datasetPath);
            } catch (IOException ignored) {}
            throw new Exception("Error creating dataset");
        }

        return d;
    }

    public void deleteDataset(Dataset dt, DatasetRepository datasetRepository) throws Exception {
        try {
            f.deleteDirectory(dt.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error deleting dataset");
        }
        datasetRepository.deleteById(dt.getId());
    }

    public void editDataset(Dataset dt, MultipartFile dt_md, MultipartFile dt_pr) throws Exception {
        if (dt_md != null) {
            f.updateFile(dt.getPath()+File.separator+"dt_md.xml",new String(dt_md.getBytes()));
        }
        if (dt_pr != null) {
            f.updateFile(dt.getPath()+File.separator+"dt_pr.xml",new String(dt_pr.getBytes()));
        }
    }
}
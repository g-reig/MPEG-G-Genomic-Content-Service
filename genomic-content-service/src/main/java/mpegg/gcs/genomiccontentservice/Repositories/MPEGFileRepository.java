package mpegg.gcs.genomiccontentservice.Repositories;

import mpegg.gcs.genomiccontentservice.Models.MPEGFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MPEGFileRepository extends CrudRepository<MPEGFile,Long> {
    List<MPEGFile> findByOwner(String Owner);
}

package mpegg.gcs.genomiccontentservice.Repositories;

import mpegg.gcs.genomiccontentservice.Models.MPEGFile;
import org.springframework.data.repository.CrudRepository;

public interface DatasetGroupRepository extends CrudRepository<MPEGFile,Integer> {

}

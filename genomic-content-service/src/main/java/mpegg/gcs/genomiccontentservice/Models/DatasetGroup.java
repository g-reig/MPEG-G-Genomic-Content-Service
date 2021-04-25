package mpegg.gcs.genomiccontentservice.Models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="datasetgroup")
public class DatasetGroup {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer dg_id;

    private String title;

    private String type;

    private String description;

    private String center;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mpegfile_id", nullable = false)
    private MPEGFile mpegfile;

    //@OneToMany(mappedBy = "dataset-group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //private Set<Dataset> datasets;
}

package mpegg.gcs.genomiccontentservice.Models;

import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;

import javax.persistence.*;

@Entity
@Table(name="dataset")
public class Dataset {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;

    private String taxon_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "datasetgroup_id", nullable = false)
    private DatasetGroup datasetGroup;
}

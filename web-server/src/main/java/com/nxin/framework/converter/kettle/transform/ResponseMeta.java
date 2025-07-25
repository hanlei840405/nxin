package com.nxin.framework.converter.kettle.transform;

import lombok.Data;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.step.StepMeta;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ResponseMeta implements Serializable {
    private String id;

    private StepMeta stepMeta;

    private TransHopMeta transHopMeta;

    private DatabaseMeta databaseMeta;

    private JobEntryCopy jobEntryCopy;

    private JobHopMeta jobHopMeta;

    private Set<Long> references;


    public ResponseMeta() {

    }

    public ResponseMeta(String id, TransHopMeta transHopMeta) {
        this.id = id;
        this.transHopMeta = transHopMeta;
    }

    public ResponseMeta(String id, JobHopMeta jobHopMeta) {
        this.id = id;
        this.jobHopMeta = jobHopMeta;
    }

    public ResponseMeta(String id, StepMeta stepMeta, DatabaseMeta databaseMeta, Set<Long> references) {
        this.id = id;
        this.stepMeta = stepMeta;
        this.databaseMeta = databaseMeta;
        this.references = references;
    }

    public ResponseMeta(String id, JobEntryCopy jobEntryCopy, DatabaseMeta databaseMeta, Set<Long> references) {
        this.id = id;
        this.jobEntryCopy = jobEntryCopy;
        this.databaseMeta = databaseMeta;
        this.references = references;
    }
}

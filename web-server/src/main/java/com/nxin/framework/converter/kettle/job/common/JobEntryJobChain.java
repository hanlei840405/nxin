package com.nxin.framework.converter.kettle.job.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.ObjectLocationSpecificationMethod;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.job.JobEntryJob;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JobEntryJobChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntryJob".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String name = (String) formAttributes.get("name");
            Number shellId = (Number) formAttributes.get("shellId");
            List<String> arguments = new ArrayList<>(0);
            List<String> parameters = new ArrayList<>(0);
            List<String> parameterFieldNames = new ArrayList<>(0);
            List<String> parameterValues = new ArrayList<>(0);
            JobEntryJob jobEntryJob = new JobEntryJob(name);
            jobEntryJob.setSpecificationMethod(ObjectLocationSpecificationMethod.FILENAME);
            if (formAttributes.containsKey("executeEachRow")) {
                jobEntryJob.execPerRow = (boolean) formAttributes.get("executeEachRow");
            }
            if (formAttributes.containsKey("expandRemoteOnSlave")) {
                jobEntryJob.expandingRemoteJob = (boolean) formAttributes.get("expandRemoteOnSlave");
            }
            if (formAttributes.containsKey("passingExport")) {
                jobEntryJob.setPassingExport((boolean) formAttributes.get("passingExport"));
            }
            if (formAttributes.containsKey("waitRemoteFinished")) {
                jobEntryJob.waitingToFinish = (boolean) formAttributes.get("waitRemoteFinished");
            }
            if (formAttributes.containsKey("followingAbortRemotely")) {
                jobEntryJob.followingAbortRemotely = (boolean) formAttributes.get("followingAbortRemotely");
            }
            List<Map<String, String>> resultArguments = (List<Map<String, String>>) formAttributes.get("resultArguments");
            for (Map<String, String> resultArgument : resultArguments) {
                arguments.add(resultArgument.get("argument"));
            }
            if (formAttributes.containsKey("argFromPrevious")) {
                jobEntryJob.argFromPrevious = (boolean) formAttributes.get("argFromPrevious");
            }
            if (!arguments.isEmpty()) {
                jobEntryJob.arguments = arguments.toArray(new String[0]);
            } else {
                jobEntryJob.arguments = new String[0];
            }
            List<Map<String, String>> nameArguments = (List<Map<String, String>>) formAttributes.get("nameArguments");
            for (Map<String, String> nameArgument : nameArguments) {
                parameters.add(nameArgument.get("name"));
                parameterFieldNames.add(nameArgument.get("field"));
                parameterValues.add(nameArgument.get("value"));
            }
            if (formAttributes.containsKey("paramsFromPrevious")) {
                jobEntryJob.paramsFromPrevious = (boolean) formAttributes.get("paramsFromPrevious");
            }
            if (formAttributes.containsKey("passingAllParameters")) {
                jobEntryJob.passingAllParameters = (boolean) formAttributes.get("passingAllParameters");
            }
            if (!parameters.isEmpty()) {
                jobEntryJob.parameters = parameters.toArray(new String[0]);
                jobEntryJob.parameterFieldNames = parameterFieldNames.toArray(new String[0]);
                jobEntryJob.parameterValues = parameterValues.toArray(new String[0]);
            } else {
                jobEntryJob.parameters = new String[0];
                jobEntryJob.parameterFieldNames = new String[0];
                jobEntryJob.parameterValues = new String[0];
            }
            Shell jobShell = getShellService().one(shellId.longValue());
            if (jobShell.getExecutable()) {
                String dir = jobMeta.getVariable(Constant.SHELL_STORAGE_DIR);
                jobEntryJob.setFileName(dir + jobShell.getProjectId() + File.separator + jobShell.getParentId() + File.separator + jobShell.getId() + Constant.DOT + Constant.JOB_SUFFIX);
            }
            jobEntryJob.getXML();
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntryJob);
            mxGeometry geometry = cell.getGeometry();
            jobEntryCopy.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            jobEntryCopy.setDrawn();
            if (formAttributes.containsKey(Constant.ETL_PARALLEL)) {
                boolean parallel = (boolean) formAttributes.get(Constant.ETL_PARALLEL);
                jobEntryCopy.setLaunchingInParallel(parallel);
            }
            Set<Long> references = new HashSet<>(0);
            if (StringUtils.hasLength(jobShell.getReference())) {
                Arrays.stream(jobShell.getReference().split(",")).forEach(id -> {
                    references.add(Long.parseLong(id));
                });
            }
            references.add(jobShell.getId());
            return new ResponseMeta(cell.getId(), jobEntryCopy, null, references);
        } else {
            return next.parse(cell, jobMeta);
        }
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}

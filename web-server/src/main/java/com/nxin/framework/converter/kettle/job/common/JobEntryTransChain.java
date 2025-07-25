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
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JobEntryTransChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntryTrans".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String name = (String) formAttributes.get("name");
            Number shellId = (Number) formAttributes.get("shellId");
            List<String> arguments = new ArrayList<>(0);
            List<String> parameters = new ArrayList<>(0);
            List<String> parameterFieldNames = new ArrayList<>(0);
            List<String> parameterValues = new ArrayList<>(0);
            JobEntryTrans jobEntryTrans = new JobEntryTrans(name);
            jobEntryTrans.setSpecificationMethod(ObjectLocationSpecificationMethod.FILENAME);
            if (formAttributes.containsKey("executeEachRow")) {
                jobEntryTrans.execPerRow = (boolean) formAttributes.get("executeEachRow");
            }
            if (formAttributes.containsKey("clearRecords")) {
                jobEntryTrans.clearResultRows = (boolean) formAttributes.get("clearRecords");
            }
            if (formAttributes.containsKey("clearFileRecords")) {
                jobEntryTrans.clearResultFiles = (boolean) formAttributes.get("clearFileRecords");
            }
            if (formAttributes.containsKey("waitRemoteFinished")) {
                jobEntryTrans.waitingToFinish = (boolean) formAttributes.get("waitRemoteFinished");
            }
            if (formAttributes.containsKey("notice")) {
                jobEntryTrans.followingAbortRemotely = (boolean) formAttributes.get("notice");
            }

            List<Map<String, String>> resultArguments = (List<Map<String, String>>) formAttributes.get("resultArguments");
            for (Map<String, String> resultArgument : resultArguments) {
                arguments.add(resultArgument.get("argument"));
            }
            if (formAttributes.containsKey("argFromPrevious")) {
                jobEntryTrans.argFromPrevious = (boolean) formAttributes.get("argFromPrevious");
            }
            if (!arguments.isEmpty()) {
                jobEntryTrans.arguments = arguments.toArray(new String[0]);
            } else {
                jobEntryTrans.arguments = new String[0];
            }
            List<Map<String, String>> nameArguments = (List<Map<String, String>>) formAttributes.get("nameArguments");
            for (Map<String, String> nameArgument : nameArguments) {
                parameters.add(nameArgument.get("name"));
                parameterFieldNames.add(nameArgument.get("field"));
                parameterValues.add(nameArgument.get("value"));
            }
            if (formAttributes.containsKey("paramsFromPrevious")) {
                jobEntryTrans.paramsFromPrevious = (boolean) formAttributes.get("paramsFromPrevious");
            }
            if (formAttributes.containsKey("passingAllParameters")) {
                jobEntryTrans.setPassingAllParameters((boolean) formAttributes.get("passingAllParameters"));
            }
            if (!parameters.isEmpty()) {
                jobEntryTrans.parameters = parameters.toArray(new String[0]);
                jobEntryTrans.parameterFieldNames = parameterFieldNames.toArray(new String[0]);
                jobEntryTrans.parameterValues = parameterValues.toArray(new String[0]);
            } else {
                jobEntryTrans.parameters = new String[0];
                jobEntryTrans.parameterFieldNames = new String[0];
                jobEntryTrans.parameterValues = new String[0];
            }
            Shell transformShell = getShellService().one(shellId.longValue());
            if (transformShell.getExecutable()) {
                String dir = jobMeta.getVariable(Constant.SHELL_STORAGE_DIR);
                jobEntryTrans.setFileName(dir + transformShell.getProjectId() + File.separator + transformShell.getParentId() + File.separator + transformShell.getId() + Constant.DOT + Constant.TRANS_SUFFIX);
            }
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntryTrans);
            mxGeometry geometry = cell.getGeometry();
            jobEntryCopy.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            jobEntryCopy.setDrawn();
            if (formAttributes.containsKey(Constant.ETL_PARALLEL)) {
                boolean parallel = (boolean) formAttributes.get(Constant.ETL_PARALLEL);
                jobEntryCopy.setLaunchingInParallel(parallel);
            }
            Set<Long> references = new HashSet<>(0);
            if (StringUtils.hasLength(transformShell.getReference())) {
                Arrays.stream(transformShell.getReference().split(",")).forEach(id -> {
                    references.add(Long.parseLong(id));
                });
            }
            references.add(transformShell.getId());
            return new ResponseMeta(cell.getId(), jobEntryCopy, null, references);
        } else {
            return next.parse(cell, jobMeta);
        }
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}

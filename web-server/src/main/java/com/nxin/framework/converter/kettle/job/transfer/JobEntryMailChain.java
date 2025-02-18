package com.nxin.framework.converter.kettle.job.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.mail.JobEntryMail;
import org.pentaho.di.job.entry.JobEntryCopy;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JobEntryMailChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntryMail".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String name = (String) formAttributes.get("name");
            String destination = (String) formAttributes.get("destination");
            String destinationCc = (String) formAttributes.get("destinationCc");
            String destinationBCc = (String) formAttributes.get("destinationBCc");
            String replyAddress = (String) formAttributes.get("replyAddress");
            String replyName = (String) formAttributes.get("replyName");
            String replyToAddresses = (String) formAttributes.get("replyToAddresses");
            String contactPerson = (String) formAttributes.get("contactPerson");
            String contactPhone = (String) formAttributes.get("contactPhone");
            String server = (String) formAttributes.get("server");
            String port = (String) formAttributes.get("port");
            boolean usingAuthentication = (boolean) formAttributes.get("usingAuthentication");
            boolean usingSecureAuthentication = (boolean) formAttributes.get("usingSecureAuthentication");
            boolean includeDate = (boolean) formAttributes.get("includeDate");
            boolean useHTML = (boolean) formAttributes.get("useHTML");
            boolean includingFiles = (boolean) formAttributes.get("includingFiles");
            boolean onlySendComment = (boolean) formAttributes.get("onlySendComment");
            String authenticationUser = (String) formAttributes.get("authenticationUser");
            String authenticationPassword = (String) formAttributes.get("authenticationPassword");
            String secureConnectionType = (String) formAttributes.get("secureConnectionType");
            String subject = (String) formAttributes.get("subject");
            String comment = (String) formAttributes.get("comment");
            JobEntryMail jobEntryMail = new JobEntryMail();
            jobEntryMail.setName(name);
            jobEntryMail.setDestination(destination);
            jobEntryMail.setDestinationCc(destinationCc);
            jobEntryMail.setDestinationBCc(destinationBCc);
            jobEntryMail.setReplyAddress(replyAddress);
            jobEntryMail.setReplyName(replyName);
            jobEntryMail.setReplyToAddresses(replyToAddresses);
            jobEntryMail.setContactPerson(contactPerson);
            jobEntryMail.setContactPhone(contactPhone);
            jobEntryMail.setServer(server);
            jobEntryMail.setPort(port);
            jobEntryMail.setUsingAuthentication(usingAuthentication);
            jobEntryMail.setUsingSecureAuthentication(usingSecureAuthentication);
            jobEntryMail.setIncludeDate(includeDate);
            jobEntryMail.setUseHTML(useHTML);
            jobEntryMail.setIncludingFiles(includingFiles);
            jobEntryMail.setOnlySendComment(onlySendComment);
            jobEntryMail.setAuthenticationUser(authenticationUser);
            jobEntryMail.setAuthenticationPassword(authenticationPassword);
            jobEntryMail.setSecureConnectionType(secureConnectionType);
            jobEntryMail.setSubject(subject);
            jobEntryMail.setComment(comment);
            jobEntryMail.setFileType(new int[]{ResultFile.FILE_TYPE_GENERAL});
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntryMail);
            mxGeometry geometry = cell.getGeometry();
            jobEntryCopy.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            jobEntryCopy.setDrawn();
            if (formAttributes.containsKey(Constant.ETL_PARALLEL)) {
                boolean parallel = (boolean) formAttributes.get(Constant.ETL_PARALLEL);
                jobEntryCopy.setLaunchingInParallel(parallel);
            }
            return new ResponseMeta(cell.getId(), jobEntryCopy, null, null);
        } else {
            return next.parse(cell, jobMeta);
        }
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}

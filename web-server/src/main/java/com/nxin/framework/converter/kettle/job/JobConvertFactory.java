package com.nxin.framework.converter.kettle.job;

import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.common.*;
import com.nxin.framework.converter.kettle.job.condition.JobEntrySimpleEvalChain;
import com.nxin.framework.converter.kettle.job.shell.JobEntryEvalChain;
import com.nxin.framework.converter.kettle.job.transfer.JobEntryFTPPutChain;
import com.nxin.framework.converter.kettle.job.transfer.JobEntryMailChain;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.kettle.ShellService;

public class JobConvertFactory extends ConvertFactory {
    private static JobConvertChain beginChain;

    public static void init(ShellService shellService, FtpService ftpService, String attachmentDir) {
        JobConvertChain beginChain = new BeginChain();
        JobConvertChain jobEntrySpecialChain = new JobEntrySpecialChain();
        JobConvertChain jobEntryDummyChain = new JobEntryDummyChain();
        JobConvertChain jobEntryTransChain = new JobEntryTransChain();
        JobConvertChain jobEntryJobChain = new JobEntryJobChain();
        JobConvertChain jobEntrySuccessChain = new JobEntrySuccessChain();
        JobConvertChain jobEntrySetVariablesChain = new JobEntrySetVariablesChain();
        JobConvertChain jobEntryEvalChain = new JobEntryEvalChain();
        JobConvertChain jobEntrySimpleEvalChain = new JobEntrySimpleEvalChain();
        JobEntryFTPPutChain jobEntryFTPPutChain = new JobEntryFTPPutChain();
        JobEntryMailChain jobEntryMailChain = new JobEntryMailChain();
        JobConvertChain jobHopChain = new JobHopChain();
        JobConvertChain endChain = new EndChain();
        jobEntryTransChain.setShellService(shellService);
        jobEntryJobChain.setShellService(shellService);
        jobEntryFTPPutChain.setShellService(shellService);
        jobEntryFTPPutChain.setFtpService(ftpService);
        jobEntryFTPPutChain.getJobVariable().put(Constant.VAR_ATTACHMENT_DIR, attachmentDir);
        beginChain.setNext(jobEntrySpecialChain);
        jobEntrySpecialChain.setNext(jobEntryDummyChain);
        jobEntryDummyChain.setNext(jobEntryTransChain);
        jobEntryTransChain.setNext(jobEntryJobChain);
        jobEntryJobChain.setNext(jobEntrySuccessChain);
        jobEntrySuccessChain.setNext(jobEntrySetVariablesChain);
        jobEntrySetVariablesChain.setNext(jobEntryEvalChain);
        jobEntryEvalChain.setNext(jobEntrySimpleEvalChain);
        jobEntrySimpleEvalChain.setNext(jobEntryFTPPutChain);
        jobEntryFTPPutChain.setNext(jobEntryMailChain);
        jobEntryMailChain.setNext(jobHopChain);
        jobHopChain.setNext(endChain);
        JobConvertFactory.beginChain = beginChain;
    }


    public static JobConvertChain getInstance() {
        return beginChain;
    }
}

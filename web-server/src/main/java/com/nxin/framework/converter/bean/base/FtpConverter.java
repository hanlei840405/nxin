package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.vo.basic.FtpVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FtpConverter extends BeanConverter<FtpVo, Ftp> {

    @Override
    public FtpVo convert(Ftp ftp) {
        FtpVo ftpVo = new FtpVo();
        BeanUtils.copyProperties(ftp, ftpVo);
        return ftpVo;
    }

    @Override
    public List<FtpVo> convert(List<Ftp> ftpList) {
        return ftpList.stream().map(this::convert).collect(Collectors.toList());
    }
}

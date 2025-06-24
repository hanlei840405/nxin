package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.vo.basic.FtpVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FtpConverter extends BeanConverter<FtpVo, Ftp> {

    @Override
    public FtpVo convert(Ftp ftp, String... ignores) {
        FtpVo ftpVo = new FtpVo();
        BeanUtils.copyProperties(ftp, ftpVo, ignores);
        return ftpVo;
    }

    @Override
    public List<FtpVo> convert(List<Ftp> ftpList, String... ignores) {
        return ftpList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}

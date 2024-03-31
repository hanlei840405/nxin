package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.vo.basic.DatasourceVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DatasourceConverter extends BeanConverter<DatasourceVo, Datasource> {

    @Override
    public DatasourceVo convert(Datasource shell) {
        DatasourceVo datasourceVo = new DatasourceVo();
        BeanUtils.copyProperties(shell, datasourceVo);
        return datasourceVo;
    }

    @Override
    public List<DatasourceVo> convert(List<Datasource> datasourceList) {
        return datasourceList.stream().map(this::convert).collect(Collectors.toList());
    }
}

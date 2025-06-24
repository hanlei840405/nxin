package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.vo.basic.DatasourceVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DatasourceConverter extends BeanConverter<DatasourceVo, Datasource> {

    @Override
    public DatasourceVo convert(Datasource datasource, String... ignores) {
        DatasourceVo datasourceVo = new DatasourceVo();
        BeanUtils.copyProperties(datasource, datasourceVo, ignores);
        return datasourceVo;
    }

    @Override
    public List<DatasourceVo> convert(List<Datasource> datasourceList, String... ignores) {
        return datasourceList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}

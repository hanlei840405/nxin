package com.nxin.framework.converter.kettle.transform.output;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.textfileoutput.TextFileField;
import org.pentaho.di.trans.steps.textfileoutput.TextFileOutputMeta;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class TextFileOutputChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "TextFileOutputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            TextFileOutputMeta textFileOutputMeta = new TextFileOutputMeta();
            String stepName = (String) formAttributes.get("name");
            String filename = (String) formAttributes.get("filename");
            String suffix = (String) formAttributes.get("suffix");
            boolean addStepNr = (boolean) formAttributes.get("addStepNr");
            boolean addPartNr = (boolean) formAttributes.get("addPartNr");
            boolean addDate = (boolean) formAttributes.get("addDate");
            boolean addTime = (boolean) formAttributes.get("addTime");
            boolean addFileToResult = (boolean) formAttributes.get("addFileToResult");
            boolean append = (boolean) formAttributes.get("append");
            String separator = (String) formAttributes.get("separator");
            String enclosure = (String) formAttributes.get("enclosure");
            boolean enclForced = (boolean) formAttributes.get("enclForced");
            boolean disableEnclosureFix = (boolean) formAttributes.get("disableEnclosureFix");
            boolean header = (boolean) formAttributes.get("header");
            boolean footer = (boolean) formAttributes.get("footer");
            String compression = (String) formAttributes.get("compression");
            String encoding = (String) formAttributes.get("encoding");
            boolean pad = (boolean) formAttributes.get("pad");
            boolean fastDump = (boolean) formAttributes.get("fastDump");
            String endedLine = (String) formAttributes.get("endedLine");
            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);

            if (!filename.startsWith(Constant.FILE_SEPARATOR)) {
                filename = Constant.FILE_SEPARATOR + filename;
            }
            textFileOutputMeta.setDoNotOpenNewFileInit(true);
            textFileOutputMeta.setExtension(suffix);
            textFileOutputMeta.setStepNrInFilename(addStepNr);
            textFileOutputMeta.setPartNrInFilename(addPartNr);
            textFileOutputMeta.setDateInFilename(addDate);
            textFileOutputMeta.setTimeInFilename(addTime);
            textFileOutputMeta.setAddToResultFiles(addFileToResult);
            textFileOutputMeta.setFileAppended(append);
            textFileOutputMeta.setSeparator(separator);
            textFileOutputMeta.setEnclosure(enclosure);
            textFileOutputMeta.setEnclosureForced(enclForced);
            textFileOutputMeta.setEnclosureFixDisabled(disableEnclosureFix);
            textFileOutputMeta.setHeaderEnabled(header);
            textFileOutputMeta.setFooterEnabled(footer);
            textFileOutputMeta.setFileCompression(compression);
            textFileOutputMeta.setEncoding(encoding);
            textFileOutputMeta.setPadded(pad);
            textFileOutputMeta.setFastDump(fastDump);
            textFileOutputMeta.setEndedLine(endedLine);
            textFileOutputMeta.setFileFormat("UNIX");

            // 本地目录，路径：~/attachment/{projectId}/{脚本所在目录ID}/{脚本ID}
            Shell shell = JSON.parseObject(transMeta.getVariable(Constant.SHELL_INFO), Shell.class);
            String exportDir = ConvertFactory.getVariable().get(Constant.VAR_ATTACHMENT_DIR).toString() + shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + cell.getId();

            LambdaQueryWrapper<AttachmentStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttachmentStorage::getShellId, shell.getId());
            queryWrapper.eq(AttachmentStorage::getComponent, cell.getStyle());
            queryWrapper.eq(AttachmentStorage::getComponentId, cell.getId());
            AttachmentStorage attachmentStorage = getAttachmentStorageService().getOne(queryWrapper);
            if (attachmentStorage == null) {
                attachmentStorage = new AttachmentStorage();
                attachmentStorage.setStatus(Constant.ACTIVE);
                attachmentStorage.setVersion(1);
            }
            attachmentStorage.setProjectId(shell.getProjectId());
            attachmentStorage.setShellId(shell.getId());
            attachmentStorage.setShellName(shell.getName());
            attachmentStorage.setShellParentId(shell.getParentId());
            attachmentStorage.setComponent(cell.getStyle());
            attachmentStorage.setComponentId(cell.getId());
            attachmentStorage.setCategory(Constant.ATTACHMENT_CATEGORY_EXPORT);
            attachmentStorage.setStorageDir(exportDir);
            attachmentStorage.setStorageDirRelative(Constant.FILE_SEPARATOR + shell.getProjectId() + Constant.FILE_SEPARATOR + shell.getParentId() + Constant.FILE_SEPARATOR + shell.getId() + Constant.FILE_SEPARATOR + cell.getId());
            getAttachmentStorageService().saveOrUpdate(attachmentStorage);

            textFileOutputMeta.setFileName(exportDir + filename);
            List<Map<String, String>> parameters = (List<Map<String, String>>) formAttributes.get("parameters");
            List<TextFileField> textFileFields = new ArrayList<>();
            for (Map<String, String> parameter : parameters) {
                TextFileField textFileField = new TextFileField();
                textFileField.setName(parameter.get("field"));
                textFileField.setType(parameter.get("category"));
                textFileField.setFormat(parameter.get("format"));
                if (!ObjectUtils.isEmpty(parameter.get("lengthValue"))) {
                    textFileField.setLength(Integer.parseInt(parameter.get("lengthValue")));
                } else {
                    textFileField.setLength(-1);
                }
                if (!ObjectUtils.isEmpty(parameter.get("accuracy"))) {
                    textFileField.setPrecision(Integer.parseInt(parameter.get("accuracy")));
                } else {
                    textFileField.setPrecision(-1);
                }
                textFileField.setCurrencySymbol(parameter.get("currency"));
                textFileField.setDecimalSymbol(parameter.get("decimal"));
                textFileField.setGroupingSymbol(parameter.get("groupBy"));
                textFileField.setNullString(parameter.get("emptyValue"));
                textFileField.setTrimTypeByDesc(parameter.get("trimType"));
                textFileField.setTrimType(ValueMetaString.getTrimTypeByCode(parameter.get("trimType")));
                textFileFields.add(textFileField);
            }
            textFileOutputMeta.setOutputFields(textFileFields.toArray(new TextFileField[0]));
            StepMeta stepMeta = new StepMeta(stepName, textFileOutputMeta);
            stepMeta.setCopies(parallel);
            mxGeometry geometry = cell.getGeometry();
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            return new ResponseMeta(cell.getId(), stepMeta, null, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}

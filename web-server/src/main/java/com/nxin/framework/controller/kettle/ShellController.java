package com.nxin.framework.controller.kettle;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.kettle.ShellConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.kettle.ShellDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.ConvertException;
import com.nxin.framework.exception.FileNotExistedException;
import com.nxin.framework.exception.RecordsNotMatchException;
import com.nxin.framework.exception.XmlParseException;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.ShellPublishService;
import com.nxin.framework.service.kettle.ShellService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.kettle.ShellVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class ShellController {
    @Autowired
    private ShellService shellService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShellPublishService shellPublishService;
    private BeanConverter<ShellVo, Shell> shellConverter = new ShellConverter();

    @GetMapping("/shell/{id}")
    public ResponseEntity<ShellVo> one(@PathVariable Long id) {
        Shell persisted = shellService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                ShellVo shellVo = shellConverter.convert(persisted);
                return ResponseEntity.ok(shellVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @GetMapping("/shell/content/{id}")
    public ResponseEntity<String> content(@PathVariable Long id) throws IOException {
        Shell persisted = shellService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                return ResponseEntity.ok(fileService.content(Constant.ENV_DEV, persisted.getProjectId() + File.separator + persisted.getParentId() + File.separator + persisted.getId() + Constant.DOT + Constant.GRAPH_SUFFIX));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @GetMapping("/shells/{id}")
    public ResponseEntity<List<ShellVo>> shells(@PathVariable Long id) {
        List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
            List<Shell> shells = shellService.all(id, null);
            return ResponseEntity.ok(shellConverter.convert(shells));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @GetMapping("/shells/{projectId}/{parentId}")
    public ResponseEntity<List<ShellVo>> shells(@PathVariable Long projectId, @PathVariable Long parentId) {
        List<User> members = userService.findByResource(projectId.toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
            List<Shell> shells = shellService.all(projectId, parentId);
            return ResponseEntity.ok(shellConverter.convert(shells));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/shell/publish")
    public ResponseEntity<ShellVo> publish(@RequestBody CrudDto payload) {
        Shell persisted = shellService.one(payload.getId());
        List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
            if (persisted.getExecutable()) {
                try {
                    shellPublishService.save(persisted, payload.getPayload());
                    ShellVo shellVo = shellConverter.convert(persisted);
                    return ResponseEntity.ok(shellVo);
                } catch (RecordsNotMatchException | IOException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_RECORDS_NOT_MATCH).build();
                } catch (FileNotExistedException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_FILE_NOT_EXIST).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_ETL_GRAMMAR).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/shell")
    public ResponseEntity<?> save(@RequestBody ShellDto shellDto) {
        List<User> members = userService.findByResource(shellDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
            Shell shell = new Shell();
            BeanUtils.copyProperties(shellDto, shell, "xml");
            try {
                shellService.save(shell);
                return ResponseEntity.ok().build();
            } catch (DuplicateKeyException e) {
                return ResponseEntity.status(Constant.EXCEPTION_DUPLICATED).build();
            } catch (XmlParseException e) {
                return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
            } catch (ConvertException e) {
                return ResponseEntity.status(Constant.EXCEPTION_PLUGIN_CONVERT).body(e.getMessage());
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/moveShells/{parentId}")
    public ResponseEntity<?> save(@PathVariable Long parentId, @RequestBody List<ShellDto> shellDtoList) {
        Shell parent = null;
        if (parentId > 0) {
            parent = shellService.one(parentId);
            List<User> parentMembers = userService.findByResource(parent.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (parentMembers.stream().noneMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        List<Shell> shells = shellService.listByIds(shellDtoList.stream().map(ShellDto::getId).collect(Collectors.toList()));
        List<Long> idList = new ArrayList<>();
        for (Shell shell : shells) {
            if (parent != null && !Objects.equals(shell.getProjectId(), parent.getProjectId())) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
            List<User> members = userService.findByResource(shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().noneMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
            idList.add(shell.getId());
        }
        shellService.move(parentId, idList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shell/content")
    public ResponseEntity<?> content(@RequestBody ShellDto shellDto) {
        if (shellDto.getId() != null) {
            Shell persisted = shellService.one(shellDto.getId());
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                persisted.setContent(shellDto.getContent());
                try {
                    shellService.save(persisted);
                } catch (XmlParseException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
                } catch (ConvertException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_PLUGIN_CONVERT).body(e.getMessage());
                }
                return ResponseEntity.ok(shellConverter.convert(persisted));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @DeleteMapping("/shell/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Shell persisted = shellService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                shellService.delete(persisted.getProjectId(), Collections.singletonList(persisted.getId()));
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
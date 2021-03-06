package com.lzpeng.project.sys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lzpeng.framework.web.service.LeftTreeRightTableServiceImpl;
import com.lzpeng.project.sys.domain.Department;
import com.lzpeng.project.sys.domain.Position;
import com.lzpeng.project.sys.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 岗位 抽象业务层 提供基于注解的缓存配置
 * @date: 2020-3-22
 * @time: 10:56:59
 * @author: 李志鹏
 */
@Service
@Transactional(rollbackOn = Exception.class)
public abstract class AbstractPositionService extends LeftTreeRightTableServiceImpl<Department, Position> {

    protected static final String ENTITY_NAME = "com.lzpeng.project.sys.domain.Position";

    protected PositionRepository positionRepository;

    protected DepartmentService departmentService;

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.baseRepository = positionRepository;
        this.leftTreeRightTableRepository = positionRepository;
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.treeService = departmentService;
        this.departmentService = departmentService;
    }

    @Override
    @CachePut(value = ENTITY_NAME, key = "#result.id", unless = "#result == null")
    public Position save(Position position) {
        return super.save(position);
    }

    @Override
    @CacheEvict(value = ENTITY_NAME, key = "#id")
    public void delete(String id) {
        super.delete(id);
    }

    @Override
    @CachePut(value = ENTITY_NAME, key = "#id", unless = "#result == null")
    public Position update(String id, Position model) {
        return super.update(id, model);
    }

    @Override
    @Cacheable(value = ENTITY_NAME, key = "#id", unless = "#result == null")
    public Position findById(String id) {
        return super.findById(id);
    }

    /**
     * 从 json 读取数据
     * 必须重写此方法否则,TypeReference获取不到泛型参数
     * @param json
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public List<Position> readDataFromJson(String json) throws JsonProcessingException {
        List<Position> list = objectMapper.readValue(json, new TypeReference<List<Position>>() {
        });
        return list;
    }

}

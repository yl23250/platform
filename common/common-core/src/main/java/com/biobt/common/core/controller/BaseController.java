package com.biobt.common.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.common.core.domain.ApiResponse;
import com.biobt.common.core.domain.PageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public abstract class BaseController<T, V> {

    protected final IService<T> service;
    protected final Function<T, V> converter;

    public BaseController(IService<T> service, Function<T, V> converter) {
        this.service = service;
        this.converter = converter;
    }


    protected ApiResponse<PageResponse<V>> pageQuery(
            @RequestBody(required = false) T query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {

        LambdaQueryWrapper<T> wrapper = buildAdvancedQuery(query);

        IPage<T> page = service.page(new Page<>(pageNum, pageSize), wrapper);
        List<V> result = page.getRecords().stream().map(converter).collect(Collectors.toList());

        return ApiResponse.success(PageResponse.of(result, page.getTotal(), (long) pageNum, (long) pageSize));
    }

    protected LambdaQueryWrapper<T> buildAdvancedQuery(T query) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        // 默认实现为空，留给子类重写（或者你可以放在 Service 层实现 QueryBuilder）
        return wrapper;
    }


    @GetMapping("/{id}")
    public ApiResponse<T> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> removeById(@PathVariable Long id) {
        return ApiResponse.success(service.removeById(id));
    }

    @PostMapping
    public ApiResponse<Boolean> saveOrUpdate(@RequestBody T data) {
        return ApiResponse.success(service.saveOrUpdate(data));
    }
}

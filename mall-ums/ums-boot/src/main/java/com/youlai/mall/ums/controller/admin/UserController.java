package com.youlai.mall.ums.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youlai.common.constant.GlobalConstants;
import com.youlai.common.enums.QueryModeEnum;
import com.youlai.common.result.Result;
import com.youlai.mall.ums.pojo.domain.UmsUser;
import com.youlai.mall.ums.service.IUmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Api(tags = "【系统管理】会员管理")
@RestController("AdminUserController")
@RequestMapping("/api.admin/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private IUmsUserService iUmsUserService;

    @ApiOperation(value = "列表分页", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryMode", paramType = "query", dataType = "QueryModeEnum"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "nickname", value = "会员昵称", paramType = "query", dataType = "String")
    })
    @GetMapping
    public Result list(
            String queryMode,
            Integer page,
            Integer limit,
            String nickname
    ) {
        QueryModeEnum queryModeEnum = QueryModeEnum.getValue(queryMode);
        LambdaQueryWrapper<UmsUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(UmsUser::getDeleted, GlobalConstants.DELETED_VALUE);
        switch (queryModeEnum) {
            default: // PAGE
                queryWrapper.like(StrUtil.isNotBlank(nickname), UmsUser::getNickname, nickname);
                IPage<UmsUser> result = iUmsUserService.list(new Page<>(page, limit), new UmsUser().setNickname(nickname));
                return Result.success(result.getRecords(), result.getTotal());
        }
    }

    @ApiOperation(value = "会员详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "会员ID", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result getMemberById(
            @PathVariable Long id
    ) {
        UmsUser user = iUmsUserService.getById(id);
        return Result.success(user);
    }

    @ApiOperation(value = "修改会员", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资源id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "member", value = "实体JSON对象", required = true, paramType = "body", dataType = "UmsMember")
    })
    @PutMapping(value = "/{id}")
    public Result update(
            @PathVariable Integer id,
            @RequestBody UmsUser user) {
        boolean status = iUmsUserService.updateById(user);
        return Result.judge(status);
    }

    @ApiOperation(value = "局部更新", httpMethod = "PATCH")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "member", value = "实体JSON对象", required = true, paramType = "body", dataType = "UmsMember")
    })
    @PatchMapping("/{id}")
    public Result patch(@PathVariable Long id, @RequestBody UmsUser user) {
        LambdaUpdateWrapper<UmsUser> updateWrapper = new LambdaUpdateWrapper<UmsUser>().eq(UmsUser::getId, id);
        updateWrapper.set(user.getStatus() != null, UmsUser::getStatus, user.getStatus());
        boolean status = iUmsUserService.update(updateWrapper);
        return Result.judge(status);
    }

    @ApiOperation(value = "删除会员", httpMethod = "DELETE")
    @ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable String ids) {
        boolean status = iUmsUserService.update(new LambdaUpdateWrapper<UmsUser>()
                .in(UmsUser::getId, Arrays.asList(ids.split(",")))
                .set(UmsUser::getDeleted, GlobalConstants.DELETED_VALUE));
        return Result.judge(status);
    }

    @ApiOperation(value = "扣减余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "amount", value = "实体JSON对象", required = true, paramType = "body", dataType = "UmsMember")
    })
    @PatchMapping("/{id}/balance/_deduct")
    public Result deductBalance(@PathVariable Long id, @RequestParam Long amount) {
        LambdaUpdateWrapper<UmsUser> updateWrapper = new LambdaUpdateWrapper<UmsUser>().eq(UmsUser::getId, id);
        updateWrapper.setSql(" balance = balance - " + amount);
        updateWrapper.gt(UmsUser::getBalance, amount);
        boolean result = iUmsUserService.update(updateWrapper);
        return Result.judge(result);
    }
}

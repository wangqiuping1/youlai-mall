package com.youlai.mall.ums.api.app;

import com.youlai.common.result.Result;
import com.youlai.mall.ums.pojo.domain.UmsUser;
import com.youlai.mall.ums.pojo.dto.AuthMemberDTO;
import com.youlai.mall.ums.pojo.dto.MemberDTO;
import com.youlai.mall.ums.pojo.dto.UmsAddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "mall-ums")
public interface MemberFeignService {

    @PostMapping("/api.app/v1/users")
    Result add(@RequestBody UmsUser user);

    /**
     * 获取会员信息
     */
    @GetMapping("/api.app/v1/users/{id}")
    Result<MemberDTO> getUserById(@PathVariable Long id);


    /**
     * 获取认证会员信息
     */
    @GetMapping("/api.app/v1/users/openid/{openid}")
    Result<AuthMemberDTO> getUserByOpenid(@PathVariable String openid);


    /**
     * 修改会员积分
     */
    @PutMapping("/api.admin/v1/users/{id}/point")
    Result updatePoint(@PathVariable Long id, @RequestParam Integer num);

    /**
     * 获取地址详情
     *
     * @param id 地址id
     * @return 地址详情
     */
    @GetMapping("/api.app/v1/addresses/{id}")
    Result<UmsAddressDTO> getAddressById(@PathVariable("id") String id);


    /**
     * 修改会员余额
     */
    @PutMapping("/api.app/v1/users/{id}/balance")
    Result updateBalance(@PathVariable Long id, @RequestParam Long balance);


    /**
     * 获取会员余额
     */
    @GetMapping("/api.app/v1/users/{id}/balance")
    Result<Long> getBalance(@PathVariable Long id);

}



package com.youlai.mall.pms.api.app;

import com.youlai.common.result.Result;
import com.youlai.mall.pms.pojo.dto.SkuDTO;
import com.youlai.mall.pms.pojo.dto.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "mall-pms")
public interface InventoryFeignService {

    /**
     * 获取库存列表
     */
    @GetMapping("/api.app/v1/skus")
    Result<List<SkuDTO>> listBySkuIds(@RequestParam List<Long> ids);

    /**
     * 获取库存信息
     */
    @GetMapping("/api.app/v1/skus/{id}")
    Result<SkuDTO> getSkuById(@PathVariable Long id);

    /**
     * 锁定库存
     */
    @PutMapping("/api.app/v1/skus/batch/lock_inventory")
    Result lockInventory(@RequestBody List<InventoryDTO> list);

    /**
     * 解锁库存
     */
    @PutMapping("/api.app/v1/skus/batch/unlock_inventory")
    Result<Boolean> unlockInventory(@RequestBody List<InventoryDTO> list);


}

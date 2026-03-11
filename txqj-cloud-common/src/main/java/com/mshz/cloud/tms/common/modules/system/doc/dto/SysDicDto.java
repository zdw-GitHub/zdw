package com.mshz.cloud.tms.common.modules.system.doc.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * packageName com.mshz.cloud.tms.common.modules.system.doc.dto
 *
 * @author wuyu
 * @version v1.0
 * @className SysDicDto
 * @date 2025/7/23
 * @description TODO
 */
@Data
@Builder
public class SysDicDto {

    private final String dictType;

    private final String code;

    @JsonCreator
    public SysDicDto(@JsonProperty("dictType") String dictType,
                     @JsonProperty("code") String code) {
        this.dictType = dictType;
        this.code = code;
    }
}
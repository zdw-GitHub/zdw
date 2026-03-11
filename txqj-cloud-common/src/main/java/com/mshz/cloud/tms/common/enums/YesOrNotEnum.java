package com.mshz.cloud.tms.common.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum YesOrNotEnum {

    YES("0","否"),
    NOT("1","是");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    public static String fromValue(String code) {
        for (YesOrNotEnum e : values()) {
            if (StrUtil.equals(e.code, code)) return e.getName();
        }
        return null;
    }

    public static String fromKey(String name) {
        for (YesOrNotEnum e : values()) {
            if (StrUtil.equals(e.name, name)) return e.getCode();
        }
        return null;
    }
}

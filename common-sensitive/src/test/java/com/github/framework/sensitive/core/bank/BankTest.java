package com.github.framework.sensitive.core.bank;

import com.github.framework.sensitive.core.util.strategy.SensitiveStrategyUtils;


public class BankTest {
    public static void main(final String[] args) {
        System.out.println(SensitiveStrategyUtils.accountNo("123123323024"));
        System.out.println(SensitiveStrategyUtils.cardNo("1231233213218888"));

        System.out.println(SensitiveStrategyUtils.cnPhone("138000000000"));
        System.out.println(SensitiveStrategyUtils.hkPhone("12312312"));
        System.out.println(SensitiveStrategyUtils.moPhone("12312312"));

        System.out.println(SensitiveStrategyUtils.cnID("51343319880820693X"));
        System.out.println(SensitiveStrategyUtils.hkID("12312332"));
        System.out.println(SensitiveStrategyUtils.moID("41213532"));

        System.out.println(SensitiveStrategyUtils.chineseName("扶桑"));
        System.out.println(SensitiveStrategyUtils.chineseName("汴京"));
        System.out.println(SensitiveStrategyUtils.chineseName("幽州"));

        System.out.println(SensitiveStrategyUtils.englishName("hello wolrd"));
        System.out.println(SensitiveStrategyUtils.englishName("nichals"));
        System.out.println(SensitiveStrategyUtils.englishName("ailong   ma"));
        System.out.println(SensitiveStrategyUtils.englishName("ailong V Chan"));
        System.out.println(SensitiveStrategyUtils.englishName("ailong  V  Chan"));
    }
}

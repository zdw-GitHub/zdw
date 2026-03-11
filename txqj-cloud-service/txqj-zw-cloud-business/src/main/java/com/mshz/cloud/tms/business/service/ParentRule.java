package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.vo.OneCommRule;
import com.mshz.cloud.tms.business.vo.QualityVerificationRulesVo;
import java.util.List;

/**
 * Create by hzy 2022/10/14
 **/
public interface ParentRule {

    //策略模式
    public List<TQualityVerificationResult> method(QualityVerificationRulesVo qualityVerificationRulesVo);


    public List<TQualityVerificationResult> method(OneCommRule oneCommRule, QualityVerificationRulesVo verificationRule);
}

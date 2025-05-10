package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRuleTreeNodeDAO {
    List<RuleTreeNode> queryRuleTreeNodeVOByTreeId(String treeId);

    List<RuleTreeNode> queryRuleLocks(String[] treeIds);
}

package com.ideatech.ams.account.dao;


import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface HeGuiYuJingAllDao extends JpaRepository<HeGuiYuJingAll, Long>, JpaSpecificationExecutor<HeGuiYuJingAll> {

    @Transactional
    List<String> deleteAllByYuJingType(String yujingtype);
}

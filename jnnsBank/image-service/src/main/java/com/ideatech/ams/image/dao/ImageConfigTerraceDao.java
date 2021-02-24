package com.ideatech.ams.image.dao;


import com.ideatech.ams.image.entity.ImageConfigTerrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageConfigTerraceDao extends JpaRepository<ImageConfigTerrace,Long>, JpaSpecificationExecutor<ImageConfigTerrace> {
}

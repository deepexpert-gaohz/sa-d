package com.ideatech.ams.apply.enums;

/**
 * @author chenxf
 * @company ydrx
 * @date 2/6/2018
 * @description baseEnum
 */
public  interface BaseEnum<E extends Enum<?>, T> {
  public T getValue();
  public String getDisplayName();

}

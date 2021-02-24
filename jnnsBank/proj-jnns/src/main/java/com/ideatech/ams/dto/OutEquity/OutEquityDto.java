package com.ideatech.ams.dto.OutEquity;

import java.util.List;


public class OutEquityDto {

     private String total;
     List<Children1> item;

     @Override
     public String toString() {
          return "OutEquityDto{" +
                  "total='" + total + '\'' +
                  ", item=" + item +
                  '}';
     }

     public String getTotal() {
          return total;
     }

     public void setTotal(String total) {
          this.total = total;
     }

     public List<Children1> getItem() {
          return item;
     }

     public void setItem(List<Children1> item) {
          this.item = item;
     }

}

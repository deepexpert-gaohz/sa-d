package com.ideatech.ams.customer.service.illegal;

import com.ideatech.ams.customer.dto.illegal.IllegalQueryErrorDto;
import org.springframework.stereotype.Service;

public interface IllegalQueryErrorService {

    void save(IllegalQueryErrorDto illegalQueryErrorDto);
}

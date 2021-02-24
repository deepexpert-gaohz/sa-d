package com.ideatech.ams.dto.SaicQuery;

import lombok.Data;

import java.util.List;

@Data
public class SaicQuery {
    private List<Deabbeat> deabbeats;
    private List<Offenderdto> offenderdtos;
    private List<Owing> owings;
    private List<BreakLaw> breakLaws;

}

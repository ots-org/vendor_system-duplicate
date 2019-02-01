package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.swagger.annotations.Api;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSProduct_Ws", description = "This service provides the operations for OTS Product")
@Path("product")
@CrossOrigin
public interface OTSProduct_Ws {

}

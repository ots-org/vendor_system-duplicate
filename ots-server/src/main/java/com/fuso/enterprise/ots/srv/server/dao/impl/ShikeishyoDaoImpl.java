
package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.Part;
import com.fuso.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.fuso.enterprise.ots.srv.common.utils.DateUtils;
import com.fuso.enterprise.ots.srv.server.dao.ShikeishyoDao;
import com.fuso.enterprise.ots.srv.server.model.entity.ShikeishoDetail;
import com.fuso.enterprise.ots.srv.server.model.entity.ShikeishoPartDetail;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class ShikeishyoDaoImpl extends AbstractIptDao<ShikeishoDetail, String> implements ShikeishyoDao {

    public ShikeishyoDaoImpl() {
        super(ShikeishoDetail.class);
    }

    @Override
    public List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole) {
        List<ShikeishoDetail> shikeishos = null;
        if (userRole.contains("RD") || userRole.contains("rd")) {
            Map<String, Object> queryParameter = new HashMap<>();
            queryParameter.put("userId", userId);
            shikeishos = super.getResultListByNamedQuery("getShikeishoForUser", queryParameter);
        } else {
            shikeishos = super.getResultListByNamedQuery("getAllShikeisho", new HashMap<>());
        }
        return shikeishos.stream().map(shikeisho -> convertShikeishyoDetailsFromEntityToDomain(shikeisho)).collect(Collectors.toList());
    }

    private ShikeishyoDetails convertShikeishyoDetailsFromEntityToDomain(ShikeishoDetail shikeisho) {
        ShikeishyoDetails shikeishyoDetails = new ShikeishyoDetails();
        shikeishyoDetails.setShikeishoState(shikeisho.getShikeishoStatus());
        shikeishyoDetails.setShikeisyoNo(shikeisho.getShikeishoDetailEmbedded().getShikeishoNo());
        shikeishyoDetails.setShikeishoDescription(shikeisho.getSubjectEn());
        shikeishyoDetails.setOrderNo(shikeisho.getOrderNo());
        //shikeishyoDetails.setMachineType(shikeisho.getMachineType());
        shikeishyoDetails.setRdDesiredDate(DateUtils.asLocalDate(shikeisho.getDesiredDate()));
        shikeishyoDetails.setDataOfIssue(DateUtils.asLocalDate(shikeisho.getDateOfIssue()));
        shikeishyoDetails.setRequestNo(shikeisho.getRequestNo());
        //shikeishyoDetails.setProductionVolumeUnits(shikeisho.get);
        shikeishyoDetails.setPayout1(shikeisho.getPayoutDestination1());
        shikeishyoDetails.setPayout2(shikeisho.getPayoutDestination2());
        shikeishyoDetails.setPayout3(shikeisho.getPayoutDestination3());
        shikeishyoDetails.setRequestCode(shikeisho.getRequestorCode());
        shikeishyoDetails.setPartsDestination(shikeisho.getRequestDestinationCode());
        shikeishyoDetails.setManagementNo(shikeisho.getManagementNo());
        //shikeishyoDetails.setNetworkNumber(shikeisho.getn);
        shikeishyoDetails.setMaterialCost(shikeisho.getMaterialCost());
        shikeishyoDetails.setDesignerName(shikeisho.getDesigner());
        shikeishyoDetails.setDepartment(shikeisho.getDepartment());
        shikeishyoDetails.setGroup(shikeisho.getGroupCode());
        shikeishyoDetails.setResponsiblePersonL4(shikeisho.getResponsiblePerson14());
        shikeishyoDetails.setPersonInchrge(shikeisho.getPersonInCharge());
        // shikeishyoDetails.setRemarks(shikeisho.get);
        shikeishyoDetails.setEoNo(shikeisho.getShikeishoDetailEmbedded().getEoNo());
        shikeishyoDetails.setRequesterID(shikeisho.getRequestNo());
        shikeishyoDetails.setFundingApprovalLink(shikeisho.getFundApprovedPath());
        //@formatter:off
        shikeishyoDetails.setParts(shikeisho.getShikeishoPartDetails().stream().map(part -> convertPartDetailsFromEntityToDomain(part)).collect(Collectors.toList()));
        //@formatter:on
        return shikeishyoDetails;
    }

    private Part convertPartDetailsFromEntityToDomain(ShikeishoPartDetail shikeishoPartDetail) {
        Part part = new Part();
        // part.setPrototypeCode(shikeishoPartDetail.getShikeishoPartDetailEmbedded().get);
        part.setDistrict(shikeishoPartDetail.getDistrict());
        part.setGeneration(shikeishoPartDetail.getGeneration());
        part.setPartName(shikeishoPartDetail.getPartName());
        part.setPartNumber(shikeishoPartDetail.getShikeishoPartDetailEmbedded().getPartNo());
        part.setQuantity(shikeishoPartDetail.getRequestedQuantity());
        part.setInternalDeliveryDate(DateUtils.asLocalDate(shikeishoPartDetail.getInternalDeliveryTime()));
        part.setImplementationInstruction(shikeishoPartDetail.getImplementationInstructions());
        part.setUpg(shikeishoPartDetail.getUpg());
        part.setInteriorMakerName(shikeishoPartDetail.getInteriorMake());
        part.setPartRemarks(shikeishoPartDetail.getRemarks());
        part.setDrawingNo(shikeishoPartDetail.getDrawing_no());
        part.setDrawingUrl(shikeishoPartDetail.getDrawing_url());
        part.setPps(shikeishoPartDetail.getPps());
        part.setStatus(shikeishoPartDetail.getStatus());
        part.setDueDate(DateUtils.asLocalDate(shikeishoPartDetail.getDueDate()));
        part.setDeliverLocation(shikeishoPartDetail.getDeliveryLocation());
        return part;
    }
}

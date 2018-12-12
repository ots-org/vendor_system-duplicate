
package com.org.enterprise.ots.srv.server.dao.impl;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.org.enterprise.ots.srv.api.model.domain.Part;
import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.org.enterprise.ots.srv.common.exception.BusinessException;
import com.org.enterprise.ots.srv.common.utils.DateUtils;
import com.org.enterprise.ots.srv.server.dao.TmpShikeishyoDao;
import com.org.enterprise.ots.srv.server.model.entity.TmpShikeisho;
import com.org.enterprise.ots.srv.server.model.entity.TmpShikeishoPart;
import com.org.enterprise.ots.srv.server.model.entity.TmpShikeishoPartEmbedded;
import com.org.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class TmpShikeishyoDaoImpl extends AbstractIptDao<TmpShikeisho, String> implements TmpShikeishyoDao {

    public TmpShikeishyoDaoImpl() {
        super(TmpShikeisho.class);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TmpShikeisho readShikeishyo(String shikeishyoNo) {
        return super.findById(shikeishyoNo);
    }

    public void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails, boolean isNewShikeisho) {
        try {
            TmpShikeisho tmpShikeisho = convertTmpShikeishoDetailsFromDomaintoEntity(shikeishyoDetails, isNewShikeisho);
            super.getEntityManager().merge(tmpShikeisho);
        } catch (Exception exception) {
            throw new BusinessException(exception.getMessage(), exception);
        }
    }

    private TmpShikeisho convertTmpShikeishoDetailsFromDomaintoEntity(ShikeishyoDetails shikeishyoDetails, boolean isNewShikeisho) {
        TmpShikeisho tmpShikeisho = new TmpShikeisho();
        tmpShikeisho.setCreationDate(LocalDateTime.now());
        // tmpShikeisho.setApprovalDeptCode(shikeishyoDetails.getDepartment());
        tmpShikeisho.setDateOfIssue(DateUtils.asLocalDate(shikeishyoDetails.getDataOfIssue()));
        // tmpShikeisho.setDeliveryLocation(shikeishyoDetails.get);
        tmpShikeisho.setDepartment(shikeishyoDetails.getDepartment());
        tmpShikeisho.setDesigner(shikeishyoDetails.getDesignerName());
        tmpShikeisho.setDesiredDate(DateUtils.asLocalDate(shikeishyoDetails.getRdDesiredDate()));
        // tmpShikeisho.setExtensionInCharge(shikeishyoDetails.getResponsiblePersonL4());
        // tmpShikeisho.setExtensionl4(shikeishyoDetails.get);
        tmpShikeisho.setGroupCode(shikeishyoDetails.getGroup());
        tmpShikeisho.setMachineType(shikeishyoDetails.getMachineType());
        tmpShikeisho.setManagementNo(shikeishyoDetails.getManagementNo());
        tmpShikeisho.setMaterialCost(shikeishyoDetails.getMaterialCost());
        tmpShikeisho.setNetworkNo(shikeishyoDetails.getNetworkNumber());
        tmpShikeisho.setNewOrUpdated(isNewShikeisho ? "C" : "U");
        tmpShikeisho.setOrderNo(shikeishyoDetails.getOrderNo());
        tmpShikeisho.setPayoutDestination1(shikeishyoDetails.getPayout1());
        tmpShikeisho.setPayoutDestination2(shikeishyoDetails.getPayout2());
        tmpShikeisho.setPayoutDestination3(shikeishyoDetails.getPayout3());
        tmpShikeisho.setPersonInCharge(shikeishyoDetails.getPersonInchrge());
        tmpShikeisho.setProductionVolumeUnits(shikeishyoDetails.getProductionVolumeUnits());
        tmpShikeisho.setRemarks(shikeishyoDetails.getRemarks());
        tmpShikeisho.setRequestDestinationCode(shikeishyoDetails.getRequestCode());
        tmpShikeisho.setRequestNo(shikeishyoDetails.getRequestNo());
        tmpShikeisho.setRequestorCode(shikeishyoDetails.getRequestCode());
        tmpShikeisho.setResponsiblePerson14(shikeishyoDetails.getResponsiblePersonL4());
        tmpShikeisho.setShikeishoNo(shikeishyoDetails.getShikeisyoNo());
        tmpShikeisho.setEoNo(shikeishyoDetails.getEoNo());
        tmpShikeisho.setSubjectEn(shikeishyoDetails.getShikeishoDescription());
        tmpShikeisho.setFundApprovedPath(shikeishyoDetails.getFundingApprovalLink());
        // tmpShikeisho.setSubjectJa(shikeishyoDetails.get);
        tmpShikeisho.setUpdatedOn(isNewShikeisho ? null : LocalDateTime.now());
        // tmpShikeisho.setUserNameLogin(userNameLogin);
        //@formatter:off
        tmpShikeisho.setTmpShikeishoParts(shikeishyoDetails.getParts().stream().map(part -> convertPartDetailsFromDomainToEntity(part, tmpShikeisho)).collect(Collectors.toList()));
        //@formatter:on
        return tmpShikeisho;
    }

    private TmpShikeishoPart convertPartDetailsFromDomainToEntity(Part part, TmpShikeisho tmpShikeisho) {
        TmpShikeishoPart tmpShikeishoPart = new TmpShikeishoPart();
        tmpShikeishoPart.setDistrict(part.getDistrict());
        tmpShikeishoPart.setGeneration(part.getGeneration());
        tmpShikeishoPart.setImplementationInstructions(part.getImplementationInstruction());
        tmpShikeishoPart.setInteriorMake(part.getInteriorMakerName());
        tmpShikeishoPart.setInternalDeliveryTime(DateUtils.asLocalDate(part.getInternalDeliveryDate()));
        tmpShikeishoPart.setPartName(part.getPartName());
        tmpShikeishoPart.setPartRemarks(part.getPartRemarks());
        tmpShikeishoPart.setPrototypeCode(part.getPrototypeCode());
        tmpShikeishoPart.setRequestedQuantity(part.getQuantity());
        TmpShikeishoPartEmbedded tmpShikeishoPartEmbedded = new TmpShikeishoPartEmbedded();
        tmpShikeishoPartEmbedded.setPartNo(part.getPartNumber());
        tmpShikeishoPartEmbedded.setTmpShikeisho(tmpShikeisho);
        tmpShikeishoPart.setTmpShikeishoPartEmbedded(tmpShikeishoPartEmbedded);
        tmpShikeishoPart.setUpg(part.getUpg());
        return tmpShikeishoPart;
    }

    @Override
    public void invokeStoredProcedure() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("start_ssis_job");
        simpleJdbcCall.execute();
    }
}

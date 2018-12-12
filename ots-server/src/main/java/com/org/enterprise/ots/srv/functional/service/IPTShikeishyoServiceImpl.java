package com.org.enterprise.ots.srv.functional.service;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.org.enterprise.ots.srv.api.service.functional.IPTShikeishyoService;
import com.org.enterprise.ots.srv.api.service.functional.IPTStorageService;
import com.org.enterprise.ots.srv.server.dao.ShikeishyoDao;
import com.org.enterprise.ots.srv.server.dao.TmpShikeishyoDao;
import com.org.enterprise.ots.srv.server.model.entity.TmpShikeisho;

@Service
@Transactional
public class IPTShikeishyoServiceImpl implements IPTShikeishyoService {

    private TmpShikeishyoDao tmpShikeishyoDao;

    private ShikeishyoDao shikeishyoDao;

    private IPTStorageService iptStorageService;

    @Inject
    public IPTShikeishyoServiceImpl(TmpShikeishyoDao tmpShikeishyoDao, ShikeishyoDao shikeishyoDao, IPTStorageService iptStorageService) {
        this.tmpShikeishyoDao = tmpShikeishyoDao;
        this.shikeishyoDao = shikeishyoDao;
        this.iptStorageService = iptStorageService;
    }

    public void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails) {
        TmpShikeisho readShikeishyo = tmpShikeishyoDao.readShikeishyo(shikeishyoDetails.getShikeisyoNo());
        String fundingApprovalLink = iptStorageService.storeFile(shikeishyoDetails.getRdApprovalImageBase64Data());
        shikeishyoDetails.setFundingApprovalLink(fundingApprovalLink);
        String department = readShikeishyo != null ? readShikeishyo.getDepartment() : null;
        String lastEoNo = readShikeishyo != null ? readShikeishyo.getEoNo() : null;
        shikeishyoDetails.setEoNo(getEONO(department, lastEoNo));
        tmpShikeishyoDao.createOrUpdateShikeishyo(shikeishyoDetails, readShikeishyo == null);
        tmpShikeishyoDao.invokeStoredProcedure();
    }

    private String getEONO(String department, String lastEoNo) {
        StringBuilder eoNo = new StringBuilder();
        LocalDate currentDate = LocalDate.now();
        Integer year = currentDate.getYear();
        String stringYear = year.toString();
        eoNo.append(stringYear.substring(0, 2));
        if (department != null) {
            eoNo.append(department.substring(0, 2));
        }
        if (lastEoNo != null && lastEoNo.length() == 6) {
            Integer nextNumber = getNextNumer(lastEoNo);
            nextNumber = nextNumber + 1;
            eoNo.append(nextNumber.toString());
        } else {
            eoNo.append("01");
        }
        return eoNo.toString();
    }

    private Integer getNextNumer(String lastEoNo) {
        try {
            return Integer.valueOf(lastEoNo.substring(4, 6));
        } catch (NumberFormatException e) {
            return Integer.valueOf(1);
        }
    }

    @Override
    public List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole) {
        return shikeishyoDao.getAllShikeishyoForUser(userId, userRole);
    }
    
    @Override
    public void testSystem(String userId)
    {
    	return;
    }

}

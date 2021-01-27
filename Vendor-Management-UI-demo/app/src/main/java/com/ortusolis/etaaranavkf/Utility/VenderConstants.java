package com.ortusolis.etaaranavkf.Utility;

import com.ortusolis.etaaranavkf.pojo.AssignedOrderModel;

import java.io.Serializable;

/**
 * Created by amit.pande on 28/7/15.
 */
public class VenderConstants  implements Serializable {
    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String JSON_URL = "https://secure.ccavenue.com/transaction/transaction.do";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";

    private static volatile VenderConstants constants;
    public static AssignedOrderModel assignedOrderModel;

    public static VenderConstants getInstance() {
        if (constants == null) {
            synchronized (VenderConstants.class) {
                if (constants == null) constants = new VenderConstants();
            }
        }
        return constants;
    }
    protected VenderConstants readResolve() {
        return getInstance();
    }

}

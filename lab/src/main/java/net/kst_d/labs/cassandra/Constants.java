package net.kst_d.labs.cassandra;

import java.util.Arrays;
import java.util.List;

public class Constants {
    private Constants() {
    }

    public static final String _SYSNAME = "system"; //bisys processing center
    public static final String _UUID = "rowid";

    public static final String ID = "ID";
    public static final String TERMINAL_ID = "TERMINAL_ID";
    public static final String SERVICE_ID = "SERVICE_ID";
    public static final String DATA = "DATA";
    public static final String PACC = "PACC";
    public static final String AMOUNT = "AMOUNT";
    public static final String SENT_DATE = "SENT_DATE";
    public static final String DESCRIBE = "DESCR";
    public static final String UNICUMCODE = "UNICUMCODE";
    public static final String STATE = "STATE";
    public static final String ERROR_CODE = "ERROR_CODE";
    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String PS_PAY_NUM = "PS_PAY_NUM";
    public static final String OUT_SERV_ID = "OUT_SERV_ID";
    public static final String ACC_ORG_ID = "ACC_ORG_ID";
    public static final String ORG_STATE = "ORG_STATE";
    public static final String ORDER_DATE = "ORDER_DATE";
    public static final String SEND_DATE = "SEND_DATE";
    public static final String PROV_PAY_ID = "PROV_PAY_ID";
    public static final String ATTR1 = "ATTR1";
    public static final String AGENT_FEE_AMOUNT = "AGENT_FEE_AMOUNT";
    public static final String BANK_FEE_AMOUNT = "BANK_FEE_AMOUNT";
    public static final String PAY_TYPE = "PAY_TYPE";
    public static final String CHECK_NUM = "CHECK_NUM";
    public static final String RCPT_ORG_ID = "RCPT_ORG_ID";
    public static final String AGENT_DATE = "AGENT_DATE";
    public static final String PROV_PAY_DATE = "PROV_PAY_DATE";
    public static final String SERV_ROW_ID = "SERV_ROW_ID";
    public static final String ATTR2 = "ATTR2";
    public static final String AGENT_FEE_TYPE = "AGENT_FEE_TYPE";
    public static final String BANK_FEE_TYPE = "BANK_FEE_TYPE";
    public static final String FLAGS = "FLAGS";
    public static final String AGENT_FEE2_TYPE = "AGENT_FEE2_TYPE";
    public static final String AGENT_FEE2_AMOUNT = "AGENT_FEE2_AMOUNT";
    public static final String BANK_FEE2_TYPE = "BANK_FEE2_TYPE";
    public static final String BANK_FEE2_AMOUNT = "BANK_FEE2_AMOUNT";
    public static final String PARENT_PAY_ID = "PARENT_PAY_ID";
    public static final String OUT_TERM_ID = "OUT_TERM_ID";

    public static final List<String> ROW_KEYS = Arrays.asList(ID, TERMINAL_ID, SERVICE_ID, DATA, PACC, AMOUNT, SENT_DATE, DESCRIBE, UNICUMCODE, STATE, ERROR_CODE, ERROR_TEXT,
		    PS_PAY_NUM,
		    OUT_SERV_ID, ACC_ORG_ID, ORG_STATE, ORDER_DATE, SEND_DATE, PROV_PAY_ID, ATTR1, AGENT_FEE_AMOUNT, BANK_FEE_AMOUNT, PAY_TYPE, CHECK_NUM, RCPT_ORG_ID, AGENT_DATE,
		    PROV_PAY_DATE, SERV_ROW_ID, ATTR2, AGENT_FEE_TYPE, BANK_FEE_TYPE, FLAGS, AGENT_FEE2_TYPE, AGENT_FEE2_AMOUNT, BANK_FEE2_TYPE, BANK_FEE2_AMOUNT, PARENT_PAY_ID,
		    OUT_TERM_ID);

}
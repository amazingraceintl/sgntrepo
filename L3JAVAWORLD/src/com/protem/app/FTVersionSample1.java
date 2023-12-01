package com.protem.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import com.temenos.api.TDate;
import com.temenos.api.TField;
import com.temenos.api.TStructure;
import com.temenos.api.TValidationResponse;
import com.temenos.api.exceptions.T24CoreException;
import com.temenos.t24.api.complex.eb.templatehook.TransactionContext;
import com.temenos.t24.api.hook.system.RecordLifecycle;
import com.temenos.t24.api.records.customer.CustomerRecord;
import com.temenos.t24.api.records.fundstransfer.FundsTransferRecord;
import com.temenos.t24.api.system.DataAccess;
import com.temenos.t24.api.system.Date;
import com.temenos.t24.api.system.Session;

/**
 * TODO: Document me!
 *
 * @author Peter
 *
 */
public class FTVersionSample1 extends RecordLifecycle {
    Logger logger = Logger.getLogger("T24");

    @Override
    public void defaultFieldValues(String application, String currentRecordId, TStructure currentRecord,
            TStructure unauthorisedRecord, TStructure liveRecord, TransactionContext transactionContext) {
        // TODO Auto-generated method stub
        FundsTransferRecord ft = new FundsTransferRecord(currentRecord);
        Session session = new Session(this);

        if (session.getUserDispoOfficer().compareTo("") == 0) {
            throw new T24CoreException("", "EB-DOFF.REQUIRED");
        } else {
            if (ft.getTransactionType().getValue().equals("")) {
                ft.setTransactionType("AC");
            }
            if (ft.getDebitCurrency().getValue().equals("")) {
                TField locCurrency = session.getCompanyRecord().getLocalCurrency();
                ft.setDebitCurrency(locCurrency.getValue());
            }
            currentRecord.set(ft.toStructure());
        }
    }

    @Override
    public TValidationResponse validateRecord(String application, String currentRecordId, TStructure currentRecord,
            TStructure unauthorisedRecord, TStructure liveRecord, TransactionContext transactionContext) {
        FundsTransferRecord ft = new FundsTransferRecord(currentRecord);
        DataAccess da = new DataAccess(this);
        Date dt = new Date(this);
        Session session = new Session(this);

        List<TField> ordCustMv = ft.getOrderingCust();
        ListIterator<TField> iterator = ordCustMv.listIterator();
        while (iterator.hasNext()) {
            TField ordCustomerID = iterator.next();
            try {
                new CustomerRecord(da.getRecord("CUSTOMER", ordCustomerID.toString()));
            } catch (T24CoreException e) {
                ordCustomerID.setError("FT-ACRC.MISSING.CUST");
            }
        }
        TField creditValueDate = ft.getCreditValueDate();
        TDate crDate = new TDate(creditValueDate.toString());
        String creditvalueDatetype = dt.getDayType(crDate);

        TField debitValueDate = ft.getDebitValueDate();
        String debDate = debitValueDate.getValue();
        String currentDate = session.getCurrentVariable("!TODAY");

        if (isAfterCutoffTime("18:30:00")) {
            ft.getTransactionType().setOverride("Beyond working hours, Message may not be delivered");
        }
        if(!debDate.equals(currentDate)) {
            debitValueDate.setOverride("Debit Value Date is not Today's date");
        }
        if (creditvalueDatetype.equals("HOLIDAY")) {
            creditValueDate.setOverride("Credit Value Date falls on a holiday");
        }

        return ft.getValidationResponse();
//        return super.validateRecord(application, currentRecordId, currentRecord, unauthorisedRecord, liveRecord,
//                transactionContext);
    }

    private boolean isAfterCutoffTime(String cutoffTime) {
        java.util.Date cutoff;
        java.util.Date systemTime;
        DateFormat df = new SimpleDateFormat("HH:MM:SS");
        Calendar calobj = Calendar.getInstance();
        String formattedDate = df.format(calobj.getTime());
        try {
            systemTime = df.parse(formattedDate);
            cutoff = df.parse(cutoffTime);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse time!");
        }
        return systemTime.after(cutoff);
    }

    // super.defaultFieldValues(application, currentRecordId, currentRecord,
    // unauthorisedRecord, liveRecord,
    // transactionContext);
}
